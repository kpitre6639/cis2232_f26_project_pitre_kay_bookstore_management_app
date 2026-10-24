package ca.hccis.bookstore.bo;

import ca.hccis.bookstore.dao.BookDAO;
import ca.hccis.bookstore.entity.IncomeSummary;
import ca.hccis.bookstore.entity.ReportInput;
import ca.hccis.bookstore.jpa.entity.Book;
import ca.hccis.bookstore.util.CisUtilityFile;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Business logic for the income report.
 *
 * The controller hands in a ReportInput holding the year and genre the user
 * chose.  This class gets the matching records through the DAO (JDBC), works
 * out the income figures specified by the BA, writes the report to a file,
 * and hands the loaded ReportInput back for the view.
 *
 * @author KP
 * @since 2026-10-24
 */
public class ReportBO {

    public static final String REPORT_FILE_NAME = "IncomeReport";

    /**
     * Run the income report for the year and genre in the report input.
     *
     * @param reportInput the user's choices; results are loaded onto it
     * @author KP
     * @since 2026-10-24
     */
    public static void processIncomeReport(ReportInput reportInput) {

        BookDAO bookDAO = new BookDAO();

        //**********************************************************************
        // Get the records for the selected year through JDBC.  The all-years
        // average needs every record on file, so that is a second query.
        //**********************************************************************
        ArrayList<Book> booksForYear = bookDAO.selectAllByYear(reportInput.getYear(), reportInput.getGenre());
        ArrayList<Book> allBooks = bookDAO.selectAll();
        bookDAO.close();

        IncomeSummary summary = new IncomeSummary();
        summary.setYear(reportInput.getYear());
        summary.setRecordCount(booksForYear.size());

        //Total the income for the selected year and bucket it by month for the chart.
        double totalForYear = 0;
        double[] incomeByMonth = new double[IncomeSummary.MONTHS_IN_YEAR];

        for (Book current : booksForYear) {

            double income = BookBO.calculate(current);

            //A record whose cost cannot be determined is left out rather than
            //allowed to poison the totals.
            if (income == BookBO.NO_COST) {
                continue;
            }

            totalForYear += income;

            //dateSold is yyyy-MM-dd, so the month is characters 5 and 6.
            int monthIndex = getMonthIndex(current.getDateSold());
            if (monthIndex >= 0) {
                incomeByMonth[monthIndex] += income;
            }
        }

        summary.setTotalIncome(totalForYear);
        summary.setIncomeByMonth(incomeByMonth);

        //**********************************************************************
        // The BA's rules: average per day is the year's total over 365, average
        // per month is the year's total over 12, and average per year is the
        // total of all years over the number of distinct years on record.
        //**********************************************************************
        summary.setAveragePerDay(totalForYear / IncomeSummary.DAYS_IN_YEAR);
        summary.setAveragePerMonth(totalForYear / IncomeSummary.MONTHS_IN_YEAR);
        summary.setAveragePerYear(calculateAveragePerYear(allBooks, reportInput.getGenre()));

        reportInput.setBooks(booksForYear);
        reportInput.setIncomeSummary(summary);

        //Also write the report to a file, as the sprint requires.
        writeReportFile(reportInput);
    }

    /**
     * Average income per year across every year on record.
     *
     * @param allBooks every sales record on file
     * @param genre    genre to limit to, or null/empty for all
     * @return total of all years divided by the number of distinct years, 0 if none
     * @author KP
     * @since 2026-10-24
     */
    public static double calculateAveragePerYear(ArrayList<Book> allBooks, String genre) {

        boolean filterGenre = genre != null && !genre.isEmpty();
        double totalAllYears = 0;
        HashSet<String> years = new HashSet<>();

        for (Book current : allBooks) {

            if (filterGenre && !genre.equals(current.getGenre())) {
                continue;
            }

            double income = BookBO.calculate(current);
            if (income == BookBO.NO_COST) {
                continue;
            }

            String year = getYear(current.getDateSold());
            if (year == null) {
                continue;
            }

            totalAllYears += income;
            years.add(year);
        }

        if (years.isEmpty()) {
            return 0;
        }

        return totalAllYears / years.size();
    }

    /**
     * Pull the year out of a yyyy-MM-dd date string.
     *
     * @param dateSold the date as stored
     * @return the yyyy part, or null if the string is too short
     * @author KP
     * @since 2026-10-24
     */
    public static String getYear(String dateSold) {
        if (dateSold == null || dateSold.length() < 4) {
            return null;
        }
        return dateSold.substring(0, 4);
    }

    /**
     * Pull the month out of a yyyy-MM-dd date string as a zero-based index.
     *
     * @param dateSold the date as stored
     * @return 0 for January through 11 for December, or -1 if it cannot be read
     * @author KP
     * @since 2026-10-24
     */
    public static int getMonthIndex(String dateSold) {
        if (dateSold == null || dateSold.length() < 7) {
            return -1;
        }
        try {
            int month = Integer.parseInt(dateSold.substring(5, 7));
            if (month < 1 || month > IncomeSummary.MONTHS_IN_YEAR) {
                return -1;
            }
            return month - 1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Write the report to c:\cis2232\IncomeReport<date time>.txt - one line per
     * record, then the summary figures.
     *
     * @param reportInput the completed report
     * @author KP
     * @since 2026-10-24
     */
    private static void writeReportFile(ReportInput reportInput) {

        ArrayList<Object> lines = new ArrayList<>();
        lines.add(reportInput.toString());
        lines.add("");
        lines.addAll(reportInput.getBooks());
        lines.add("");
        lines.add(reportInput.getIncomeSummary().toString());

        CisUtilityFile.writeReportToFile(REPORT_FILE_NAME, lines);
    }
}
