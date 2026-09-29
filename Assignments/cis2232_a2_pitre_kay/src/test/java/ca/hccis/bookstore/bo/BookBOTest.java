package ca.hccis.bookstore.bo;

import ca.hccis.bookstore.entity.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit tests for the cost calculation in BookBO.
 *
 * @author Kay Pitre
 * @since 20260928
 */
public class BookBOTest {

    /**
     * Tolerance for comparing currency held in a double.
     */
    private static final double TOLERANCE = 0.001;

    /**
     * A book released before 1950 carries a 50% premium.
     *
     * Written using a test driven development approach.  This test was written
     * before any logic existed in calculateCost, run to confirm it failed
     * against the NO_COST sentinel, and only then was the premium logic added.
     *
     * 2 copies at $10.00 released in 1949 = 20.00 * 1.50 = 30.00
     */
    @Test
    public void testCalculateCostPre1950CarriesFiftyPercentPremium() {
        Book book = new Book("The Old One", 10.00, "1949", 2);

        double actual = BookBO.calculateCost(book);

        assertEquals(30.00, actual, TOLERANCE);
    }

    /**
     * A book released between 1970 and 1989 carries a 20% premium.
     *
     * Written using a test driven development approach.  Written and run
     * before calculateCost handled this band at all, confirmed failing against
     * the NO_COST sentinel, and only then was the 20% band added.
     *
     * 3 copies at $12.99 released in 1977 = 38.97 * 1.20 = 46.764
     */
    @Test
    public void testCalculateCostSeventiesCarriesTwentyPercentPremium() {
        Book book = new Book("The Shining", 12.99, "1977", 3);

        double actual = BookBO.calculateCost(book);

        assertEquals(46.764, actual, TOLERANCE);
    }

    /**
     * A book released after the premium bands carries no premium at all.
     *
     * Written using a test driven development approach.  Written and run
     * before calculateCost returned anything but the NO_COST sentinel for a
     * modern title, and only then was the no-premium case added.
     *
     * The second assertion is the point of this test.  It is not enough that
     * the cost is right; the premium must demonstrably not have been applied,
     * so the test also asserts the result is not the 50% figure.
     *
     * 4 copies at $18.50 released in 2001 = 74.00, unchanged
     */
    @Test
    public void testCalculateCostModernTitleCarriesNoPremium() {
        Book book = new Book("Life of Pi", 18.50, "2001", 4);

        double actual = BookBO.calculateCost(book);

        assertEquals(74.00, actual, TOLERANCE);
        assertNotEquals(74.00 * BookBO.PREMIUM_HIGH_MULTIPLIER, actual, TOLERANCE);
    }
}
