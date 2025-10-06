package ink.kindler.metasearch.integration.service.impl;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import ink.kindler.metasearch.integration.service.StandardEbooksIntegration;
import ink.kindler.metasearch.integration.service.model.StandardEbooksBook;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static ink.kindler.metasearch.util.HtmlUtil.stripHtml;

@Component
public class DefaultStandardEbooksIntegration implements StandardEbooksIntegration {
  private static final String OPDS_URL = "https://standardebooks.org/feeds/opds/all";

  private final RestTemplate restTemplate;

  public DefaultStandardEbooksIntegration(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder
        .basicAuthentication("TOKEN", "")
        .build();
  }

  @Override
  public List<StandardEbooksBook> retrieveAllEbooksFromFeed() {
    var responseEntity = restTemplate.getForEntity(OPDS_URL, Resource.class);
    if (!responseEntity.getStatusCode().is2xxSuccessful()) {
      // TODO log warning
      return List.of();
    }
    return convertResponseBodyToFeedEntries(responseEntity.getBody())
        .parallelStream()
        .map(this::mapToBook).toList();
  }

  private StandardEbooksBook mapToBook(SyndEntry syndEntry) {
    return new StandardEbooksBook(
        syndEntry.getTitle(),
        syndEntry.getAuthor(),
        syndEntry.getLinks().get(2).getHref(),
        stripHtml(syndEntry.getContents().getFirst().getValue()),
        syndEntry.getLinks().get(3).getHref(), // Epub
        syndEntry.getLinks().get(5).getHref(), // Kobo
        syndEntry.getLinks().get(6).getHref(), // Azw3
        syndEntry.getLinks().get(7).getHref() // Html
    );
  }

  private List<SyndEntry> convertResponseBodyToFeedEntries(Resource body) {
    return getBodyAsInputStreamQuietly(body).map(this::getEntries).orElse(List.of());
  }

  private List<SyndEntry> getEntries(InputStream inputStream) {
    try {
      return new SyndFeedInput().build(new XmlReader(inputStream)).getEntries();
    } catch (FeedException | IOException exception) {
      // TODO Handle the exception
      return List.of();
    }
  }

  private Optional<InputStream> getBodyAsInputStreamQuietly(Resource body) {
    try {
      return Optional.of(body.getInputStream());
    } catch (IOException exception) {
      // TODO log error and return nothing
    }
    return Optional.empty();
  }
}