package com.company.RssReaderBot.services.parser;

import com.company.RssReaderBot.db.models.RssFeed;
import com.company.RssReaderBot.db.models.UserDB;
import com.company.RssReaderBot.db.repositories.RssFeedRepository;
import com.company.RssReaderBot.entities.ItemsList;
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

    public void getRssFeedAndSetToParser(UserDB userDB) {
        RssFeed feed = feedRepository.findByUserUserid(userDB.getUserid()).orElseThrow();
        parser.parseRss(feed.getUrl());
    }

    public void parseElementsByTitle(UserDB userDB, String title) {
        getRssFeedAndSetToParser(userDB);
        ItemsList.setItemsList(parser.getElementListByTitle(title));
    }

    public void parseAllElements(UserDB userDB) {
        getRssFeedAndSetToParser(userDB);
        ItemsList.setItemsList(parser.getAllElementsList());
    }

    public void parseElementsByDate(UserDB userDB, String dateString) {
        ItemsList.setItemsList(parser.getElementListByDate(dateString));
    }
}
