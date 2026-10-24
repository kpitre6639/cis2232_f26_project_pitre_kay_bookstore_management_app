package ca.hccis.bookstore.entity;

import ca.hccis.bookstore.jpa.entity.Book;

import java.util.ArrayList;

/**
 * Entity class to hold the inputs and the results of the income report.
 *
 * The same object is bound to the report form on the way in (year and genre)
 * and carries the matching sales records and the calculated summary back to
 * the view on the way out.
 *
 * @author KP
 * @since 2026-10-24
 */
public class ReportInput {

    //Inputs from the report form.
    private String year;
    private String genre;

    //Results loaded by the report business object.
    private ArrayList<Book> books;
    private IncomeSummary incomeSummary;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public IncomeSummary getIncomeSummary() {
        return incomeSummary;
    }

    public void setIncomeSummary(IncomeSummary incomeSummary) {
        this.incomeSummary = incomeSummary;
    }

    @Override
    public String toString() {
        return "Income report for " + year
                + (genre == null || genre.isEmpty() ? "" : ", genre " + genre);
    }
}
