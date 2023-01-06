package com.company.RssReaderBot.core;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

@BotController
public class RssReaderBot implements TelegramMvcController {

	@Value("${bot.token}")
	private String botToken;

	@Override
	public String getToken() { return botToken; }

	@PostConstruct
	public void start() {
		System.out.println("token: " + getToken());
	} // todo: debug, remove
}
