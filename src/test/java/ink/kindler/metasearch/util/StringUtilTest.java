package ink.kindler.metasearch.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StringUtilTest {

  @Test
  void shouldRemoveQuotes() {
    var quotedString = "My name is \"John\". My last name is \"Doe\".";

    var unquotedString = StringUtil.removeAllQuote(quotedString);

    assertThat(unquotedString).isEqualTo("My name is John. My last name is Doe.");
  }

  @MethodSource("stringCases")
  @ParameterizedTest
  void shouldCheckWhetherStringHasDesiredLength(String str, boolean isDesiredLength) {
    assertThat(StringUtil.isDesiredLength(str, 5)).isEqualTo(isDesiredLength);
  }

  private static Stream<Arguments> stringCases() {
    return Stream.of(
        Arguments.of("", false),
        Arguments.of(null, false),
        Arguments.of("Hello", true),
        Arguments.of("Hi", true),
        Arguments.of("Discussion", false)
    );
  }
}