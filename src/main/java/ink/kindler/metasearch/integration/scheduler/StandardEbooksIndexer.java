package ink.kindler.metasearch.integration.scheduler;

import ink.kindler.metasearch.integration.service.StandardEbooksIntegration;
import ink.kindler.metasearch.integration.service.model.StandardEbooksBook;
import ink.kindler.metasearch.persistent.entity.Book;
import ink.kindler.metasearch.persistent.entity.BookSummary;
import ink.kindler.metasearch.persistent.entity.Provider;
import ink.kindler.metasearch.service.BookService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StandardEbooksIndexer {
  private final Logger logger = LoggerFactory.getLogger(StandardEbooksIndexer.class);
  private final StandardEbooksIntegration standardEbooksIntegration;
  private final BookService bookService;

  public StandardEbooksIndexer(StandardEbooksIntegration standardEbooksIntegration, BookService bookService) {
    this.standardEbooksIntegration = standardEbooksIntegration;
    this.bookService = bookService;
  }

  @Scheduled(cron = "0 0 0 * * *")
  @SchedulerLock(name = "StandardEbooksIndexer", lockAtLeastFor = "10m", lockAtMostFor = "30m")
  public void indexEbooks() {
    logger.info("Started StandardEbooks indexer");
    bookService.deleteAll();
    var ebooks = standardEbooksIntegration.retrieveAllEbooksFromFeed().parallelStream().map(this::convertToBook).toList();
    bookService.saveBooks(ebooks);
    logger.info("Finished StandardEbooks indexer. {} ebooks upserted", ebooks.size());
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
    book.setProvider(Provider.STANDARD_EBOOKS);
    return book;
  }
}
