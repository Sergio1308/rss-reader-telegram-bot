package com.company.RssReaderBot.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * This class represents a list of items: all items or found by title
 * itemsMap uses to get selected item object instantly and save it to variable
 */
@Component
public class ItemsList {

    @Getter @Setter
    private List<ItemModel> itemsList;
    @Getter @Setter
    private HashMap<String, ItemModel> itemsMap;

    public void putInMap(String title, ItemModel itemModel) {
        itemsMap.put(title, itemModel);
    }
}
