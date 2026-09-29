package ca.hccis.bookstore.bo;

import ca.hccis.bookstore.entity.Book;

/**
 * Business logic for the Book Store Management App.
 *
 * The calculation lives here rather than on the entity because the entity
 * classes are generated from the database later in the project, which would
 * discard any custom method written into them.
 *
 * Cost is the value of a number of copies of a title with the vintage premium
 * applied.  It is used two ways from the same logic.  Against a sales record,
 * where the quantity is the copies that record says sold, it gives the income
 * those sales produced.  Against a customer's request, where the quantity is
 * the number of copies they want, it gives a price quote.
 *
 * The premium bands follow revision 2 of the project topic document.  The
 * original document contradicted itself on the 50% band, giving both pre-1970
 * and pre-1950; pre-1950 is used here pending confirmation from the business
 * analyst, which is why the boundary years are constants rather than literals.
 *
 * @author Kay Pitre
 * @since 20260928
 */
public class BookBO {

    /**
     * Returned when no cost could be determined.  Not a valid cost.
     */
    public static final double NO_COST = -1;

    /**
     * Titles released before this year carry the higher premium.
     */
    public static final int PREMIUM_HIGH_BEFORE_YEAR = 1950;

    /**
     * First year of the lower premium band.
     */
    public static final int PREMIUM_LOW_START_YEAR = 1970;

    /**
     * Last year of the lower premium band.
     */
    public static final int PREMIUM_LOW_END_YEAR = 1989;

    /**
     * Multiplier applied to titles released before PREMIUM_HIGH_BEFORE_YEAR.
     */
    public static final double PREMIUM_HIGH_MULTIPLIER = 1.50;

    /**
     * Multiplier applied to titles released inside the lower premium band.
     */
    public static final double PREMIUM_LOW_MULTIPLIER = 1.20;

    /**
     * Multiplier applied when no premium is due.
     */
    public static final double NO_PREMIUM_MULTIPLIER = 1.00;

    /**
     * Calculate the cost of the copies this record says were sold.
     *
     * @param book the sales record
     * @return the cost of the copies sold, or NO_COST when the release year
     *         cannot be read
     */
    public static double calculateCost(Book book) {
        if (book == null) {
            return NO_COST;
        }
        return calculateCost(book, book.getAmountSold());
    }

    /**
     * Calculate the cost of a given number of copies of a title.
     *
     * Used for a customer's price quote, where the quantity is what they want
     * to buy rather than what the record says has sold.
     *
     * @param book     the title being priced
     * @param quantity the number of copies
     * @return the cost of that many copies, or NO_COST when the release year
     *         cannot be read
     */
    public static double calculateCost(Book book, int quantity) {
        if (book == null) {
            return NO_COST;
        }

        double multiplier = getVintageMultiplier(book.getDateReleased());
        if (multiplier == NO_COST) {
            return NO_COST;
        }

        return quantity * book.getPrice() * multiplier;
    }

    /**
     * Determine the vintage premium multiplier for a release year.
     *
     * Extracted from calculateCost so the bands can be tested on their own,
     * without having to build a whole Book to check a boundary year.
     *
     * @param dateReleased the year the title was first released, yyyy
     * @return the multiplier to apply, or NO_COST when the year cannot be read
     */
    public static double getVintageMultiplier(String dateReleased) {
        int yearReleased;

        try {
            yearReleased = Integer.parseInt(dateReleased.trim());
        } catch (NumberFormatException | NullPointerException e) {
            return NO_COST;
        }

        if (yearReleased < PREMIUM_HIGH_BEFORE_YEAR) {
            return PREMIUM_HIGH_MULTIPLIER;
        }

        if (yearReleased >= PREMIUM_LOW_START_YEAR && yearReleased <= PREMIUM_LOW_END_YEAR) {
            return PREMIUM_LOW_MULTIPLIER;
        }

        return NO_PREMIUM_MULTIPLIER;
    }
}
