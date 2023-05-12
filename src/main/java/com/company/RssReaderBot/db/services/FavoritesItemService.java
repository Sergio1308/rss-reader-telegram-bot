package com.company.RssReaderBot.db.services;

import com.company.RssReaderBot.db.repositories.FavoritesItemRepository;
import org.springframework.stereotype.Service;

@Service
public class FavoritesItemService {

    private final FavoritesItemRepository favoritesItemRepository;

    public FavoritesItemService(FavoritesItemRepository favoritesItemRepository) {
        this.favoritesItemRepository = favoritesItemRepository;
    }
}
