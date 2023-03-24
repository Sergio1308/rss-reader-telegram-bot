package com.company.RssReaderBot.parser;

import com.company.RssReaderBot.entities.ItemsList;

import java.util.Date;

public class ParseElements {

    public static RssParser parser;

    public ParseElements() {
        parser = new RssParser();
    }

    public void parseElementsByTitle(String title) {
        ItemsList.setItemsList(parser.getElementListByTitle(title));
    }

    public void parseAllElements() {
        ItemsList.setItemsList(parser.getAllElementsList());
    }

    public void parseElementsByDate(String dateString) {
        ItemsList.setItemsList(parser.getElementListByDate(dateString));
    }
}
