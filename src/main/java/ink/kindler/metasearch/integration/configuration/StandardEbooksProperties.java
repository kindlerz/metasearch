package ink.kindler.metasearch.integration.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "standard-ebooks")
@Component
public class StandardEbooksProperties {
  private String token;
  private String opdsUrl;

  public String getToken() {
    return token;
  }

  void setToken(String token) {
    this.token = token;
  }

  public String getOpdsUrl() {
    return opdsUrl;
  }

  void setOpdsUrl(String opdsUrl) {
    this.opdsUrl = opdsUrl;
  }
}
