package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.db.DatabaseHandler;
import com.company.RssReaderBot.parser.RssParser;
import com.company.RssReaderBot.parser.RssUrlValidator;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RssUrlValidationCommand implements Command<Long> {

    private final LoadMainMenuCommand loadMainMenuCommand = new LoadMainMenuCommand();

    private final DatabaseHandler databaseHandler = DatabaseHandler.getInstance();

    public void execute(Update update, Long chatId) {
        String message = update.getMessage().getText();

        messageSender.sendText(chatId, "Processing your request...");
        RssUrlValidator urlValidator = new RssUrlValidator();
        String result = urlValidator.validateRssUrl(message);
        if (urlValidator.isValid()) {
            StartCommand.setEntered(false);
            messageSender.sendText(chatId, "Valid URL. Saved to your personal settings, " +
                    "where you can change your RSS URL at any time");
            RssParser.setRssUrl(result);
            databaseHandler.updateRssUrl(update.getMessage().getFrom().getId(), RssParser.getRssUrl());
            loadMainMenuCommand.execute(chatId);
        } else {
            messageSender.sendText(chatId, result + "\n▶Send me RSS URL below again\uD83D\uDC47" +
                    "\n▶or click on the button above to learn more about RSS\uD83D\uDC46");
        }
    }

    @Override
    public void execute(Long chatId, Long messageId) {
    }
}
