package ca.hccis.bookstore.repositories;

import ca.hccis.bookstore.jpa.entity.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for Book.
 *
 * No body - CrudRepository supplies findAll, findById, save, saveAll,
 * deleteById and the rest.  Spring generates the implementation at startup.
 *
 * @author KP
 * @since 2026-11-18
 */
@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {

    /**
     * Find every book whose title or author contains the text supplied.
     *
     * This is a derived query method - there is no body and no SQL.  Spring
     * builds the query from the method name at startup, so the attribute
     * names here (bookName, author) must match the Book entity exactly.
     *
     * @param bookName text to look for in the title
     * @param author   text to look for in the author
     * @return the books that matched, empty if none did
     * @author KP
     * @since 2026-11-18
     */
    List<Book> findByBookNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(String bookName, String author);
}
