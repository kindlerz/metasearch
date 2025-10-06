package ink.kindler.metasearch.rest;

import ink.kindler.metasearch.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/search")
public class SearchController {
  private final SearchService searchService;

  public SearchController(SearchService searchService) {
    this.searchService = searchService;
  }

  @GetMapping
  public ResponseEntity<Void> searchBook(@RequestParam(name = "q") String query) {
    System.out.printf("Query: %s%n", query);
    searchService.search(query);
    return ResponseEntity.ok().build();
  }
}
