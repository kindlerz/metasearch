package ink.kindler.metasearch.integration.scheduler;

import ink.kindler.metasearch.integration.service.StandardEbooksIntegration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StandardEbooksIndexer {
  private final StandardEbooksIntegration standardEbooksIntegration;

  public StandardEbooksIndexer(StandardEbooksIntegration standardEbooksIntegration) {
    this.standardEbooksIntegration = standardEbooksIntegration;
  }

  @Scheduled(cron = "0 0 0 * * *")
  public void indexEbooks() {
    var ebooks = standardEbooksIntegration.retrieveAllEbooksFromFeed();

  }
}
