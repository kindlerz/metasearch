package ink.kindler.metasearch.service;

import ink.kindler.metasearch.persistent.entity.Book;
import ink.kindler.metasearch.persistent.entity.Provider;
import ink.kindler.metasearch.persistent.projection.BookOverview;
import java.util.List;
import java.util.Optional;

public interface BookService {

  void saveBook(Book book);

  void saveBooks(List<Book> books);

  List<BookOverview> searchBooksByProviderAndTitleOrAuthorMatching(Provider provider, String query);

  Optional<Book> getBookById(Long id);
}
