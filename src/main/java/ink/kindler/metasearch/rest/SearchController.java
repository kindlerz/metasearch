package ink.kindler.metasearch.rest;

import ink.kindler.metasearch.persistent.entity.Book;
import ink.kindler.metasearch.persistent.entity.Provider;
import ink.kindler.metasearch.persistent.projection.BookOverview;
import ink.kindler.metasearch.rest.model.BookOverviewResponse;
import ink.kindler.metasearch.rest.model.BookResponse;
import ink.kindler.metasearch.service.SearchService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/books")
public class SearchController {

  private final SearchService searchService;

  public SearchController(SearchService searchService) {
    this.searchService = searchService;
  }

  @GetMapping("/search")
  public ResponseEntity<List<BookOverviewResponse>> searchBook(@RequestParam(name="provider") Provider provider, @RequestParam(name = "q") String query) {
    if (query.length() > 100) {
      return ResponseEntity.badRequest().build();
    }
    var books = searchService.search(provider, query).parallelStream().map(this::convertToBookOverview).toList();
    return ResponseEntity.ok().body(books);
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookResponse> getBookDetailsById(@PathVariable("id") Long id) {
    return searchService.findBookById(id).map(this::convertToBookResponse)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  private BookOverviewResponse convertToBookOverview(BookOverview bookOverview) {
    return new BookOverviewResponse(
        bookOverview.getId(),
        bookOverview.getTitle(),
        bookOverview.getAuthor(),
        bookOverview.getCoverImageUrl()
    );
  }

  private BookResponse convertToBookResponse(Book book) {
    return new BookResponse(
        book.getId(),
        book.getTitle(),
        book.getAuthor(),
        book.getCoverImageUrl(),
        book.getEpubUrl(),
        book.getKoboUrl(),
        book.getMobiUrl(),
        book.getAzwUrl(),
        book.getHtmlUrl(),
        book.getSummary().getSummary()
    );
  }
}
