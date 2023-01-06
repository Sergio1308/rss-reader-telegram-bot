package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.config.BotConfig;
import com.company.RssReaderBot.db.DatabaseHandler;
import com.company.RssReaderBot.parser.RssParser;
import com.company.RssReaderBot.parser.RssUrlValidator;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Component;

@Component
public class RssUrlValidationCommand implements Command<Long, Integer> {

    private final LoadMainMenuCommand loadMainMenuCommand = new LoadMainMenuCommand();

    private final DatabaseHandler databaseHandler = DatabaseHandler.getInstance();

    public BaseRequest<SendMessage, SendResponse> execute(Update update, Long chatId) {
        String message = update.message().text();
        BotConfig.getTelegramBot().execute(new SendMessage(chatId, "Processing your request..."));
        RssUrlValidator urlValidator = new RssUrlValidator();
        String result = urlValidator.validateRssUrl(message);
        if (urlValidator.isValid()) {
            StartCommand.setEntered(false);
            BotConfig.getTelegramBot().execute(new SendMessage(chatId, "Valid URL. Saved to your personal settings, " +
                    "where you can change your RSS URL at any time"));
            RssParser.setRssUrl(result);
            databaseHandler.updateRssUrl(update.message().from().id(), RssParser.getRssUrl());
            return loadMainMenuCommand.execute(chatId);
        } else {
            return new SendMessage(chatId, result + "\n▶Send me RSS URL below again\uD83D\uDC47" +
                    "\n▶or click on the button above to learn more about RSS\uD83D\uDC46");
        }
    }

    @Override
    public BaseRequest execute(Long chatId, Integer messageId) {
        return null;
    }
}
