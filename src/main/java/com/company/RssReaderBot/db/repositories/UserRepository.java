package com.company.RssReaderBot.db.repositories;

import com.company.RssReaderBot.db.entities.UserDB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDB, Long> {
}
