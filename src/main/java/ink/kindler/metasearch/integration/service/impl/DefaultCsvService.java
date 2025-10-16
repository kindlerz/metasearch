package ink.kindler.metasearch.integration.service.impl;

import ink.kindler.metasearch.integration.service.CsvService;
import ink.kindler.metasearch.persistent.entity.Book;
import ink.kindler.metasearch.persistent.entity.BookSummary;
import ink.kindler.metasearch.persistent.entity.Provider;
import ink.kindler.metasearch.util.StringUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static ink.kindler.metasearch.integration.spec.GutenbergAustraliaCsvSpec.*;
import static ink.kindler.metasearch.util.StringUtil.isDesiredLength;

@Service
public class DefaultCsvService implements CsvService {
  private static final int MAX_LENGTH = 255;
  private static final Logger logger = LoggerFactory.getLogger(DefaultCsvService.class);

  public Stream<CSVRecord> streamCsvQuietly(Resource indexCsv) {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(indexCsv.getInputStream(), StandardCharsets.UTF_8))) {
      return CSVFormat.DEFAULT.builder()
          .setHeader()
          .setSkipHeaderRecord(true)
          .setIgnoreSurroundingSpaces(true)
          .setTrim(true)
          .get()
          .parse(reader)
          .getRecords()
          .stream()
          .filter(this::isValidRow);
    } catch (IOException e) {
      logger.error("Could not get Gutenberg Australia index content");
    }
    return Stream.empty();
  }

  public Book convertCsvRecord(CSVRecord csvRecord) {
    var book = new Book();
    book.setAuthor(StringUtil.removeAllQuote(csvRecord.get(AUTHOR)));
    book.setTitle(StringUtil.removeAllQuote(csvRecord.get(TITLE)));
    book.setHtmlUrl(csvRecord.get(HTML_URL));
    book.setCoverImageUrl(csvRecord.get(COVER_IMAGE_URL));
    book.setGoogleCoverImageUrl(csvRecord.get(GOOGLE_COVER_IMAGE_URL));
    book.setSummary(createBookSummary(book, csvRecord.get(SUMMARY)));
    book.setProvider(Provider.GUTENBERG_AUSTRALIA);
    return book;
  }

  private boolean isValidRow(CSVRecord csvRecord) {
    try {
      var isValid = isDesiredLength(csvRecord.get(TITLE), MAX_LENGTH) &&
          isDesiredLength(csvRecord.get(AUTHOR), MAX_LENGTH);
      logger.warn("CsvRecord {} is invalid. It will be skipped", csvRecord);
      return isValid;
    } catch (IllegalArgumentException csvParsingError) {
      logger.warn("Could not parse CsvRecord {}. It will be skipped", csvRecord);
    }
    return false;
  }

  private BookSummary createBookSummary(Book book, String summary) {
    var bookSummary = new BookSummary();
    bookSummary.setSummary(summary);
    bookSummary.setBook(book);
    return bookSummary;
  }
}
