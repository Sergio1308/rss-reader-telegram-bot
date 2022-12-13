package com.company.RssReaderBot;

import com.company.RssReaderBot.core.RssReaderBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class RssReaderBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(RssReaderBotApplication.class, args);
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new RssReaderBot());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
