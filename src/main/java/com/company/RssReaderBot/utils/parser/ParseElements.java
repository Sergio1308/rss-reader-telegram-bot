package com.company.RssReaderBot.utils.parser;

import com.company.RssReaderBot.db.entities.RssFeed;
import com.company.RssReaderBot.db.repositories.RssFeedRepository;
import com.company.RssReaderBot.models.ItemsList;
import com.company.RssReaderBot.utils.InvalidUserEnteredDateException;
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

    public void findFeedAndParseRss(Integer feedId) throws RssParsingException {
        RssFeed feed = feedRepository.findById(feedId).orElseThrow();
        parser.setFeedId(feedId);
        parser.parseRss(feed.getUrl());
    }

    public void parseElementsByTitle(String title) {
        itemsList.setItemsList(parser.getElementListByTitle(title));
    }

    public void parseAllElements(Integer feedId) throws RssParsingException {
        findFeedAndParseRss(feedId);
        itemsList.setItemsList(parser.getAllElementsList());
    }

    public void parseElementsByDate(String dateString) throws InvalidUserEnteredDateException {
        itemsList.setItemsList(parser.getElementListByDate(dateString));
    }
}
