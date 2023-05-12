package com.company.RssReaderBot.controllers.core;

import com.company.RssReaderBot.config.BotConfig;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;

@BotController
public class RssReaderBot implements TelegramMvcController {

	private final BotConfig botConfig;

	public RssReaderBot(BotConfig botConfig) {
		this.botConfig = botConfig;
	}

	@Override
	public String getToken() {
		return botConfig.getBotToken();
	}
}
