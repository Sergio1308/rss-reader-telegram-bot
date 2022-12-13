package com.company.RssReaderBot.core;

import com.company.RssReaderBot.handlers.UpdateHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;

@Component
public class RssReaderBot extends TelegramLongPollingBot {

//	@Value("${bot.username}")
	public static final String username = "";
//	@Value("${bot.token}")
	public static final String token = "";

	public final UpdateHandler updateHandler = new UpdateHandler();

	@Override
	public void onUpdateReceived(Update update) {
		if (updateHandler.hasHandler(update)) {
			updateHandler.handle(update);
		}
	}

	@Override
	public String getBotUsername() {
		return username;
	}

	@Override
	public String getBotToken() {
		return token;
	}

	@PostConstruct
	public void start() {}
}
