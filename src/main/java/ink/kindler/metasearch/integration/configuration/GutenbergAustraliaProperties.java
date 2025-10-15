package ink.kindler.metasearch.integration.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "gutenberg-australia")
@Component
public class GutenbergAustraliaProperties {

  private boolean alwaysReindex;

  public boolean isAlwaysReindex() {
    return alwaysReindex;
  }

  public void setAlwaysReindex(boolean alwaysReindex) {
    this.alwaysReindex = alwaysReindex;
  }
}
