package com.company.RssReaderBot.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class ItemModel {

    @Getter
    private final String title;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private Date pubDate;
    @Getter @Setter
    private String mediaUrl;
    @Getter @Setter
    private String guid;

    public ItemModel(String title, String description, Date pubDate, String mediaUrl, String guid) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.mediaUrl = mediaUrl;
        this.guid = guid;
    }

    public ItemModel(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ItemModel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", guid='" + guid + '\'' +
                '}';
    }
}
