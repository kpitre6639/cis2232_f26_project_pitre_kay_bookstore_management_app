package ca.hccis.bookstore.entity;

/**
 * A single sales record for one book title on one date.
 *
 * Fields are as specified by the business analyst in the project topic
 * document for the Book Store Management App.  One record represents the
 * copies of a title that sold on a single date, so the same title appears
 * more than once when it has sold on more than one date.
 *
 * The console input and JSON methods from Assignment 1 are deliberately not
 * carried over.  Assignment 2 exercises the cost calculation through unit
 * tests only, so the entity needs to hold state and nothing more.
 *
 * @author Kay Pitre
 * @since 20260928
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
     * Default constructor.
     */
    public Book() {
    }

    /**
     * Convenience constructor for the fields the cost calculation depends on.
     *
     * @param bookName     name of the book
     * @param price        price of one copy at the time of this sale
     * @param dateReleased year the title was first released, yyyy
     * @param amountSold   copies of this title sold on this date
     */
    public Book(String bookName, double price, String dateReleased, int amountSold) {
        this.bookName = bookName;
        this.price = price;
        this.dateReleased = dateReleased;
        this.amountSold = amountSold;
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
     */
    @Override
    public String toString() {
        return String.format(
                "id=%d | %s by %s | genre=%s | price=%.2f | released=%s | sold=%d | inventory=%d | dateSold=%s",
                id, bookName, author, genre, price,
                dateReleased, amountSold, inventoryAmount, dateSold);
    }
}
