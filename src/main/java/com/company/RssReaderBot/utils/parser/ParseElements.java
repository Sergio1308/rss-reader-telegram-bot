package com.company.RssReaderBot.utils.parser;

import com.company.RssReaderBot.db.entities.RssFeed;
import com.company.RssReaderBot.db.repositories.RssFeedRepository;
import com.company.RssReaderBot.models.ItemsList;
import org.springframework.stereotype.Component;

@Component
public class ParseElements {

    private final RssParser parser;

    private final RssFeedRepository feedRepository;

    private final ItemsList itemsList;

    public ParseElements(RssFeedRepository feedRepository, RssParser parser, ItemsList itemsList) {
        this.feedRepository = feedRepository;
        this.parser = parser;
        this.itemsList = itemsList;
    }

    public void getRssFeedAndSetToParser(Integer feedId) {
        RssFeed feed = feedRepository.findById(feedId).orElseThrow();
        parser.parseRss(feed.getUrl());
    }

    public void parseElementsByTitle(String title) {
        itemsList.setItemsList(parser.getElementListByTitle(title));
    }

    public void parseAllElements(Integer feedId) {
        getRssFeedAndSetToParser(feedId);
        itemsList.setItemsList(parser.getAllElementsList());
    }

    public void parseElementsByDate(Integer feedId, String dateString) {
        getRssFeedAndSetToParser(feedId);
        itemsList.setItemsList(parser.getElementListByDate(dateString));
    }
}
