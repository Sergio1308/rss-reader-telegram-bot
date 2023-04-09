package com.company.RssReaderBot.db.repositories;

import com.company.RssReaderBot.db.models.FavoriteItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritesItemsRepository extends JpaRepository<FavoriteItem, Long> {
}
