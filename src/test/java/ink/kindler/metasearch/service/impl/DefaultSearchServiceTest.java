package ink.kindler.metasearch.service.impl;

import ink.kindler.metasearch.persistent.entity.Book;
import ink.kindler.metasearch.persistent.entity.Provider;
import ink.kindler.metasearch.persistent.projection.BookOverview;
import ink.kindler.metasearch.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultSearchServiceTest {

  @InjectMocks
  private DefaultSearchService searchService;

  @Mock
  private BookService bookService;

  @Test
  void shouldSearchBooksByProviderAndAuthorOrTitle() {
    var bookOverViewOne = mock(BookOverview.class);
    var bookOverViewTwo = mock(BookOverview.class);

    when(bookService.searchBooksByProviderAndTitleOrAuthorMatching(Provider.STANDARD_EBOOKS, "Mark"))
        .thenReturn(List.of(bookOverViewOne, bookOverViewTwo));

    var searchResult = searchService.search(Provider.STANDARD_EBOOKS, "Mark");

    assertThat(searchResult).isEqualTo(List.of(bookOverViewOne, bookOverViewTwo));
  }

  @Test
  void shouldFindBookById() {
    var book = Optional.of(mock(Book.class));
    when(bookService.getBookById(1L)).thenReturn(book);

    var retrievedBook = searchService.findBookById(1L);

    assertThat(retrievedBook).isEqualTo(book);
  }
}