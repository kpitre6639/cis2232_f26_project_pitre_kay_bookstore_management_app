package ca.hccis.bookstore.entity;

import ca.hccis.util.CisUtility;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A single book held by the book store.
 *
 * Fields are as specified by the business analyst in the project topic
 * document for the Book Store Management App.
 *
 * @author Kay Pitre
 * @since 20260923
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

    /**
     * Default constructor required by Gson.
     */
    public Book() {
    }

    /**
     * Prompt the user for every field of the book.
     *
     * The id is assigned by the controller and the created date/time is set
     * here, so neither is requested from the user.
     */
    public void getInformation() {
        bookName = CisUtility.getInputString("Book name");
        author = CisUtility.getInputString("Author");
        genre = CisUtility.getInputString("Genre (comedy, horror, romance, etc.)");
        price = CisUtility.getInputDouble("Price");
        dateReleased = CisUtility.getInputString("Year released (yyyy)");
        amountSold = CisUtility.getInputInt("Amount sold");
        inventoryAmount = CisUtility.getInputInt("Inventory amount");
        dateSold = CisUtility.getInputString("Date sold (yyyy-MM-dd)");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        createdDateTime = LocalDateTime.now().format(formatter);
    }

    /**
     * Convert this book to a JSON String.
     *
     * @return this book encoded as JSON
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
     * Readable description of this book for display at the console.
     *
     * @return the book's details on a single line
     */
    @Override
    public String toString() {
        return String.format(
                "id=%d | %s by %s | genre=%s | price=%s | released=%s | sold=%d | inventory=%d | dateSold=%s",
                id, bookName, author, genre, CisUtility.toCurrency(price),
                dateReleased, amountSold, inventoryAmount, dateSold);
    }
}
