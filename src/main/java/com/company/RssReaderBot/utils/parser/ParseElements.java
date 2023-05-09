package com.company.RssReaderBot.utils.parser;

import com.company.RssReaderBot.db.entities.RssFeed;
import com.company.RssReaderBot.db.repositories.RssFeedRepository;
import com.company.RssReaderBot.models.ItemsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParseElements {

    private final RssParser parser;

    private final RssFeedRepository feedRepository;

    public ParseElements(RssFeedRepository feedRepository, @Autowired RssParser parser) {
        this.feedRepository = feedRepository;
        this.parser = parser;
    }

    public void getRssFeedAndSetToParser(Integer feedId) {
        RssFeed feed = feedRepository.findById(feedId).orElseThrow();
        parser.parseRss(feed.getUrl());
    }

    public void parseElementsByTitle(String title) {
        ItemsList.setItemsList(parser.getElementListByTitle(title));
    }

    public void parseAllElements(Integer feedId) {
        getRssFeedAndSetToParser(feedId);
        ItemsList.setItemsList(parser.getAllElementsList());
    }

    public void parseElementsByDate(Integer feedId, String dateString) {
        getRssFeedAndSetToParser(feedId);
        ItemsList.setItemsList(parser.getElementListByDate(dateString));
    }
}
