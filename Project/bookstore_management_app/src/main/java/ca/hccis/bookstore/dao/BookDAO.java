package ca.hccis.bookstore.dao;

import ca.hccis.bookstore.jpa.entity.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Data access object for the Book table, using JDBC.
 *
 * All of the JDBC work for the income report lives here.  The business object
 * calls these methods and never touches the connection itself.  Sprint 3 will
 * add a Spring Data repository for the CRUD screens; the report stays on JDBC
 * as the sprint requires.
 *
 * @author KP
 * @since 2026-10-24
 */
public class BookDAO {

    private static final Logger logger = LoggerFactory.getLogger(BookDAO.class);

    private Connection conn = null;

    /**
     * Open a connection using the datasource settings in application.properties,
     * the same settings Spring uses, so there is only one place to change them.
     *
     * @author KP
     * @since 2026-10-24
     */
    public BookDAO() {

        ResourceBundle rb = ResourceBundle.getBundle("application");
        String connectionString = rb.getString("spring.datasource.url");
        String userName = rb.getString("spring.datasource.username");
        String password = rb.getString("spring.datasource.password");

        try {
            conn = DriverManager.getConnection(connectionString, userName, password);
        } catch (SQLException e) {
            logger.error("BookDAO could not connect to the database: " + e);
        }
    }

    /**
     * Select the sales records for one year, optionally limited to one genre.
     *
     * @param year  the year of dateSold to report on, yyyy
     * @param genre the genre to limit to, or null/empty for all genres
     * @return the matching records ordered by date sold, empty if none
     * @author KP
     * @since 2026-10-24
     */
    public ArrayList<Book> selectAllByYear(String year, String genre) {

        ArrayList<Book> books = new ArrayList<>();
        boolean filterGenre = genre != null && !genre.isEmpty();

        //**********************************************************************
        // dateSold is held as yyyy-MM-dd text, so the year is its first four
        // characters.  A prepared statement is used so the user's input is
        // passed as a parameter rather than built into the SQL string.
        //**********************************************************************
        String sql = "SELECT * FROM Book WHERE SUBSTRING(dateSold, 1, 4) = ?"
                + (filterGenre ? " AND genre = ?" : "")
                + " ORDER BY dateSold, bookName";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, year);
            if (filterGenre) {
                stmt.setString(2, genre);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                books = loadList(rs);
            }

        } catch (SQLException e) {
            logger.error("BookDAO.selectAllByYear failed: " + e);
        }

        return books;
    }

    /**
     * Select every sales record.  Used for the all-years average.
     *
     * @return all records, empty if none
     * @author KP
     * @since 2026-10-24
     */
    public ArrayList<Book> selectAll() {

        ArrayList<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Book ORDER BY dateSold, bookName";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            books = loadList(rs);

        } catch (SQLException e) {
            logger.error("BookDAO.selectAll failed: " + e);
        }

        return books;
    }

    /**
     * Select the distinct years that have sales on record, newest first.
     * Feeds the year drop-down on the report form.
     *
     * @return the years as yyyy strings
     * @author KP
     * @since 2026-10-24
     */
    public ArrayList<String> selectDistinctYears() {

        ArrayList<String> years = new ArrayList<>();
        String sql = "SELECT DISTINCT SUBSTRING(dateSold, 1, 4) AS saleYear FROM Book"
                + " WHERE dateSold IS NOT NULL ORDER BY saleYear DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                years.add(rs.getString("saleYear"));
            }

        } catch (SQLException e) {
            logger.error("BookDAO.selectDistinctYears failed: " + e);
        }

        return years;
    }

    /**
     * Select the distinct genres that have sales on record, alphabetically.
     * Feeds the genre drop-down on the report form.
     *
     * @return the genres
     * @author KP
     * @since 2026-10-24
     */
    public ArrayList<String> selectDistinctGenres() {

        ArrayList<String> genres = new ArrayList<>();
        String sql = "SELECT DISTINCT genre FROM Book ORDER BY genre";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                genres.add(rs.getString("genre"));
            }

        } catch (SQLException e) {
            logger.error("BookDAO.selectDistinctGenres failed: " + e);
        }

        return genres;
    }

    /**
     * Load a list of Book objects from a result set.  Column names match
     * createDatabase.sql exactly.
     *
     * @param rs the result set positioned before the first row
     * @return the books, one per row
     * @throws SQLException if a column cannot be read
     * @author KP
     * @since 2026-10-24
     */
    private ArrayList<Book> loadList(ResultSet rs) throws SQLException {

        ArrayList<Book> books = new ArrayList<>();

        //Loop through the result set using the next method, one row per book.
        while (rs.next()) {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setCreatedDateTime(rs.getString("createdDateTime"));
            book.setBookName(rs.getString("bookName"));
            book.setAuthor(rs.getString("author"));
            book.setGenre(rs.getString("genre"));
            book.setPrice(rs.getBigDecimal("price"));
            book.setDateReleased(rs.getString("dateReleased"));
            book.setAmountSold(rs.getInt("amountSold"));
            book.setInventoryAmount(rs.getInt("inventoryAmount"));
            book.setDateSold(rs.getString("dateSold"));
            books.add(book);
        }

        return books;
    }

    /**
     * Close the connection.  Called by the business object when the report
     * run is finished.
     *
     * @author KP
     * @since 2026-10-24
     */
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            logger.error("BookDAO could not close the connection: " + e);
        }
    }
}
