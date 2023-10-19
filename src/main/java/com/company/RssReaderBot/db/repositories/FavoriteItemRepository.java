package com.company.RssReaderBot.db.repositories;

import com.company.RssReaderBot.db.entities.FavoriteItem;
import com.company.RssReaderBot.db.entities.UserDB;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Integer> {
    boolean existsByUserAndItemTitle(UserDB user, String title);

    List<FavoriteItem> findFavoriteItemsByUserUserid(Long userid);

    void deleteFavoriteItemByUserAndItemTitle(UserDB user, String title);

}
