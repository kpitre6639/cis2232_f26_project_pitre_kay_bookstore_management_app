package ca.hccis.bookstore;

import ca.hccis.bookstore.entity.Book;
import ca.hccis.util.CisUtility;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Console application for the Book Store Management App.
 *
 * Books added by the user are saved to a JSON file so that they are still
 * available the next time the program is started.
 *
 * Note: the calculations described in the project topic document are not
 * coded here.  They are completed in the unit testing assignment.
 *
 * @author Kay Pitre
 * @since 20260923
 */
public class Controller {

    public static final String OPTION_ADD = "A";
    public static final String OPTION_VIEW = "V";
    public static final String OPTION_EXIT = "X";

    public static final String MENU = System.lineSeparator()
            + "--Book Store Management--" + System.lineSeparator()
            + OPTION_ADD + ") Add" + System.lineSeparator()
            + OPTION_VIEW + ") View" + System.lineSeparator()
            + OPTION_EXIT + ") eXit" + System.lineSeparator()
            + "Choose an option";

    public static final String MESSAGE_ERROR = "That is not a valid option.";
    public static final String MESSAGE_EXIT = "Goodbye";
    public static final String MESSAGE_SUCCESS = "Book saved successfully.";
    public static final String MESSAGE_NONE_FOUND = "No books found.";

    public static final String PATH_NAME = "c:\\cis2232\\data_pitre_kay.json";

    private static LinkedHashMap<Integer, Book> bookMap = new LinkedHashMap<>();
    private static Gson gson = new Gson();

    /**
     * Start the application.  The data folder is created if needed, any books
     * already on file are loaded, and the menu is shown until the user exits.
     *
     * @param args not used
     */
    public static void main(String[] args) {

        createDataFolder();
        readAll();

        String menuOption;

        do {
            menuOption = CisUtility.getInputString(MENU).toUpperCase();

            switch (menuOption) {
                case OPTION_ADD:
                    add();
                    break;
                case OPTION_VIEW:
                    viewAll();
                    break;
                case OPTION_EXIT:
                    System.out.println(MESSAGE_EXIT);
                    break;
                default:
                    System.out.println(MESSAGE_ERROR);
                    break;
            }
        } while (!menuOption.equals(OPTION_EXIT));
    }

    /**
     * Create the c:\cis2232 folder if it does not already exist.
     *
     * FileWriter will create a missing file but not a missing folder, so this
     * must run before anything is written.
     */
    public static void createDataFolder() {
        try {
            Path folder = Paths.get(PATH_NAME).getParent();
            Files.createDirectories(folder);
        } catch (IOException e) {
            System.out.println("Could not create the data folder: " + e.getMessage());
        }
    }

    /**
     * Processing for menu option A.  Gather a new book from the user, give it
     * the next available id, and save every book to the file.
     */
    public static void add() {
        System.out.println(System.lineSeparator() + "--Add Book--");

        Book newBook = new Book();
        newBook.getInformation();
        newBook.setId(getNextId());

        bookMap.put(newBook.getId(), newBook);
        writeAll();

        System.out.println(MESSAGE_SUCCESS);
    }

    /**
     * Work out the next id to use, based on the highest id currently held.
     *
     * @return the next available id, starting at 1
     */
    private static int getNextId() {
        int highest = 0;
        for (Integer current : bookMap.keySet()) {
            if (current > highest) {
                highest = current;
            }
        }
        return highest + 1;
    }

    /**
     * Processing for menu option V.  Reload the books from the file so that
     * what is shown is always what is stored, then display them.
     */
    public static void viewAll() {
        readAll();

        System.out.println(System.lineSeparator() + "--Books on File--");

        if (bookMap.isEmpty()) {
            System.out.println(MESSAGE_NONE_FOUND);
        } else {
            for (Book current : bookMap.values()) {
                System.out.println(current.toString());
            }
            System.out.println(bookMap.size() + " book(s) found.");
        }
    }

    /**
     * Write every book held in memory to the data file, one JSON object per
     * line.  The file is replaced each time so it always matches the map.
     */
    public static void writeAll() {
        try (FileWriter writer = new FileWriter(PATH_NAME, false)) {
            for (Book current : bookMap.values()) {
                writer.append(gson.toJson(current));
                writer.append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Could not write to " + PATH_NAME + ": " + e.getMessage());
        }
    }

    /**
     * Load every book from the data file into memory.  A missing file simply
     * means nothing has been added yet, which is not an error.
     */
    public static void readAll() {
        Path path = Paths.get(PATH_NAME);

        if (!Files.exists(path)) {
            return;
        }

        try {
            bookMap.clear();
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (!line.isBlank()) {
                    Book bookFromJson = gson.fromJson(line, Book.class);
                    bookMap.put(bookFromJson.getId(), bookFromJson);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read " + PATH_NAME + ": " + e.getMessage());
        }
    }
}
