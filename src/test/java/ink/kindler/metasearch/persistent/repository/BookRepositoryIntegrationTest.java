package ink.kindler.metasearch.persistent.repository;

import ink.kindler.metasearch.TestcontainersConfiguration;
import ink.kindler.metasearch.persistent.entity.Book;
import ink.kindler.metasearch.persistent.entity.BookSummary;
import ink.kindler.metasearch.persistent.entity.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class BookRepositoryIntegrationTest {

  @Autowired
  private BookRepository bookRepository;

  @BeforeEach
  void cleanDatabase() {
    bookRepository.deleteAll();
  }

  @Test
  void shouldFindBooksByProviderAndAuthorOrTitleLike() {
    var books = List.of(
        stubBook( "The Adventures of Tom Sawyer", "Mark Twain", Provider.STANDARD_EBOOKS),
        stubBook("Adventures of Huckleberry Finn", "Mark Twain", Provider.GUTENBERG),
        stubBook("Mark Zuckerberg", "Mark Zuckerberg", Provider.STANDARD_EBOOKS),
        stubBook("Nineteen Eighty-Four (1984)", "George Orwell", Provider.STANDARD_EBOOKS),
        stubBook("The Mark of Zorro", "Johnston McCulley", Provider.STANDARD_EBOOKS)
    );
    bookRepository.saveAll(books);

    var foundBooks = bookRepository.searchBooks(Provider.STANDARD_EBOOKS, "mArK", PageRequest.of(0, 10));

    assertThat(foundBooks).hasSize(3);

    assertThat(foundBooks.get(0).getTitle()).isEqualTo("The Mark of Zorro");
    assertThat(foundBooks.get(0).getAuthor()).isEqualTo("Johnston McCulley");

    assertThat(foundBooks.get(1).getTitle()).isEqualTo("The Adventures of Tom Sawyer");
    assertThat(foundBooks.get(1).getAuthor()).isEqualTo("Mark Twain");

    assertThat(foundBooks.get(2).getTitle()).isEqualTo("Mark Zuckerberg");
    assertThat(foundBooks.get(2).getAuthor()).isEqualTo("Mark Zuckerberg");
  }

  @Test
  void shouldDeleteAllEbooksByProvider() {
    var books = List.of(
        stubBook( "The Adventures of Tom Sawyer", "Mark Twain", Provider.STANDARD_EBOOKS),
        stubBook("Adventures of Huckleberry Finn", "Mark Twain", Provider.GUTENBERG),
        stubBook("Mark Zuckerberg", "Mark Zuckerberg", Provider.STANDARD_EBOOKS)
    );

    bookRepository.saveAll(books);
    bookRepository.deleteAllByProvider(Provider.STANDARD_EBOOKS);

    var availableBooks = bookRepository.findAll();

    assertThat(availableBooks).size().isEqualTo(1);
    assertThat(availableBooks.getFirst().getTitle()).isEqualTo("Adventures of Huckleberry Finn");
  }

  @Test
  void shouldCountAvailableBooksByProvider() {
    var books = List.of(
        stubBook( "The Adventures of Tom Sawyer", "Mark Twain", Provider.STANDARD_EBOOKS),
        stubBook("Adventures of Huckleberry Finn", "Mark Twain", Provider.GUTENBERG),
        stubBook("Mark Zuckerberg", "Mark Zuckerberg", Provider.STANDARD_EBOOKS)
    );

    bookRepository.saveAll(books);

    int availableBookCount = bookRepository.countByProvider(Provider.STANDARD_EBOOKS);

    assertThat(availableBookCount).isEqualTo(2);
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