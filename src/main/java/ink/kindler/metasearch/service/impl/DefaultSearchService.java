package ink.kindler.metasearch.service.impl;

import ink.kindler.metasearch.integration.service.StandardEbooksIntegration;
import ink.kindler.metasearch.service.SearchService;
import org.springframework.stereotype.Service;

@Service
public class DefaultSearchService implements SearchService {
  private final StandardEbooksIntegration standardEbooksIntegration;

  public DefaultSearchService(StandardEbooksIntegration standardEbooksIntegration) {
    this.standardEbooksIntegration = standardEbooksIntegration;
  }

  @Override
  public void search(String query) {
    standardEbooksIntegration.retrieveAllEbooksFromFeed();
  }
}
