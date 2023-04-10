package com.company.RssReaderBot;

import com.company.RssReaderBot.entities.Item;
import com.company.RssReaderBot.services.parser.RssParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@SpringBootTest
public class RSSParserTest {
    private static String rssUrl;

    private final RssParser parser = new RssParser();

    private static final String staticRssUrlPath = "src/test/resources";

    @BeforeAll
    public static void setUp() {
        // https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml
    }

    @Test
    public void testGetAllElementsList() {
        parser.parseRss("https://rss.nytimes.com/services/xml/rss/nyt/Sports.xml");
        List<Item> elementsList = parser.getAllElementsList();
        Assertions.assertNotNull(elementsList);
        Assertions.assertFalse(elementsList.isEmpty());
        assertEquals(20, elementsList.size());
    }

    @Test
    public void testGetElementsListByTitle() {
        parser.parseRss("https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml");
        List<Item> elementsList= parser.getElementListByTitle("");
    }
}
