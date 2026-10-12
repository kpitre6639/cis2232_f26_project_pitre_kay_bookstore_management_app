package ca.hccis.util;

import javax.swing.JOptionPane;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

/**
 * Utility methods for getting input from the user and displaying output.
 *
 * For Assignment 3 this class is instance based rather than static.  Each
 * thread is given its own CisUtility, which is what allows the same thread
 * code to run once on the console and once through JOptionPane.  It also means
 * each console instance owns its own Scanner, so two threads are never reading
 * System.in through the same object.
 *
 * @author Kay Pitre
 * @since 20261012
 */
public class CisUtility {

    private Scanner input = new Scanner(System.in);

    //Decides whether this instance talks to the user through JOptionPane or
    //through the console.  Set once when the thread is created.
    private boolean isGUI = false;

    /**
     * Default constructor.  Console mode.
     *
     * @author Kay Pitre
     * @since 20261012
     */
    public CisUtility() {
    }

    /**
     * Create a utility in either GUI or console mode.
     *
     * @param isGUI true to use JOptionPane, false to use the console
     * @author Kay Pitre
     * @since 20261012
     */
    public CisUtility(boolean isGUI) {
        this.isGUI = isGUI;
    }

    public boolean getIsGUI() {
        return isGUI;
    }

    public void setIsGUI(boolean isGUI) {
        this.isGUI = isGUI;
    }

    /**
     * Prompt the user and return what they entered as a String.
     *
     * @param prompt the prompt shown to the user
     * @return the String the user entered
     * @author Kay Pitre
     * @since 20261012
     */
    public String getInputString(String prompt) {

        String entered;

        if (isGUI) {
            entered = JOptionPane.showInputDialog(prompt);
            //A cancelled dialog returns null, which would break every caller,
            //so treat it as an empty entry instead.
            if (entered == null) {
                entered = "";
            }
        } else {
            System.out.println(prompt);
            entered = input.nextLine();
        }

        return entered;
    }

    /**
     * Prompt the user until they enter a whole number.
     *
     * @param prompt the prompt shown to the user
     * @return the number the user entered
     * @author Kay Pitre
     * @since 20261012
     */
    public int getInputInt(String prompt) {

        while (true) {
            try {
                return Integer.parseInt(getInputString(prompt).trim());
            } catch (NumberFormatException e) {
                display("That is not a whole number.  Please try again.");
            }
        }
    }

    /**
     * Prompt the user until they enter a number.
     *
     * @param prompt the prompt shown to the user
     * @return the number the user entered
     * @author Kay Pitre
     * @since 20261012
     */
    public double getInputDouble(String prompt) {

        while (true) {
            try {
                return Double.parseDouble(getInputString(prompt).trim());
            } catch (NumberFormatException e) {
                display("That is not a number.  Please try again.");
            }
        }
    }

    /**
     * Show a message to the user.
     *
     * @param output the text to display
     * @author Kay Pitre
     * @since 20261012
     */
    public void display(String output) {

        if (isGUI) {
            JOptionPane.showMessageDialog(null, output);
        } else {
            System.out.println(output);
        }
    }

    /**
     * Format a number as Canadian currency.
     *
     * @param amount the amount to format
     * @return the amount formatted as currency
     * @author Kay Pitre
     * @since 20261012
     */
    public static String toCurrency(double amount) {
        return NumberFormat.getCurrencyInstance(Locale.CANADA).format(amount);
    }
}
