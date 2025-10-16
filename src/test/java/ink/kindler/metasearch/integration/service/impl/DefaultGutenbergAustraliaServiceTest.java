package ink.kindler.metasearch.integration.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import ink.kindler.metasearch.integration.configuration.GutenbergAustraliaProperties;
import ink.kindler.metasearch.persistent.entity.Provider;
import ink.kindler.metasearch.service.BookService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultGutenbergAustraliaServiceTest {

  @InjectMocks
  private DefaultGutenbergAustraliaService gutenbergAustraliaService;

  @Mock
  private BookService bookService;

  @Mock
  private GutenbergAustraliaProperties gutenbergAustraliaProperties;

  @ParameterizedTest
  @CsvSource({"10,false,true", "0,false,false", "10,true,false"})
  void shouldSkipIndex(int availableBooks, boolean isAlwaysReindex, boolean expected) {
    when(bookService.countAvailableBooks(Provider.GUTENBERG_AUSTRALIA)).thenReturn(availableBooks);
    lenient().when(gutenbergAustraliaProperties.isAlwaysReindex()).thenReturn(isAlwaysReindex);

    var shouldSkipIndexing = gutenbergAustraliaService.shouldSkipIndexing();

    assertThat(shouldSkipIndexing).isEqualTo(expected);
  }
}