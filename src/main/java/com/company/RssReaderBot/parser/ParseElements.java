package com.company.RssReaderBot.parser;

import com.company.RssReaderBot.entities.ItemsList;

import java.util.Date;

public class ParseElements {

    public static final RssParser parser = new RssParser();

    public ParseElements() {
        parser.parseRss();
    }

    public void parseElementsByTitle(String title) {
        ItemsList.setItemsList(parser.getElementListByTitle(parser.getNodeList(), title));
    }

    public void parseAllElements() {
        ItemsList.setItemsList(parser.getAllElementsList(parser.getNodeList()));
    }

    public void parseElementByDate(Date date) {
        ItemsList.setItemsList(parser.getElementListByDate(parser.getNodeList(), date));
    }
}
