package com.company.RssReaderBot.entities;

import java.util.HashMap;
import java.util.List;

/**
 * This class represents a list of items: all items or found by title
 * itemsMap uses to get selected item object instantly and save it to variable
 */
public class ItemsList {

    private static List<Item> itemsList;

    private static HashMap<String, Item> itemsMap;

    public static List<Item> getItemsList() {
        return itemsList;
    }

    public static void setItemsList(List<Item> itemsList) {
        ItemsList.itemsList = itemsList;
    }

    public static HashMap<String, Item> getItemsMap() {
        return itemsMap;
    }

    public static void setItemsMap(HashMap<String, Item> itemsMap) {
        ItemsList.itemsMap = itemsMap;
    }

    public static void putInMap(String title, Item item) {
        itemsMap.put(title, item);
    }
}
