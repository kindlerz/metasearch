package ink.kindler.metasearch.integration.scheduler;

import ink.kindler.metasearch.TestcontainersConfiguration;
import ink.kindler.metasearch.integration.service.StandardEbooksIntegration;
import ink.kindler.metasearch.integration.service.model.StandardEbooksBook;
import ink.kindler.metasearch.persistent.entity.Book;
import ink.kindler.metasearch.persistent.entity.BookSummary;
import ink.kindler.metasearch.persistent.entity.Provider;
import ink.kindler.metasearch.service.BookService;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {"spring.task.scheduling.enabled=false"})
@Import(TestcontainersConfiguration.class)
class StandardEbooksIndexerIntegrationTest {

  @Autowired
  private StandardEbooksIndexer standardEbooksIndexer;

  @MockitoBean
  private StandardEbooksIntegration standardEbooksIntegration;

  @MockitoSpyBean
  private BookService bookService;

  @BeforeEach
  void cleanDatabase() {
    bookService.deleteAll(Provider.STANDARD_EBOOKS);
    bookService.saveBooks(List.of(
        stubBook("The Adventures of Tom Sawyer", "Mark Twain"),
        stubBook("Nineteen Eighty-Four (1984)", "George Orwell"),
        stubBook("The Mark of Zorro", "Johnston McCulley")
    ));
  }

  @Test
  void shouldRewriteEntriesOfStandardEbooksLibrary() {
    when(standardEbooksIntegration.retrieveAllEbooksFromFeed()).thenReturn(stubStandardEbooksBooks());

    standardEbooksIndexer.indexEbooks();

    assertThat(bookService.countAvailableBooks(Provider.STANDARD_EBOOKS)).isEqualTo(2);
    assertThat(bookService.searchBooksByProviderAndTitleOrAuthorMatching(Provider.STANDARD_EBOOKS, "Invisible"))
        .hasSize(1);
    assertThat(bookService.searchBooksByProviderAndTitleOrAuthorMatching(Provider.STANDARD_EBOOKS, "1984"))
        .hasSize(0);
  }

  @Test
  void shouldRollbackDatabaseModificationOnException() {
    when(standardEbooksIntegration.retrieveAllEbooksFromFeed()).thenReturn(stubStandardEbooksBooks());
    doThrow(new PersistenceException("Unable to save")).when(bookService).saveBooks(anyList());

    assertThatThrownBy(() -> standardEbooksIndexer.indexEbooks());

    assertThat(bookService.countAvailableBooks(Provider.STANDARD_EBOOKS)).isEqualTo(3);
    assertThat(bookService.searchBooksByProviderAndTitleOrAuthorMatching(Provider.STANDARD_EBOOKS, "1984"))
        .hasSize(1);
    assertThat(bookService.searchBooksByProviderAndTitleOrAuthorMatching(Provider.STANDARD_EBOOKS, "Invisible"))
        .hasSize(0);
  }

  private Book stubBook(String title, String author) {
    var book = new Book();
    book.setTitle(title);
    book.setAuthor(author);
    book.setSummary(stubBookSummary(book));
    book.setCoverImageUrl("https://coverimageurl.com");
    book.setEpubUrl("https://epuburl.com");
    book.setKoboUrl("https://kobourl.com");
    book.setMobiUrl("https://mobiurl.com");
    book.setAzwUrl("https://azwurl.com");
    book.setHtmlUrl("https://htmlurl.com");
    book.setProvider(Provider.STANDARD_EBOOKS);
    return book;
  }

  private BookSummary stubBookSummary(Book book) {
    var bookSummary = new BookSummary();
    bookSummary.setSummary("Test summary");
    bookSummary.setBook(book);
    return bookSummary;
  }

  private List<StandardEbooksBook> stubStandardEbooksBooks() {
    return List.of(new StandardEbooksBook(
            "The Invisible Man",
            "H. G. Wells",
            "https://standardebooks.org/ebooks/h-g-wells/the-invisible-man/downloads/cover-thumbnail.jpg",
            "Griffin, a scientist...",
            "https://standardebooks.org/ebooks/h-g-wells/the-invisible-man/downloads/h-g-wells_the-invisible-man.epub?source=feed",
            "https://standardebooks.org/ebooks/h-g-wells/the-invisible-man/downloads/h-g-wells_the-invisible-man.kepub.epub?source=feed",
            "https://standardebooks.org/ebooks/h-g-wells/the-invisible-man/downloads/h-g-wells_the-invisible-man.azw3?source=feed",
            "https://standardebooks.org/ebooks/h-g-wells/the-invisible-man/text/single-page",
            Instant.parse("2025-10-08T21:56:51Z")),
        new StandardEbooksBook(
            "Recollections of Full Years",
            "Helen Herron Taft",
            "https://standardebooks.org/ebooks/helen-herron-taft/recollections-of-full-years/downloads/cover-thumbnail.jpg",
            "Helen Herron Taft served as First Lady...",
            "https://standardebooks.org/ebooks/helen-herron-taft/recollections-of-full-years/downloads/helen-herron-taft_recollections-of-full-years.epub?source=feed",
            "https://standardebooks.org/ebooks/helen-herron-taft/recollections-of-full-years/downloads/helen-herron-taft_recollections-of-full-years.kepub.epub?source=feed",
            "https://standardebooks.org/ebooks/helen-herron-taft/recollections-of-full-years/downloads/helen-herron-taft_recollections-of-full-years.azw3?source=feed",
            "https://standardebooks.org/ebooks/helen-herron-taft/recollections-of-full-years/text/single-page",
            Instant.parse("2025-10-08T02:04:59Z"))
    );
  }
}