package com.company.RssReaderBot.core;

import com.company.RssReaderBot.config.BotConfig;
import com.company.RssReaderBot.db.DatabaseHandler;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.sql.SQLException;

@BotController
public class RssReaderBot implements TelegramMvcController {

	@Autowired
	private BotConfig botConfig;

	@Autowired
	private DatabaseHandler databaseHandler;

	@Override
	public String getToken() { return botConfig.getBotToken(); }

	public TelegramBot getBot() { return botConfig.getTelegramBot(); }

	@PostConstruct
	public void start() {
		try {
			System.out.println("token: " + getToken() + "\nConnection: " + databaseHandler.getConnection());
		} catch (ClassNotFoundException | SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
