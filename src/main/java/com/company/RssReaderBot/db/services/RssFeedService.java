package com.company.RssReaderBot.db.services;

import com.company.RssReaderBot.db.entities.FavoriteItem;
import com.company.RssReaderBot.db.entities.RssFeed;
import com.company.RssReaderBot.db.entities.UserDB;
import com.company.RssReaderBot.db.repositories.RssFeedRepository;
import com.company.RssReaderBot.models.ItemModel;
import com.company.RssReaderBot.utils.parser.RssParser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RssFeedService {

    private final RssFeedRepository feedRepository;

    private final RssParser parser;

    @Getter @Setter
    private String feedTitle;

    public RssFeedService(RssFeedRepository feedRepository, RssParser parser) {
        this.feedRepository = feedRepository;
        this.parser = parser;
    }

    public void addFeed(UserDB userDB, String url) {
        parser.parseFeedTitle(url);
        RssFeed feed = new RssFeed(userDB, url, parser.getFeedTitle());
        feedRepository.save(feed);
    }

    public void updateFeed(RssFeed feed) {
        feedRepository.save(feed);
    }

    public boolean hasFeed(UserDB user, String url) {
        return feedRepository.existsByUserAndUrl(user, url);
    }

    public void addFavoriteItem(UserDB userDB, ItemModel itemModel) {
        FavoriteItem favoriteItem = new FavoriteItem(userDB, itemModel);
        // todo: handle the case if rss feed is not exist in db -> add to db with posting=false + sub=false
        //  if sub=false then posting must be always false
        feedRepository.findById(itemModel.getFeedId()).ifPresent(feed -> {
            favoriteItem.setFeed(feed);
            feed.addItem(favoriteItem);
            feedRepository.save(feed);
            }
        );
    }

    public void removeFeed(Integer feedId) {
        feedRepository.deleteById(feedId);
    }

    public List<RssFeed> getAllFeeds(Long userid) {
        return feedRepository.findRssFeedsByUserUserid(userid);
    }

    public Long countUserFeeds(Long userid) {
        return feedRepository.countByUserUserid(userid);
    }

}
