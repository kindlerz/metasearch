package ink.kindler.metasearch.integration.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import ink.kindler.metasearch.integration.configuration.GutenbergAustraliaProperties;
import ink.kindler.metasearch.integration.service.CsvService;
import ink.kindler.metasearch.persistent.entity.Book;
import ink.kindler.metasearch.persistent.entity.Provider;
import ink.kindler.metasearch.service.BookService;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class DefaultGutenbergAustraliaServiceTest {

  @InjectMocks
  private DefaultGutenbergAustraliaService gutenbergAustraliaService;

  @Mock
  private BookService bookService;

  @Mock
  private GutenbergAustraliaProperties gutenbergAustraliaProperties;

  @Mock
  private CsvService csvService;

  @Captor
  ArgumentCaptor<List<Book>> bookCaptor;

  @ParameterizedTest
  @CsvSource({"10,false,true,true", "0,false,true,false", "10,true,true,false", "10,true,true,false", "10,true,false,true"})
  void shouldSkipIndex(int availableBooks, boolean isAlwaysReindex, boolean isEnabled, boolean expected) {
    when(gutenbergAustraliaProperties.isEnabled()).thenReturn(isEnabled);
    lenient().when(bookService.countAvailableBooks(Provider.GUTENBERG_AUSTRALIA)).thenReturn(availableBooks);
    lenient().when(gutenbergAustraliaProperties.isAlwaysReindex()).thenReturn(isAlwaysReindex);

    var shouldSkipIndexing = gutenbergAustraliaService.shouldSkipIndexing();

    assertThat(shouldSkipIndexing).isEqualTo(expected);
  }

  @Test
  void shouldRefreshGutenbergAustraliaIndex() {
    var bookOne = mock(Book.class);
    var bookTwo = mock(Book.class);
    var resource = mock(Resource.class);
    when(csvService.streamCsvQuietly(resource)).thenReturn(Stream.of(mock(CSVRecord.class), mock(CSVRecord.class)));
    when(csvService.convertCsvRecord(any(CSVRecord.class))).thenReturn(bookOne, bookTwo);

    gutenbergAustraliaService.refreshIndex(resource);

    verify(bookService).deleteAll(Provider.GUTENBERG_AUSTRALIA);
    verify(bookService).saveBooks(bookCaptor.capture());
    assertThat(bookCaptor.getValue()).containsExactlyInAnyOrder(bookOne, bookTwo);
  }
}