package com.company.RssReaderBot.db.services;

import com.company.RssReaderBot.db.entities.FavoriteItem;
import com.company.RssReaderBot.db.entities.UserDB;
import com.company.RssReaderBot.db.repositories.FavoriteItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteItemService {

    private final FavoriteItemRepository favoriteItemRepository;

    public FavoriteItemService(FavoriteItemRepository favoriteItemRepository) {
        this.favoriteItemRepository = favoriteItemRepository;
    }

    public boolean hasItem(UserDB user, String title) {
        return favoriteItemRepository.existsByUserAndItemTitle(user, title);
    }

    public List<FavoriteItem> getAllFavoriteItems(Long userid) {
        return favoriteItemRepository.findFavoriteItemsByUserUserid(userid);
    }

    @Transactional
    public void removeFromFavorites(UserDB user, String itemTitle) {
        favoriteItemRepository.deleteFavoriteItemByUserAndItemTitle(user, itemTitle);
    }
}
