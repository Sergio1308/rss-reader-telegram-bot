package com.company.RssReaderBot.db.repositories;

import com.company.RssReaderBot.db.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
