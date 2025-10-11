package ink.kindler.metasearch.rest;

import ink.kindler.metasearch.TestcontainersConfiguration;
import ink.kindler.metasearch.persistent.entity.Book;
import ink.kindler.metasearch.persistent.entity.BookSummary;
import ink.kindler.metasearch.persistent.entity.Provider;
import ink.kindler.metasearch.persistent.repository.BookRepository;
import ink.kindler.metasearch.rest.model.BookOverviewResponse;
import ink.kindler.metasearch.rest.model.BookResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchControllerIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private BookRepository bookRepository;

  @BeforeEach
  void setUpDatabase() {
    bookRepository.deleteAll();
    bookRepository.saveAll(List.of(
        stubBook("The Adventures of Tom Sawyer", "Mark Twain", Provider.STANDARD_EBOOKS),
        stubBook("Adventures of Huckleberry Finn", "Mark Twain", Provider.GUTENBERG),
        stubBook("Nineteen Eighty-Four (1984)", "George Orwell", Provider.STANDARD_EBOOKS),
        stubBook("The Mark of Zorro", "Johnston McCulley", Provider.STANDARD_EBOOKS)
    ));
  }

  @Test
  void shouldSearchBook() {
    var endpoint = "http://localhost:%s/v1/books/search?q=mark&provider=STANDARD_EBOOK".formatted(port);
    var expectedResponse = List.of(
        new BookOverviewResponse(4L, "The Mark of Zorro", "Johnston McCulley", "https://coverimageurl.com"),
        new BookOverviewResponse(1L, "The Adventures of Tom Sawyer", "Mark Twain", "https://coverimageurl.com")
    );

    var responseEntity = restTemplate.exchange(endpoint, HttpMethod.GET, null, new ParameterizedTypeReference<List<BookOverviewResponse>>() {
    });

    assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(responseEntity.hasBody()).isTrue();
    var foundBooks = responseEntity.getBody();

    assertThat(foundBooks)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(expectedResponse);
  }

  @Test
  void shouldRetrieveBookById() {
    var endpoint = "http://localhost:%s/v1/books/1".formatted(port);
    var expectedResponse = new BookResponse(
        1L,
        "The Adventures of Tom Sawyer",
        "Mark Twain",
        "https://coverimageurl.com",
        "https://epuburl.com",
        "https://kobourl.com",
        "https://mobiurl.com",
        "https://azwurl.com",
        "https://htmlurl.com",
        "Test summary"
    );

    var responseEntity = restTemplate.getForEntity(endpoint, BookResponse.class);

    assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(responseEntity.hasBody()).isTrue();
    var foundBook = responseEntity.getBody();

    assertThat(foundBook)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(expectedResponse);
  }

  private Book stubBook(String title, String author, Provider bookProvider) {
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
    book.setProvider(bookProvider);
    return book;
  }

  private BookSummary stubBookSummary(Book book) {
    var bookSummary = new BookSummary();
    bookSummary.setSummary("Test summary");
    bookSummary.setBook(book);
    return bookSummary;
  }
}