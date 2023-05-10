package com.company.RssReaderBot.db.services;

import com.company.RssReaderBot.db.repositories.ItemRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public boolean hasItem(String title) {
        return itemRepository.existsByTitle(title);
    }
}
