package ca.hccis.bookstore.controllers;

import org.springframework.ui.Model;
import ca.hccis.bookstore.repositories.CodeValueRepository;
import ca.hccis.bookstore.util.CisUtility;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Base controller which control general functionality in the app.
 *
 * @since 20220624
 * @author BJM
 */
@Controller
public class BaseController {

    private final CodeValueRepository _cvr;

    @Autowired
    public BaseController(CodeValueRepository cvr) {
        _cvr = cvr;
    }

    /**
     * Send the user to the welcome view
     *
     * @since 20220624
     * @author BJM
     */
    @RequestMapping("/")
    public String home(HttpSession session, Model model) {


        //BJM 20200602 Issue#1 Set the current date in the session
        String currentDate = CisUtility.getCurrentDate("yyyy-MM-dd");
        session.setAttribute("currentDate", currentDate);

        //KP 2026-10-24 #5 Removed the squash sample assessments list that fed the home page chart.

        return "index";
    }

    /**
     * Send the user to the about view.
     *
     * @since 20220624
     * @author BJM
     */
    @RequestMapping("/about")
    public String about() {
        return "other/about";
    }
}
