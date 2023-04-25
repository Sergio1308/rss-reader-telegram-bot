package com.company.RssReaderBot.db.repositories;

import com.company.RssReaderBot.db.models.RssFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RssFeedRepository extends JpaRepository<RssFeed, Integer> {

    Optional<RssFeed> findByUserUserid(Long userid);

    Long countByUserUserid(Long userid);

    List<RssFeed> findAllByUserUserid(Long userid);
}
