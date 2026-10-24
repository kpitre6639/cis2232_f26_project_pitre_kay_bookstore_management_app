package ca.hccis.bookstore.entity;

/**
 * Entity class to hold the calculated figures for the income report.
 *
 * One of these is built per report run.  The monthly totals feed the chart;
 * the four summary figures are the report requirement specified by the BA.
 *
 * @author KP
 * @since 2026-10-24
 */
public class IncomeSummary {

    public static final int MONTHS_IN_YEAR = 12;
    public static final int DAYS_IN_YEAR = 365;

    private String year;
    private int recordCount;

    //The four figures the report is required to show.
    private double totalIncome;
    private double averagePerDay;
    private double averagePerMonth;
    private double averagePerYear;

    //Income for each month of the selected year, index 0 = January.
    private double[] incomeByMonth = new double[MONTHS_IN_YEAR];

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getAveragePerDay() {
        return averagePerDay;
    }

    public void setAveragePerDay(double averagePerDay) {
        this.averagePerDay = averagePerDay;
    }

    public double getAveragePerMonth() {
        return averagePerMonth;
    }

    public void setAveragePerMonth(double averagePerMonth) {
        this.averagePerMonth = averagePerMonth;
    }

    public double getAveragePerYear() {
        return averagePerYear;
    }

    public void setAveragePerYear(double averagePerYear) {
        this.averagePerYear = averagePerYear;
    }

    public double[] getIncomeByMonth() {
        return incomeByMonth;
    }

    public void setIncomeByMonth(double[] incomeByMonth) {
        this.incomeByMonth = incomeByMonth;
    }

    @Override
    public String toString() {
        return String.format(
                "Year %s: %d sales records, total income $%,.2f, "
                        + "average per day $%,.2f, average per month $%,.2f, "
                        + "average per year (all years) $%,.2f",
                year, recordCount, totalIncome, averagePerDay, averagePerMonth, averagePerYear);
    }
}
