package ca.hccis.bookstore;

import ca.hccis.bookstore.threads.ThreadBookstore;
import ca.hccis.util.CisUtility;

/**
 * Controls the overall flow of the program.
 *
 * Assignment 3 runs the application in two separate threads.  Both run the
 * same menu from ThreadBookstore; the only difference between them is the
 * CisUtility they are given.  The first talks to the user on the console, the
 * second through JOptionPane dialogs.
 *
 * Both threads add to the same data file, which is what accumulates the
 * entries from the two of them.
 *
 * @author Kay Pitre
 * @since 20261012
 */
public class Controller {

    /**
     * Start the console thread and the GUI thread.
     *
     * @param args not used
     * @author Kay Pitre
     * @since 20261012
     */
    public static void main(String[] args) {

        //Each thread gets its own CisUtility.  That is what decides whether it
        //runs on the console or through JOptionPane, and it also means each
        //console instance owns its own Scanner rather than sharing one.
        Thread threadConsole = new ThreadBookstore(new CisUtility(false));
        Thread threadGUI = new ThreadBookstore(new CisUtility(true));

        threadConsole.setName("console");
        threadGUI.setName("gui");

        threadConsole.start();
        threadGUI.start();
    }
}
