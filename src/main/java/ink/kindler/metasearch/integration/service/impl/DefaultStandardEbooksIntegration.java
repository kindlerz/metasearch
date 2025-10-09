package ink.kindler.metasearch.integration.service.impl;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import ink.kindler.metasearch.integration.configuration.StandardEbooksProperties;
import ink.kindler.metasearch.integration.service.StandardEbooksIntegration;
import ink.kindler.metasearch.integration.service.model.StandardEbooksBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static ink.kindler.metasearch.util.HtmlUtil.stripHtml;

@Component
public class DefaultStandardEbooksIntegration implements StandardEbooksIntegration {
  private static final Logger logger = LoggerFactory.getLogger(DefaultStandardEbooksIntegration.class);

  private final RestTemplate restTemplate;
  private final StandardEbooksProperties standardEbooksProperties;

  public DefaultStandardEbooksIntegration(RestTemplateBuilder restTemplateBuilder, StandardEbooksProperties standardEbooksProperties) {
    this.standardEbooksProperties = standardEbooksProperties;
    this.restTemplate = restTemplateBuilder
        .basicAuthentication(standardEbooksProperties.getToken(), "")
        .build();
  }

  @Override
  public List<StandardEbooksBook> retrieveAllEbooksFromFeed() {
    try {
      var responseEntity = restTemplate.getForEntity(standardEbooksProperties.getOpdsUrl(), Resource.class);
      if (!responseEntity.getStatusCode().is2xxSuccessful()) {
        logger.warn("Received none 200 response from StandardEbooks server. Response code: {}", responseEntity.getStatusCode());
        return List.of();
      }
      return convertResponseBodyToFeedEntries(responseEntity.getBody())
          .parallelStream()
          .map(this::mapToBook).toList();
    } catch (HttpClientErrorException | HttpServerErrorException exception) {
      logger.error("Request failed with status {}: {}", exception.getStatusCode(), exception.getResponseBodyAsString());
      return List.of();
    }
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
        syndEntry.getLinks().get(7).getHref(), // Html
        syndEntry.getUpdatedDate().toInstant()
    );
  }

  private List<SyndEntry> convertResponseBodyToFeedEntries(Resource body) {
    return getBodyAsInputStreamQuietly(body).map(this::getEntries).orElse(List.of());
  }

  private List<SyndEntry> getEntries(InputStream inputStream) {
    try {
      return new SyndFeedInput().build(new XmlReader(inputStream)).getEntries();
    } catch (FeedException | IOException exception) {
      logger.error("Failed to convert StandardEbooks response to Syndicate Entries", exception);
      return List.of();
    }
  }

  private Optional<InputStream> getBodyAsInputStreamQuietly(Resource body) {
    try {
      return Optional.of(body.getInputStream());
    } catch (IOException exception) {
      logger.error("Failed to get input stream of StandardEbooks response", exception);
    }
    return Optional.empty();
  }
}