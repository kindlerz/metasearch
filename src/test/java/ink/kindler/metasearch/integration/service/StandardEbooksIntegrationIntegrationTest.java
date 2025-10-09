package ink.kindler.metasearch.integration.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import ink.kindler.metasearch.TestcontainersConfiguration;
import ink.kindler.metasearch.WireMockTestConfig;
import ink.kindler.metasearch.integration.configuration.StandardEbooksProperties;
import ink.kindler.metasearch.integration.service.model.StandardEbooksBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({WireMockTestConfig.class, TestcontainersConfiguration.class})
@AutoConfigureWireMock(
    port = 0,
    stubs = "classpath:/wiremock/stubs/responses",
    files = "classpath:/wiremock/stubs")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StandardEbooksIntegrationIntegrationTest {

  @Autowired
  private StandardEbooksIntegration standardEbooksIntegration;

  @Autowired
  private StandardEbooksProperties properties;

  @Autowired
  private WireMockServer wireMockServer;

  @Test
  void shouldRetrieveAllEbooksFromFeed() {
    var expectedEbooks = List.of(new StandardEbooksBook(
        "The Invisible Man",
        "H. G. Wells",
        "https://standardebooks.org/ebooks/h-g-wells/the-invisible-man/downloads/cover-thumbnail.jpg",
        "Griffin, a scientist...",
        "https://standardebooks.org/ebooks/h-g-wells/the-invisible-man/downloads/h-g-wells_the-invisible-man.epub?source=feed",
        "https://standardebooks.org/ebooks/h-g-wells/the-invisible-man/downloads/h-g-wells_the-invisible-man.kepub.epub?source=feed",
        "https://standardebooks.org/ebooks/h-g-wells/the-invisible-man/downloads/h-g-wells_the-invisible-man.azw3?source=feed",
        "https://standardebooks.org/ebooks/h-g-wells/the-invisible-man/text/single-page",
        Instant.parse("2025-10-08T21:56:51Z")),
        new StandardEbooksBook(
        "Recollections of Full Years",
        "Helen Herron Taft",
        "https://standardebooks.org/ebooks/helen-herron-taft/recollections-of-full-years/downloads/cover-thumbnail.jpg",
        "Helen Herron Taft served as First Lady...",
        "https://standardebooks.org/ebooks/helen-herron-taft/recollections-of-full-years/downloads/helen-herron-taft_recollections-of-full-years.epub?source=feed",
        "https://standardebooks.org/ebooks/helen-herron-taft/recollections-of-full-years/downloads/helen-herron-taft_recollections-of-full-years.kepub.epub?source=feed",
        "https://standardebooks.org/ebooks/helen-herron-taft/recollections-of-full-years/downloads/helen-herron-taft_recollections-of-full-years.azw3?source=feed",
        "https://standardebooks.org/ebooks/helen-herron-taft/recollections-of-full-years/text/single-page",
        Instant.parse("2025-10-08T02:04:59Z"))
    );

    var ebooks = standardEbooksIntegration.retrieveAllEbooksFromFeed();

    assertThat(ebooks).isEqualTo(expectedEbooks);
  }

  @Test
  void shouldReturnNoEbooksOnFailure() {
    ReflectionTestUtils.setField(properties, "opdsUrl",
        "http://localhost:" + wireMockServer.port() + "/feeds/opds/all?simulateFailure=true");

    var result = standardEbooksIntegration.retrieveAllEbooksFromFeed();

    assertThat(result).isEmpty();
  }
}