package ink.kindler.metasearch.service.impl;

import ink.kindler.metasearch.persistent.entity.Book;
import ink.kindler.metasearch.persistent.entity.Provider;
import ink.kindler.metasearch.persistent.projection.BookOverview;
import ink.kindler.metasearch.persistent.repository.BookRepository;
import ink.kindler.metasearch.service.BookService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultBookService implements BookService {
  private final BookRepository bookRepository;

  public DefaultBookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Override
  public void saveBook(Book book) {
    bookRepository.save(book);
  }

  @Override
  public void saveBooks(List<Book> books) {
    bookRepository.saveAll(books);
  }

  @Override
  public List<BookOverview> searchBooksByProviderAndTitleOrAuthorMatching(Provider provider, String query) {
    return bookRepository.searchBooks(provider, query, PageRequest.of(0, 50));
  }

  @Override
  public Optional<Book> getBookById(Long id) {
    return bookRepository.findById(id);
  }
}
