package com.company.RssReaderBot.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

/**
 * This class represents a list of items: all items or found by title
 * itemsMap uses to get selected item object instantly and save it to variable
 */
public class ItemsList {

    @Getter @Setter
    private static List<Item> itemsList;
    @Getter @Setter
    private static HashMap<String, Item> itemsMap;

    public static void putInMap(String title, Item item) {
        itemsMap.put(title, item);
    }
}
