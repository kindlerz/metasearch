package ink.kindler.metasearch.service;

import ink.kindler.metasearch.persistent.entity.Book;
import java.util.List;

public interface BookService {

  void saveBook(Book book);

  void saveBooks(List<Book> books);
}
