package com.company.RssReaderBot.models;

import lombok.*;

import java.util.Date;

@Getter
@Builder
@ToString
public class ItemModel {

    private final String title;
    private String description;
    private Date pubDate;
    private String mediaUrl;
    private String sourceLink;

}
