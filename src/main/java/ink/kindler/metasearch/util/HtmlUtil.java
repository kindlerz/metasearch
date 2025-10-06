package ink.kindler.metasearch.util;

import org.jsoup.Jsoup;

public class HtmlUtil {
    private HtmlUtil() {

    }

    public static String stripHtml(String html) {
        return Jsoup.parse(html).text();
    }
}
