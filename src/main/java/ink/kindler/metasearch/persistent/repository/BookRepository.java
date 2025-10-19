package ink.kindler.metasearch.persistent.repository;

import ink.kindler.metasearch.persistent.entity.Book;
import ink.kindler.metasearch.persistent.entity.Provider;
import ink.kindler.metasearch.persistent.projection.BookOverview;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

  @Query("""
        SELECT b.id AS id,
            b.title AS title,
            b.author AS author,
            b.coverImageUrl AS coverImageUrl,
            b.googleCoverImageUrl As googleCoverImageUrl
        FROM Book b
        WHERE b.provider = :provider
          AND (LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')))
        ORDER BY b.author ASC, b.title ASC
      """)
  List<BookOverview> searchBooks(@Param("provider") Provider provider, @Param("query") String query, Pageable pageable);

  void deleteAllByProvider(Provider provider);

  int countByProvider(Provider provider);
}
