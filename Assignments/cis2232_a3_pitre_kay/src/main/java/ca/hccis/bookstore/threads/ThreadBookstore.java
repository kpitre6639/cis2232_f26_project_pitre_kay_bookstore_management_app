package ca.hccis.bookstore.threads;

import ca.hccis.bookstore.bo.BookBO;
import ca.hccis.bookstore.entity.Book;
import ca.hccis.util.CisUtility;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * The Book Store Management App menu, written to run inside a thread.
 *
 * One instance of this class runs on the console and another runs through
 * JOptionPane.  Which one it is depends entirely on the CisUtility it is given,
 * so the menu logic itself is written once and shared.
 *
 * @author Kay Pitre
 * @since 20261012
 */
public class ThreadBookstore extends Thread {

    public static final String OPTION_ADD = "A";
    public static final String OPTION_VIEW = "V";
    public static final String OPTION_EXIT = "X";

    public static final String MENU = System.lineSeparator()
            + "--Book Store Management App--" + System.lineSeparator()
            + OPTION_ADD + ") Add a book" + System.lineSeparator()
            + OPTION_VIEW + ") View all books" + System.lineSeparator()
            + OPTION_EXIT + ") eXit" + System.lineSeparator();

    public static final String MESSAGE_ERROR = "That is not a valid option.";
    public static final String MESSAGE_EXIT = "Goodbye.";
    public static final String MESSAGE_NONE_FOUND = "No books on file yet.";

    public static final String FOLDER_NAME = "c:\\cis2232";
    public static final String PATH_NAME = FOLDER_NAME + "\\data_pitre_kay.json";

    //**************************************************************************
    // Both threads read and write the same file and both update the same
    // accumulators, so every one of those operations happens inside a block
    // synchronized on this single shared lock.  Without it the two threads can
    // interleave a read and a write and corrupt the file, or both read the same
    // highest id and assign it twice.
    //**************************************************************************
    private static final Object FILE_LOCK = new Object();

    //Accumulators shared by both threads.  Updated and displayed after every
    //book is added, from whichever thread added it.
    private static int totalBooksAdded = 0;
    private static double totalValueAdded = 0;

    private CisUtility cisUtility;

    /**
     * Create a thread which talks to the user through the given utility.
     *
     * @param cisUtility console or GUI utility for this thread
     * @author Kay Pitre
     * @since 20261012
     */
    public ThreadBookstore(CisUtility cisUtility) {
        this.cisUtility = cisUtility;
    }

    public void setCisUtility(CisUtility cisUtility) {
        this.cisUtility = cisUtility;
    }

    /**
     * Run the menu until the user chooses to exit.
     *
     * @author Kay Pitre
     * @since 20261012
     */
    @Override
    public void run() {

        createDataFolder();

        String menuOption;

        do {
            menuOption = cisUtility.getInputString(MENU).trim().toUpperCase();

            switch (menuOption) {
                case OPTION_ADD:
                    processAdd();
                    break;
                case OPTION_VIEW:
                    processView();
                    break;
                case OPTION_EXIT:
                    cisUtility.display(MESSAGE_EXIT);
                    break;
                default:
                    cisUtility.display(MESSAGE_ERROR);
                    break;
            }
        } while (!menuOption.equals(OPTION_EXIT));
    }

    /**
     * Create the data folder if it is not already there.
     *
     * @author Kay Pitre
     * @since 20261012
     */
    public void createDataFolder() {

        try {
            Files.createDirectories(Paths.get(FOLDER_NAME));
        } catch (IOException e) {
            cisUtility.display("Could not create the data folder: " + e.getMessage());
        }
    }

    /**
     * Prompt for a book, save it to the file, then update and display the
     * accumulated totals.
     *
     * @author Kay Pitre
     * @since 20261012
     */
    public void processAdd() {

        Book book = new Book(cisUtility);
        book.getInformation();

        double cost = BookBO.calculate(book);

        //Everything that touches the file or the accumulators is done in one
        //synchronized block so the id, the write and the totals stay in step
        //even when both threads add a book at the same moment.
        synchronized (FILE_LOCK) {

            book.setId(getNextId());
            writeBook(book);

            totalBooksAdded++;
            if (cost != BookBO.NO_COST) {
                totalValueAdded += cost;
            }
        }

        String threadName = cisUtility.getIsGUI() ? "GUI" : "Console";

        cisUtility.display("Book added from the " + threadName + " thread."
                + System.lineSeparator()
                + book.toString()
                + System.lineSeparator()
                + "Cost of these copies: "
                + (cost == BookBO.NO_COST ? "could not be calculated" : CisUtility.toCurrency(cost))
                + System.lineSeparator()
                + System.lineSeparator()
                + "--Accumulated across both threads--" + System.lineSeparator()
                + "Books added this run: " + totalBooksAdded + System.lineSeparator()
                + "Total value added this run: " + CisUtility.toCurrency(totalValueAdded));
    }

    /**
     * Read every book from the file and display them.
     *
     * The whole listing is built into one String before being displayed, so the
     * GUI thread shows a single dialog rather than one popup per book.
     *
     * @author Kay Pitre
     * @since 20261012
     */
    public void processView() {

        List<Book> books = readAll();

        if (books.isEmpty()) {
            cisUtility.display(MESSAGE_NONE_FOUND);
            return;
        }

        StringBuilder output = new StringBuilder("--Books on File--" + System.lineSeparator());

        for (Book current : books) {
            double cost = BookBO.calculate(current);
            output.append(current.toString())
                    .append(" | cost=")
                    .append(cost == BookBO.NO_COST ? "n/a" : CisUtility.toCurrency(cost))
                    .append(System.lineSeparator());
        }

        output.append(System.lineSeparator())
                .append(books.size())
                .append(" book(s) found.");

        cisUtility.display(output.toString());
    }

    /**
     * Append one book to the data file as a line of JSON.
     *
     * The writer is opened and closed on every call rather than being held open
     * for the life of the thread.  Two threads each holding an open writer on
     * the same file is what causes the file to lock, so this avoids it.
     *
     * Callers must already hold FILE_LOCK.
     *
     * @param book the book to write
     * @author Kay Pitre
     * @since 20261012
     */
    private void writeBook(Book book) {

        try (FileWriter writer = new FileWriter(PATH_NAME, true)) {
            writer.append(book.toJson());
            writer.append(System.lineSeparator());
        } catch (IOException e) {
            cisUtility.display("Could not write to the data file: " + e.getMessage());
        }
    }

    /**
     * Read every book currently in the data file.
     *
     * @return the books on file, empty when the file does not exist yet
     * @author Kay Pitre
     * @since 20261012
     */
    private List<Book> readAll() {

        ArrayList<Book> books = new ArrayList<>();
        Gson gson = new Gson();

        synchronized (FILE_LOCK) {

            Path path = Paths.get(PATH_NAME);

            //A missing file simply means nothing has been added yet.
            if (!Files.exists(path)) {
                return books;
            }

            try {
                for (String line : Files.readAllLines(path)) {
                    if (!line.isBlank()) {
                        books.add(gson.fromJson(line, Book.class));
                    }
                }
            } catch (IOException e) {
                cisUtility.display("Could not read the data file: " + e.getMessage());
            }
        }

        return books;
    }

    /**
     * Work out the next id to use, one higher than the highest on file.
     *
     * Callers must already hold FILE_LOCK, otherwise both threads can read the
     * same highest id and assign it twice.
     *
     * @return the next available id
     * @author Kay Pitre
     * @since 20261012
     */
    private int getNextId() {

        int highest = 0;

        for (Book current : readAll()) {
            if (current.getId() > highest) {
                highest = current.getId();
            }
        }

        return highest + 1;
    }
}
