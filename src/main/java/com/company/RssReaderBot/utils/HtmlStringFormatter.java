package com.company.RssReaderBot.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Safelist;

/**
 * This class provides utility methods for formatting HTML strings (bypassing the occurrence of a conflict
 * when using parse mode by Telegram API when some string may have HTML tags).
 * It allows sanitizing and removing HTML tags from strings, ensuring proper formatting and handling of HTML content.
 */
public class HtmlStringFormatter {

    private HtmlStringFormatter() {

    }

    public static String sanitizeString(String str) {
        if (!hasHtmlTags(str)) {
            return str;
        }
        Document document = Jsoup.parse(str);
        document.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
        return Jsoup.clean(document.html(), "", Safelist.none());
    }

    private static boolean hasHtmlTags(String str) {
        // some cases could be false, so we also check if string starts with '<'
        return !Jsoup.isValid(str, Safelist.none()) || str.startsWith("<");
    }
}
