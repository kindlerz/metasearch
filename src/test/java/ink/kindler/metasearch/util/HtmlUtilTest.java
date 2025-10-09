package ink.kindler.metasearch.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HtmlUtilTest {

  @Test
  void shouldRemoveHtmlFromString() {
    var html = """
        <p>
          This is a link to <a href="https://google.com">Google</a>
        </p>
        """;

    var strippedHtml = HtmlUtil.stripHtml(html);

    assertThat(strippedHtml).isEqualTo("This is a link to Google");
  }
}