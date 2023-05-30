package com.company.RssReaderBot.db.repositories;

import com.company.RssReaderBot.db.entities.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Integer> {

    Optional<UserSettings> findByUserUserid(Long userid);

}
