package ca.hccis.bookstore.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

/**
 * Controller to administer reports of the project.
 *
 * @author BJM
 * @since 20251009
 * @modifiedby KP 2026-10-24 #5 Removed the squash sample reports; the income report is added in Sprint 2.
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

}
