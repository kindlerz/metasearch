package ink.kindler.metasearch.integration.service.impl;

import ink.kindler.metasearch.integration.configuration.GutenbergAustraliaProperties;
import ink.kindler.metasearch.integration.service.CsvService;
import ink.kindler.metasearch.integration.service.GutenbergAustraliaService;
import ink.kindler.metasearch.persistent.entity.Book;
import ink.kindler.metasearch.persistent.entity.Provider;
import ink.kindler.metasearch.service.BookService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultGutenbergAustraliaService implements GutenbergAustraliaService {
  private final BookService bookService;
  private final CsvService csvService;
  private final GutenbergAustraliaProperties properties;

  public DefaultGutenbergAustraliaService(BookService bookService, CsvService csvService,
                                          GutenbergAustraliaProperties properties) {
    this.bookService = bookService;
    this.csvService = csvService;
    this.properties = properties;
  }

  @Override
  public boolean shouldSkipIndexing() {
    if (!properties.isEnabled()) {
      return true;
    }
    var bookCount = bookService.countAvailableBooks(Provider.GUTENBERG_AUSTRALIA);
    return bookCount > 0 && !properties.isAlwaysReindex();
  }

  @Override
  public void refreshIndex(Resource indexCsv) {
    bookService.deleteAll(Provider.GUTENBERG_AUSTRALIA);
    bookService.saveBooks(getBooksFromCsvSource(indexCsv));
  }

  private List<Book> getBooksFromCsvSource(Resource indexCsv) {
    return csvService.streamCsvQuietly(indexCsv).parallel().map(csvService::convertCsvRecord).toList();
  }
}