package com.company.RssReaderBot.services;

import com.company.RssReaderBot.db.models.RssFeed;
import com.company.RssReaderBot.db.models.UserDB;
import com.company.RssReaderBot.db.repositories.RssFeedRepository;
import com.company.RssReaderBot.services.parser.RssParser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedService {

    @Autowired
    private RssFeedRepository feedRepository;

    @Autowired
    private RssParser parser;

    @Getter @Setter
    private String feedTitle;

    public void addFeed(UserDB userDB, String url) {
        feedTitle = parser.getFeedTitle(url);
        RssFeed feed = new RssFeed(userDB, url, feedTitle);
        feedRepository.save(feed);
    }

    public List<RssFeed> getAllFeeds(Long userid) {
        return feedRepository.findAllByUserUserid(userid);
    }

    public Long countUserFeeds(Long userid) {
        return feedRepository.countByUserUserid(userid);
    }
    // todo parse channel title and save to db
}
