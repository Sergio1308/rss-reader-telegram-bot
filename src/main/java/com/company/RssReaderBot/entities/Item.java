package com.company.RssReaderBot.entities;

import org.springframework.beans.factory.annotation.Value;

public class Item {
    @Value("${episode.header}")
    private String userHeader;
    private String description;
    private String title;

    private ItemUrl itemUrl;

    private ItemDate date;

    public Item(String title, String description, ItemUrl itemUrl, ItemDate date) {
        this.title = title;
        this.description = description;
        this.itemUrl = itemUrl;
        this.date = date;
    }

    public ItemDate getDate() {
        return date;
    }

    public void setDate(ItemDate date) {
        this.date = date;
    }

    public String getUserHeader() {
        return userHeader;
    }

    public void setUserHeader(String userHeader) {
        this.userHeader = userHeader;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = title;
    }

    public ItemUrl getAudio() {
        return itemUrl;
    }

    public void setAudio(ItemUrl itemUrl) {
        this.itemUrl = itemUrl;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "userHeader='" + userHeader + '\'' +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", audio=" + itemUrl +
                ", date=" + date +
                '}';
    }
}
