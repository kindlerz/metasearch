package ink.kindler.metasearch.service.impl;

import ink.kindler.metasearch.persistent.entity.Book;
import ink.kindler.metasearch.persistent.entity.Provider;
import ink.kindler.metasearch.persistent.projection.BookOverview;
import ink.kindler.metasearch.service.BookService;
import ink.kindler.metasearch.service.SearchService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class DefaultSearchService implements SearchService {
  private final BookService bookService;

  public DefaultSearchService(BookService bookService) {
    this.bookService = bookService;
  }

  @Override
  public List<BookOverview> search(Provider provider, String query) {
    return bookService.searchBooksByProviderAndTitleOrAuthorMatching(provider, query);
  }

  @Override
  public Optional<Book> findBookById(Long id) {
    return bookService.getBookById(id);
  }
}
