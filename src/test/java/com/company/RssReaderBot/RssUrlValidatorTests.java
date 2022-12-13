package com.company.RssReaderBot;

public class RssUrlValidatorTests {
    /*public static void main(String[] args) {
        // https://zapier.com/blog/how-to-find-rss-feed-url/ test rss urls
        *//*String rssUrl1 = "https://radiorecord.ru/rss/rss.xml";
        String rssUrl2 = "https://www.kissfm.ua/static/podcast.xml";
        String rssUrl3 = "https://lololo";
        String rssUrl4 = "https://..xml";
        String rssUrl5 = "https://news.yahoo.com/rss/us";
        String url6 = "https://proglib.io/"; // test connect time out
        String t = "http://test.com/lol.xml";*//*
        String[] rssArr = {
                "https://radiorecord.ru/rss/rss.xml", "https://www.kissfm.ua/static/podcast.xml", "test.test.haha",
                "https://news.yahoo.com/rss/us", "https://justinpot.com/feed", "https://example.tumblr.com/rss",
                "https://example.blogspot.com/feeds/posts/default", "https://www.newsweek.com/rss",
                "http://feeds.feedburner.com/TheAtlanticWire", "http://feeds.nbcnews.com/feeds/nbcpolitics",
                "http://twitchy.com/category/us-politics/feed/", "http://rss.cnn.com/rss/cnn_health.rss",
                "http://www.mayoclinic.org/rss/all-health-information-topics"
        };
        RssUrlValidator validator = new RssUrlValidator();
        for (String u : rssArr) {
            System.out.println(validator.validateRssUrl(u) + "\tisValid=" + validator.isValid());
        }
    }*/
}
