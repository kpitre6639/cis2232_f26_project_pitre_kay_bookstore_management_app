package ca.hccis.util;

import java.text.NumberFormat;
import java.util.Scanner;

/**
 * Utility methods used to gather validated input from the user at the console.
 *
 * A single Scanner is shared by the whole application.  Creating more than one
 * Scanner on System.in causes input to be lost between them.
 *
 * @author Kay Pitre
 * @since 20260923
 */
public class CisUtility {

    private static final Scanner INPUT = new Scanner(System.in);

    /**
     * Prompt the user and return the text they enter.
     *
     * @param prompt message shown to the user
     * @return the text entered, trimmed
     */
    public static String getInputString(String prompt) {
        System.out.print(prompt + " --> ");
        return INPUT.nextLine().trim();
    }

    /**
     * Prompt the user for a whole number and keep asking until one is entered.
     *
     * @param prompt message shown to the user
     * @return the int entered by the user
     */
    public static int getInputInt(String prompt) {
        while (true) {
            String entered = getInputString(prompt);
            try {
                return Integer.parseInt(entered);
            } catch (NumberFormatException e) {
                System.out.println("That is not a whole number.  Please try again.");
            }
        }
    }

    /**
     * Prompt the user for a decimal number and keep asking until one is entered.
     *
     * @param prompt message shown to the user
     * @return the double entered by the user
     */
    public static double getInputDouble(String prompt) {
        while (true) {
            String entered = getInputString(prompt);
            try {
                return Double.parseDouble(entered);
            } catch (NumberFormatException e) {
                System.out.println("That is not a number.  Please try again.");
            }
        }
    }

    /**
     * Format a double as currency using the default locale.
     *
     * @param amount value to format
     * @return the amount as a currency String
     */
    public static String toCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(amount);
    }
}
