package com.company.RssReaderBot.db.repositories;

import com.company.RssReaderBot.db.models.FavoritesItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritesItemRepository extends JpaRepository<FavoritesItem, Integer> {
}
