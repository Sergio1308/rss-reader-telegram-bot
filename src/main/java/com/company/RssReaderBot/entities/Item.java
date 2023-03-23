package com.company.RssReaderBot.entities;

import lombok.Getter;
import lombok.Setter;

public class Item {

    @Getter
    private final String title;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private String pubDate;
    @Getter @Setter
    private String mediaUrl;
    @Getter @Setter
    private String guid;

    public Item(String title, String description, String pubDate, String mediaUrl, String guid) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.mediaUrl = mediaUrl;
        this.guid = guid;
    }

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", guid='" + guid + '\'' +
                '}';
    }
}
