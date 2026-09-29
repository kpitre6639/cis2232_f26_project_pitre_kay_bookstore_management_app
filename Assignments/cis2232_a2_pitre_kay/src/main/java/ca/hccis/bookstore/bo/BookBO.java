package ca.hccis.bookstore.bo;

import ca.hccis.bookstore.entity.Book;

/**
 * Business logic for the Book Store Management App.
 *
 * The calculation lives here rather than on the entity because the entity
 * classes are generated from the database later in the project, which would
 * discard any custom method written into them.
 *
 * @author Kay Pitre
 * @since 20260928
 */
public class BookBO {

    public static final double NO_COST = -1;

    public static final int PREMIUM_HIGH_BEFORE_YEAR = 1950;
    public static final int PREMIUM_LOW_START_YEAR = 1970;
    public static final int PREMIUM_LOW_END_YEAR = 1989;

    public static final double PREMIUM_HIGH_MULTIPLIER = 1.50;
    public static final double PREMIUM_LOW_MULTIPLIER = 1.20;
    public static final double NO_PREMIUM_MULTIPLIER = 1.00;

    /**
     * Calculate the cost of the copies this sales record says were sold.
     *
     * This is the entry point named in the assignment requirements,
     * +calculate(Entity):double.  It delegates to the quantity version using
     * the quantity held on the record itself.
     *
     * @param book the sales record to price
     * @return the cost of the copies sold, or NO_COST if it cannot be determined
     * @author Kay Pitre
     * @since 20260928
     */
    public static double calculate(Book book) {

        //A missing record returns the sentinel rather than throwing, so that
        //one bad row cannot bring a whole report down.
        if (book == null) {
            return NO_COST;
        }

        return calculate(book, book.getAmountSold());
    }

    /**
     * Calculate the cost of a given number of copies of a title.
     *
     * Used for a customer's price quote, where the quantity is the number of
     * copies they want to buy rather than the number the record says has sold.
     *
     * @param book     the title being priced
     * @param quantity the number of copies being priced
     * @return the cost of that many copies, or NO_COST if it cannot be determined
     * @author Kay Pitre
     * @since 20260928
     */
    public static double calculate(Book book, int quantity) {

        if (book == null) {
            return NO_COST;
        }

        //Work out the premium first.  An unreadable release year means the
        //premium cannot be determined, so no cost can be quoted either.
        double multiplier = getVintageMultiplier(book.getDateReleased());
        if (multiplier == NO_COST) {
            return NO_COST;
        }

        //**********************************************************************
        // Applying the premium to the line total gives the same result as
        // applying it to the unit price and then multiplying, which is noted in
        // the topic document so that either implementation reads as correct.
        //**********************************************************************
        return quantity * book.getPrice() * multiplier;
    }

    /**
     * Determine the vintage premium multiplier for a release year.
     *
     * Extracted from calculate so the premium bands can be tested on their own,
     * without having to build a whole Book just to check a boundary year.
     *
     * @param dateReleased the year the title was first released, yyyy
     * @return the multiplier to apply, or NO_COST if the year cannot be read
     * @author Kay Pitre
     * @since 20260928
     */
    public static double getVintageMultiplier(String dateReleased) {

        int yearReleased;

        //dateReleased is held as a String on the entity as specified by the
        //business analyst, so it has to be parsed before it can be compared.
        //Anything that is not a whole number is treated as unknown rather than
        //being allowed to throw.
        try {
            yearReleased = Integer.parseInt(dateReleased.trim());
        } catch (NumberFormatException | NullPointerException e) {
            return NO_COST;
        }

        //**********************************************************************
        // Premium bands follow revision 2 of the project topic document.  The
        // original contradicted itself on the 50% band, giving both pre-1970 in
        // the description and pre-1950 in the calculation section.  Pre-1950 is
        // used here pending confirmation from the business analyst, which is why
        // the boundary years are constants rather than literals.
        //**********************************************************************

        //Oldest band first.  The bands do not overlap so the order does not
        //change the result, but reading oldest to newest matches the way the
        //premiums are described in the requirements.
        if (yearReleased < PREMIUM_HIGH_BEFORE_YEAR) {
            return PREMIUM_HIGH_MULTIPLIER;
        }

        if (yearReleased >= PREMIUM_LOW_START_YEAR && yearReleased <= PREMIUM_LOW_END_YEAR) {
            return PREMIUM_LOW_MULTIPLIER;
        }

        //Everything outside the two premium bands sells at face value.
        return NO_PREMIUM_MULTIPLIER;
    }
}
