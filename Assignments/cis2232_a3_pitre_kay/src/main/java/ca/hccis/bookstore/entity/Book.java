package ca.hccis.bookstore.entity;

import ca.hccis.util.CisUtility;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A single sales record for one book title on one date.
 *
 * Fields are as specified by the business analyst in the project topic
 * document for the Book Store Management App.
 *
 * For Assignment 3 the utility is held on the instance rather than used
 * statically, so a book created by the console thread prompts on the console
 * and a book created by the GUI thread prompts through JOptionPane.
 *
 * @author Kay Pitre
 * @since 20261012
 */
public class Book {

    private int id;
    private String bookName;
    private String author;
    private String genre;
    private double price;
    private String dateReleased;
    private int amountSold;
    private int inventoryAmount;
    private String dateSold;
    private String createdDateTime;

    //Not serialised to the file - transient keeps Gson from writing it out.
    private transient CisUtility cisUtility;

    /**
     * Default constructor required by Gson when reading records back.
     *
     * @author Kay Pitre
     * @since 20261012
     */
    public Book() {
    }

    /**
     * Create a book which will prompt through the given utility.
     *
     * @param cisUtility the console or GUI utility this book should use
     * @author Kay Pitre
     * @since 20261012
     */
    public Book(CisUtility cisUtility) {
        this.cisUtility = cisUtility;
    }

    public void setCisUtility(CisUtility cisUtility) {
        this.cisUtility = cisUtility;
    }

    /**
     * Prompt the user for every field of the book.
     *
     * The id is assigned by the thread and the created date/time is set here,
     * so neither is requested from the user.
     *
     * @author Kay Pitre
     * @since 20261012
     */
    public void getInformation() {

        bookName = cisUtility.getInputString("Book name");
        author = cisUtility.getInputString("Author");
        genre = cisUtility.getInputString("Genre (comedy, horror, romance, etc.)");
        price = cisUtility.getInputDouble("Price");
        dateReleased = cisUtility.getInputString("Year released (yyyy)");
        amountSold = cisUtility.getInputInt("Amount sold");
        inventoryAmount = cisUtility.getInputInt("Inventory amount");
        dateSold = cisUtility.getInputString("Date sold (yyyy-MM-dd)");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        createdDateTime = LocalDateTime.now().format(formatter);
    }

    /**
     * Convert this book to a JSON String.
     *
     * @return this book encoded as JSON
     * @author Kay Pitre
     * @since 20261012
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDateReleased() {
        return dateReleased;
    }

    public void setDateReleased(String dateReleased) {
        this.dateReleased = dateReleased;
    }

    public int getAmountSold() {
        return amountSold;
    }

    public void setAmountSold(int amountSold) {
        this.amountSold = amountSold;
    }

    public int getInventoryAmount() {
        return inventoryAmount;
    }

    public void setInventoryAmount(int inventoryAmount) {
        this.inventoryAmount = inventoryAmount;
    }

    public String getDateSold() {
        return dateSold;
    }

    public void setDateSold(String dateSold) {
        this.dateSold = dateSold;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    /**
     * Readable description of this sales record.
     *
     * @return the record's details on a single line
     * @author Kay Pitre
     * @since 20261012
     */
    @Override
    public String toString() {
        return String.format(
                "id=%d | %s by %s | genre=%s | price=%s | released=%s | sold=%d | inventory=%d | dateSold=%s",
                id, bookName, author, genre, CisUtility.toCurrency(price),
                dateReleased, amountSold, inventoryAmount, dateSold);
    }
}
