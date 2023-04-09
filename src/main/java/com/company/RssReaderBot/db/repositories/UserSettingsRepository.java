package com.company.RssReaderBot.db.repositories;

import com.company.RssReaderBot.db.models.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
}
