package ca.hccis.bookstore.controllers;

import ca.hccis.bookstore.bo.ReportBO;
import ca.hccis.bookstore.dao.BookDAO;
import ca.hccis.bookstore.entity.ReportInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;

/**
 * Controller to administer reports of the project.
 *
 * @author BJM
 * @since 20251009
 * @modifiedby KP 2026-10-24 #5 Replaced the squash sample reports with the bookstore income report.
 */
@Controller
@RequestMapping("/report")
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    /**
     * Send the user to list of reports view.
     *
     * @param model
     * @param session
     * @return To the appropriate view
     * @author BJM
     * @since 20251009
     */
    @RequestMapping("")
    public String home(Model model, HttpSession session) {

        logger.info("Running the reports controller base method");
        return "report/list";
    }

    /**
     * Send the user to the income report input view.
     *
     * The year and genre drop-downs are loaded from the database through the
     * DAO and kept in the session so they survive the form post back to the
     * same page.
     *
     * @param model
     * @param session
     * @return the income report view
     * @author KP
     * @since 2026-10-24
     */
    @RequestMapping("/income")
    public String reportIncome(Model model, HttpSession session) {

        logger.info("Running the reports controller income method");

        //Load the drop-down choices from the database.
        BookDAO bookDAO = new BookDAO();
        ArrayList<String> years = bookDAO.selectDistinctYears();
        ArrayList<String> genres = bookDAO.selectDistinctGenres();
        bookDAO.close();

        session.setAttribute("years", years);
        session.setAttribute("genres", genres);

        model.addAttribute("reportInput", new ReportInput());
        return "report/reportIncome";
    }

    /**
     * Process the income report.
     *
     * @param model
     * @param reportInput Object containing the user's inputs for the report
     * @return the income report view, now with results
     * @author KP
     * @since 2026-10-24
     */
    @RequestMapping("/income/submit")
    public String reportIncomeSubmit(Model model, @ModelAttribute("reportInput") ReportInput reportInput) {

        logger.info("Running the income report for year " + reportInput.getYear()
                + " genre " + reportInput.getGenre());

        //All of the model work - database, calculation, file - happens in the BO.
        ReportBO.processIncomeReport(reportInput);

        //Add a message in case the report does not contain any data.
        if (reportInput.getBooks() == null || reportInput.getBooks().isEmpty()) {
            model.addAttribute("message", "No sales found for that year and genre");
        }

        //Put the results in the model so they can be used on the view.
        model.addAttribute("reportInput", reportInput);
        model.addAttribute("incomeSummary", reportInput.getIncomeSummary());

        return "report/reportIncome";
    }

}
