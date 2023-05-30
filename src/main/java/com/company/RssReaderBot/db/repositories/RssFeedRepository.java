package com.company.RssReaderBot.db.repositories;

import com.company.RssReaderBot.db.entities.RssFeed;
import com.company.RssReaderBot.db.entities.UserDB;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RssFeedRepository extends JpaRepository<RssFeed, Integer> {

    Optional<RssFeed> findByUserUserid(Long userid);

    boolean existsByUserAndUrl(UserDB user, String url);

    Long countByUserUserid(Long userid);

    List<RssFeed> findRssFeedsByUserUserid(Long userid);

}
