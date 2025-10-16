package ink.kindler.metasearch.integration.runner;

import ink.kindler.metasearch.integration.service.GutenbergAustraliaService;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SimpleLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Order(1)
@Component
public class GutenbergAustraliaRunner implements CommandLineRunner {
  private static final Logger logger = LoggerFactory.getLogger(GutenbergAustraliaRunner.class);

  private final GutenbergAustraliaService gutenbergAustraliaService;
  private final LockProvider lockProvider;

  @Value("classpath:gutenberg-australia/index_with_summary.csv")
  private Resource indexCsv;

  public GutenbergAustraliaRunner(GutenbergAustraliaService gutenbergAustraliaService, LockProvider lockProvider) {
    this.gutenbergAustraliaService = gutenbergAustraliaService;
    this.lockProvider = lockProvider;
  }

  @Override
  public void run(String... args) throws Exception {
    logger.info("Starting Gutenberg Australia Data Seeder runner");
    if (gutenbergAustraliaService.shouldSkipIndexing()) {
      logger.info("No indexing is needed. Skipping");
      return;
    }
    var lock = getLock();
    if (lock.isEmpty()) {
      logger.info("Another instance is already running — skipping seeding");
      return;
    }
    try {
      gutenbergAustraliaService.refreshIndex(indexCsv);
      logger.info("Successfully indexed all Gutenberg Australia books");
    } finally {
      lock.ifPresent(SimpleLock::unlock);
    }
  }

  private Optional<SimpleLock> getLock() {
    var lockConfiguration = new LockConfiguration(Instant.now(Clock.systemUTC()), "GutenbergAustraliaRunner",
        Duration.ofMinutes(10), Duration.ofMinutes(1));
    return lockProvider.lock(lockConfiguration);
  }
}