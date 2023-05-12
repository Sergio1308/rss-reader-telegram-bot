package com.company.RssReaderBot;

import com.company.RssReaderBot.utils.parser.RssUrlValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RssUrlValidatorTest {

    @Autowired
    private RssUrlValidator validator;

    @Test
    void testValidUrl() {
        String url = "https://news.yahoo.com/rss/us";
        String result = validator.validateRssUrl(url);

        assertTrue(validator.isValid());
        assertEquals(result, url);
    }

    @Test
    void testInvalidUrl() {
        String url = "https://..xml";
        String result = validator.validateRssUrl(url);

        assertFalse(validator.isValid());
        assertTrue(result.contains("Unable to connect to URL"));
    }

    @Test
   void testUnsupportedMimeType() {
        String url = "https://news.yahoo.com/";
        String result = validator.validateRssUrl(url);

        assertFalse(validator.isValid());
        assertTrue(result.contains("unsupported format"));
    }

    @Test
    void testRedirectUrl() {
        String url = "http://feeds.feedburner.com/TheAtlanticWire";
        String result = validator.validateRssUrl(url);

        assertTrue(validator.isValid());
        assertTrue(result.startsWith("https://"));
        assertNotEquals(result, url);
    }

    @Test
    void testInvalidResponseCodeUrl() {
        String url = "https://twitchy.com/category/us-politics/feed/";
        String result = validator.validateRssUrl(url);

        assertFalse(validator.isValid());
        assertTrue(result.contains("Unable to get response"));
    }

    @Test
    void testCustomTimeouts() {
        int readTimeout = 2000;
        int connectTimeout = 2000;
        List<String> contentTypes = List.of("text/xml");
        validator = new RssUrlValidator(readTimeout, connectTimeout, contentTypes);
        String url = "https://www.mayoclinic.org/rss/all-health-information-topics";
        String result = validator.validateRssUrl(url);

        assertTrue(validator.isValid());
        assertEquals(result, url);
    }
}
