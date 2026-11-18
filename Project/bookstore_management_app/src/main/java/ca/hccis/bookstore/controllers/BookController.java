package ca.hccis.bookstore.controllers;

import ca.hccis.bookstore.bo.BookBO;
import ca.hccis.bookstore.jpa.entity.Book;
import ca.hccis.bookstore.repositories.BookRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * CRUD controller for Book records using Spring Data JPA and Spring Validation.
 *
 * Sprint 3.  Follows the class example: the repository arrives by constructor
 * injection, one form serves both add and edit, and the submit mapping runs
 * the validation annotations on the entity before anything is saved.
 *
 * @author KP
 * @since 2026-11-18
 */
@Controller
@RequestMapping("/book")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private static final String VIEW_LIST = "book/list";
    private static final String VIEW_ADD = "book/add";
    private static final String REDIRECT_LIST = "redirect:/book";

    private static final String ATTRIBUTE_BOOKS = "books";
    private static final String ATTRIBUTE_BOOK = "book";
    private static final String ATTRIBUTE_MESSAGE_SUCCESS = "messageSuccessKey";
    private static final String ATTRIBUTE_MESSAGE_ERROR = "messageErrorKey";

    private final BookRepository bookRepository;

    /**
     * Constructor injection of the repository, as shown in class.
     *
     * @param bookRepository the Spring Data repository for Book
     * @author KP
     * @since 2026-11-18
     */
    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * List every book record.
     *
     * @param model the model for the view
     * @return the list view
     * @author KP
     * @since 2026-11-18
     */
    @RequestMapping("")
    public String list(Model model) {
        loadList(model);
        return VIEW_LIST;
    }

    /**
     * Show an empty form to add a record.
     *
     * createdDateTime is set here rather than typed by the user, and the form
     * carries it as a hidden field so it survives the POST.
     *
     * @param model the model for the view
     * @return the add/edit view
     * @author KP
     * @since 2026-11-18
     */
    @RequestMapping("/add")
    public String add(Model model) {
        Book book = new Book();
        book.setCreatedDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        model.addAttribute(ATTRIBUTE_BOOK, book);
        return VIEW_ADD;
    }

    /**
     * Show the form loaded with an existing record to edit.
     *
     * @param id    the id of the record to edit
     * @param model the model for the view
     * @return the add/edit view, or the list with an error if the id is unknown
     * @author KP
     * @since 2026-11-18
     */
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {

        Optional<Book> book = bookRepository.findById(id);

        if (book.isPresent()) {
            model.addAttribute(ATTRIBUTE_BOOK, book.get());
            return VIEW_ADD;
        }

        //The id in the URL does not exist - go back to the list and say so.
        logger.warn("Edit requested for book id {} which does not exist", id);
        model.addAttribute(ATTRIBUTE_MESSAGE_ERROR, "error.message.book.not.found");
        loadList(model);
        return VIEW_LIST;
    }

    /**
     * Handle the form submission for both add and edit.
     *
     * The @Valid annotation runs every constraint on the Book entity and the
     * results land in bindingResult.  If anything failed, the form is shown
     * again with the messages beside the fields; nothing is saved.
     *
     * @param book          the record from the form, populated by Spring
     * @param bindingResult the outcome of validating it
     * @param model         the model for the view
     * @return redirect to the list on success, the form again on error
     * @author KP
     * @since 2026-11-18
     */
    @RequestMapping("/submit")
    public String submit(@Valid @ModelAttribute(ATTRIBUTE_BOOK) Book book,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            //Log what failed so it can be seen in the console during marking,
            //then send the user back to the form with the messages showing.
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.info("Validation failed: {}", error.getDefaultMessage());
            }
            model.addAttribute(ATTRIBUTE_BOOK, book);
            return VIEW_ADD;
        }

        //A record edited through the form keeps its original createdDateTime
        //because the hidden field carried it.  A brand new one has it from add().
        bookRepository.save(book);
        logger.info("Saved book {} - cost of this record is {}", book.getId(), BookBO.calculate(book));

        return REDIRECT_LIST;
    }

    /**
     * Delete a record and return to the list.
     *
     * @param id    the id of the record to delete
     * @param model the model for the view
     * @return the list view with a success message
     * @author KP
     * @since 2026-11-18
     */
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model) {

        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            logger.info("Deleted book {}", id);
            model.addAttribute(ATTRIBUTE_MESSAGE_SUCCESS, "message.book.deleted");
        } else {
            logger.warn("Delete requested for book id {} which does not exist", id);
            model.addAttribute(ATTRIBUTE_MESSAGE_ERROR, "error.message.book.not.found");
        }

        loadList(model);
        return VIEW_LIST;
    }

    /**
     * Put every record on the model for the list view.
     *
     * @param model the model for the view
     * @author KP
     * @since 2026-11-18
     */
    private void loadList(Model model) {
        Iterable<Book> books = bookRepository.findAll();
        model.addAttribute(ATTRIBUTE_BOOKS, books);
    }
}
