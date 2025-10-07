package ink.kindler.metasearch.persistent.repository;

import ink.kindler.metasearch.persistent.entity.BookSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookSummaryRepository extends JpaRepository<BookSummary, Long> {
}
