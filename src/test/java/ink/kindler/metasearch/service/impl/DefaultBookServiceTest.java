package ink.kindler.metasearch.service.impl;

import ink.kindler.metasearch.persistent.entity.Book;
import ink.kindler.metasearch.persistent.entity.Provider;
import ink.kindler.metasearch.persistent.projection.BookOverview;
import ink.kindler.metasearch.persistent.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultBookServiceTest {

  @InjectMocks
  private DefaultBookService bookService;

  @Mock
  private BookRepository bookRepository;

  @Test
  void shouldPersistBook() {
    var book = mock(Book.class);

    bookService.saveBook(book);

    verify(bookRepository).save(book);
  }

  @Test
  void shouldPersistBooks() {
    var books = List.of(mock(Book.class), mock(Book.class));

    bookService.saveBooks(books);

    verify(bookRepository).saveAll(books);
  }

  @Test
  void shouldSearchBooksByProviderAndAuthorOrTitle() {
    var foundBooks = List.of(mock(BookOverview.class), mock(BookOverview.class));

    when(bookRepository.searchBooks(Provider.STANDARD_EBOOKS, "Mark", PageRequest.of(0, 50)))
        .thenReturn(foundBooks);

    var result = bookService.searchBooksByProviderAndTitleOrAuthorMatching(Provider.STANDARD_EBOOKS, "Mark");

    assertThat(result).isEqualTo(foundBooks);
  }

  @Test
  void shouldFindBookById() {
    var foundBook = Optional.of(mock(Book.class));

    when(bookRepository.findById(1L)).thenReturn(foundBook);

    var result = bookService.getBookById(1L);

    assertThat(result).isEqualTo(foundBook);
  }

}