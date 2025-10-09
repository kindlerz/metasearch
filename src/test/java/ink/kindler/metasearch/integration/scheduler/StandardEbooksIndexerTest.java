package ink.kindler.metasearch.integration.scheduler;

import ink.kindler.metasearch.integration.service.StandardEbooksIntegration;
import ink.kindler.metasearch.integration.service.model.StandardEbooksBook;
import ink.kindler.metasearch.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StandardEbooksIndexerTest {

  @InjectMocks
  private StandardEbooksIndexer standardEbooksIndexer;

  @Mock
  private StandardEbooksIntegration standardEbooksIntegration;

  @Mock
  private BookService bookService;

  @Test
  void shouldIndexEbooksOfStandardEbooksLibrary() {
    when(standardEbooksIntegration.retrieveAllEbooksFromFeed()).thenReturn(stubStandardEbooksBooks());

    standardEbooksIndexer.indexEbooks();

    verify(bookService).saveBooks(anyList());
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