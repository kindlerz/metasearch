package ink.kindler.metasearch;

import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.WireMockConfigurationCustomizer;

public class WireMockTestConfig implements WireMockConfigurationCustomizer {
  @Override
  public void customize(WireMockConfiguration config, ConfigureWireMock options) {
    config.notifier(new ConsoleNotifier(true));
  }
}
