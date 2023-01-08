package com.company.RssReaderBot.config;

import com.github.kshashov.telegram.config.TelegramBotGlobalProperties;
import com.github.kshashov.telegram.config.TelegramBotGlobalPropertiesConfiguration;
import com.pengrad.telegrambot.TelegramBot;
import lombok.Getter;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class BotConfig implements TelegramBotGlobalPropertiesConfiguration {

    @Getter
    @Value("${bot.token}")
    private String botToken;

    @Getter
    private TelegramBot telegramBot;

    @Override
    public void configure(TelegramBotGlobalProperties.Builder builder) {
        OkHttpClient okHttp = new OkHttpClient.Builder()
                .connectTimeout(12, TimeUnit.SECONDS)
                .build();
        builder
                .configureBot(botToken, botBuilder -> {
                    botBuilder.configure(builder1 -> builder1.okHttpClient(okHttp));
                })
                .processBot(botToken, bot -> telegramBot = bot);
    }
}
