package ink.kindler.metasearch.rest;

import ink.kindler.metasearch.integration.service.StandardEbooksIntegration;
import ink.kindler.metasearch.integration.service.model.StandardEbooksBook;
import ink.kindler.metasearch.persistent.entity.Book;
import ink.kindler.metasearch.persistent.entity.BookSummary;
import ink.kindler.metasearch.persistent.entity.Provider;
import ink.kindler.metasearch.service.BookService;
import ink.kindler.metasearch.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/search")
public class SearchController {
  @Autowired
  private StandardEbooksIntegration standardEbooksIntegration;
  @Autowired
  private BookService bookService;

  private final SearchService searchService;

  public SearchController(SearchService searchService) {
    this.searchService = searchService;
  }

  @GetMapping
  public ResponseEntity<Void> searchBook(@RequestParam(name = "q") String query) {
    System.out.printf("Query: %s%n", query);
    var ebooks = standardEbooksIntegration.retrieveAllEbooksFromFeed()
        .parallelStream().map(this::convertToBook).toList();
    //bookService.saveBooks(ebooks);

    return ResponseEntity.ok().build();
  }

  private Book convertToBook(StandardEbooksBook standardEbooksBook) {
    var book = new Book();
    var bookSummary = new BookSummary();
    bookSummary.setSummary(standardEbooksBook.summary());
    bookSummary.setBook(book);
    book.setTitle(standardEbooksBook.title());
    book.setAuthor(standardEbooksBook.author());
    book.setSummary(bookSummary);
    book.setCoverImageUrl(standardEbooksBook.coverImage());
    book.setEpubUrl(standardEbooksBook.epub());
    book.setKoboUrl(standardEbooksBook.kobo());
    book.setAzwUrl(standardEbooksBook.azw());
    book.setHtmlUrl(standardEbooksBook.html());
    book.setProvider(Provider.STANDARD_EBOOK);
    return book;
  }
}
