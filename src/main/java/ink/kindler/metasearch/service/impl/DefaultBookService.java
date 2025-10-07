package ink.kindler.metasearch.service.impl;

import ink.kindler.metasearch.persistent.entity.Book;
import ink.kindler.metasearch.persistent.repository.BookRepository;
import ink.kindler.metasearch.persistent.repository.BookSummaryRepository;
import ink.kindler.metasearch.service.BookService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DefaultBookService implements BookService {
  private final BookRepository bookRepository;
  private final BookSummaryRepository bookSummaryRepository;

  public DefaultBookService(BookRepository bookRepository, BookSummaryRepository bookSummaryRepository) {
    this.bookRepository = bookRepository;
    this.bookSummaryRepository = bookSummaryRepository;
  }

  @Override
  public void saveBook(Book book) {
    bookRepository.save(book);
  }

  @Override
  public void saveBooks(List<Book> books) {
    bookRepository.saveAll(books);
  }
}
