package ink.kindler.metasearch.service;

import ink.kindler.metasearch.persistent.entity.Book;
import ink.kindler.metasearch.persistent.entity.Provider;
import ink.kindler.metasearch.persistent.projection.BookOverview;
import java.util.List;
import java.util.Optional;

public interface SearchService {
  List<BookOverview> search(Provider provider, String query);

  Optional<Book> findBookById(Long id);
}
