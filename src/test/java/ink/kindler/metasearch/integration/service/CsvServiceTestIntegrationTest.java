package ink.kindler.metasearch.integration.service;

import ink.kindler.metasearch.TestcontainersConfiguration;
import ink.kindler.metasearch.integration.service.impl.DefaultCsvService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CsvServiceTestIntegrationTest {

  @Value("classpath:gutenberg-australia/index_with_summary.csv")
  private Resource indexCsv;

  @Autowired
  private DefaultCsvService csvService;

  @Test
  void shouldStreamVerifiedRowsOfIndexCsvFile() {
    var csvRecords = csvService.streamCsvQuietly(indexCsv).toList();

    assertThat(csvRecords.size()).isEqualTo(1);
    assertThat(csvRecords.getFirst().get("title")).isEqualTo("Sapiens: A Brief History of Humankind");
  }

  @Test
  void shouldReturnEmptyStreamOnException() throws IOException {
    var resource = mock(Resource.class);
    when(resource.getInputStream()).thenThrow(new IOException("Failed to open the resource!"));

    var stream = csvService.streamCsvQuietly(resource);

    assertThat(stream).isEmpty();
  }

  @Test
  void shouldConvertCsvRecordToBook() {
    var csvRecord = csvService.streamCsvQuietly(indexCsv).toList().getFirst();

    var book = csvService.convertCsvRecord(csvRecord);

    assertThat(book.getTitle()).isEqualTo("Sapiens: A Brief History of Humankind");
    assertThat(book.getAuthor()).isEqualTo("Yuval Noah Harari");
    assertThat(book.getHtmlUrl()).isEqualTo("https://library.xyz/books/1");
    assertThat(book.getCoverImageUrl()).isEqualTo("https://www.ynharari.com/book/sapiens-2/");
    assertThat(book.getGoogleCoverImageUrl()).isEqualTo("https://books.google.com/homo-spaiens.jpg");
    assertThat(book.getSummary().getSummary()).isEqualTo("Summary of Homo sapiens book");
  }
}