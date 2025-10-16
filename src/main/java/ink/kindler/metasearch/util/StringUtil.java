package ink.kindler.metasearch.util;

import org.springframework.util.StringUtils;

public class StringUtil {
  private StringUtil() {

  }

  public static String removeAllQuote(String str) {
    return StringUtils.replace(str, "\"", "");
  }

  public static boolean isDesiredLength(String str, int maxLength) {
    return StringUtils.hasLength(str) && str.length() <= maxLength;
  }
}
