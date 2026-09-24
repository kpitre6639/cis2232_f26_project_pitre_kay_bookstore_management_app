package ca.hccis.bookstore.repositories;

import java.util.List;

import ca.hccis.bookstore.jpa.entity.CodeValue;
import ca.hccis.bookstore.jpa.entity.CodeValueId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeValueRepository extends CrudRepository<CodeValue, CodeValueId> {
}