package ca.hccis.bookstore.repositories;

import ca.hccis.bookstore.jpa.entity.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

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
}
