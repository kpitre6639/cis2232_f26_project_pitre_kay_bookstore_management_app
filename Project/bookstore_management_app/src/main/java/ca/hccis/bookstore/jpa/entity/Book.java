package ca.hccis.bookstore.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * JPA entity for the Book table.  One row is the sales of one title on one date.
 * Built to match createDatabase.sql column for column - do not add business
 * logic here (see BookBO), so the class can be regenerated if the table changes.
 *
 * @author KP
 * @since 2026-10-24
 * @modifiedby KP 2026-11-18 Sprint 3 - added Spring Validation constraints with
 *             user-facing messages so add and edit are validated on the form.
 */
@Entity
@Table(name = "Book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    //Set by the controller when a record is added, carried as a hidden field
    //on the form, never typed by the user.
    @Size(max = 20)
    @NotNull
    @Column(name = "createdDateTime", nullable = false, length = 20)
    private String createdDateTime;

    @NotBlank(message = "Title is required.")
    @Size(max = 100, message = "Title must be 100 characters or fewer.")
    @Column(name = "bookName", nullable = false, length = 100)
    private String bookName;

    @NotBlank(message = "Author is required.")
    @Size(max = 50, message = "Author must be 50 characters or fewer.")
    @Column(name = "author", nullable = false, length = 50)
    private String author;

    @NotBlank(message = "Genre is required.")
    @Size(max = 30, message = "Genre must be 30 characters or fewer.")
    @Column(name = "genre", nullable = false, length = 30)
    private String genre;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.00", message = "Price cannot be negative.")
    @Column(name = "price", nullable = false, precision = 7, scale = 2)
    private BigDecimal price;

    //Held as a String per the BA and the instructor - a four digit year.
    @NotBlank(message = "Year released is required.")
    @Pattern(regexp = "\\d{4}", message = "Year released must be four digits, e.g. 1977.")
    @Column(name = "dateReleased", nullable = false, length = 10)
    private String dateReleased;

    @Min(value = 0, message = "Copies sold cannot be negative.")
    @Column(name = "amountSold")
    private Integer amountSold;

    @Min(value = 0, message = "Inventory cannot be negative.")
    @Column(name = "inventoryAmount")
    private Integer inventoryAmount;

    //Optional, but when present it must be a date the reports can group on.
    @Pattern(regexp = "|\\d{4}-\\d{2}-\\d{2}", message = "Date sold must be yyyy-MM-dd.")
    @Size(max = 10)
    @Column(name = "dateSold", length = 10)
    private String dateSold;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDateReleased() {
        return dateReleased;
    }

    public void setDateReleased(String dateReleased) {
        this.dateReleased = dateReleased;
    }

    public Integer getAmountSold() {
        return amountSold;
    }

    public void setAmountSold(Integer amountSold) {
        this.amountSold = amountSold;
    }

    public Integer getInventoryAmount() {
        return inventoryAmount;
    }

    public void setInventoryAmount(Integer inventoryAmount) {
        this.inventoryAmount = inventoryAmount;
    }

    public String getDateSold() {
        return dateSold;
    }

    public void setDateSold(String dateSold) {
        this.dateSold = dateSold;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", createdDateTime='" + createdDateTime + '\'' +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", price=" + price +
                ", dateReleased='" + dateReleased + '\'' +
                ", amountSold=" + amountSold +
                ", inventoryAmount=" + inventoryAmount +
                ", dateSold='" + dateSold + '\'' +
                '}';
    }
}
