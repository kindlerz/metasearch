package ink.kindler.metasearch.persistent.repository;

import ink.kindler.metasearch.persistent.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
