package com.company.RssReaderBot.handlers;

import com.company.RssReaderBot.commands.*;
import com.company.RssReaderBot.parser.ParseElements;
import com.company.RssReaderBot.services.TelegramSendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Date;

public class MessageHandler implements Handler {

    TelegramSendMessage messageSender = new TelegramSendMessage();

    // commands instance fields
    private final StartCommand startCommand = new StartCommand();
    private final LoadItemsByTitleCommand loadItemsByTitleCommand = new LoadItemsByTitleCommand();
    private final RssUrlValidationCommand rssUrlValidationCommand = new RssUrlValidationCommand();
    // commandHandlersMap

    @Override
    public void handle(Update update) {
        String message = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        handleMessageLog(update);

        if (message.equals("/start")) {
            startCommand.execute(update);
//            loadMainMenuCommand.execute(chatId);
        } else if (StartCommand.hasEntered()) {
            rssUrlValidationCommand.execute(update, chatId);
        } else if (EnteringItemTitleCommand.hasEntered()) {
            // todo parse in loadItemsByTitleCommand
            ParseElements parseElements = new ParseElements();
            parseElements.parseElementsByTitle(message);
            loadItemsByTitleCommand.process(update, chatId);
        } else {
            String answer = "Invalid command. Use inline menu above\uD83D\uDC46";
            messageSender.sendText(chatId, answer);
        }
    }

    @Override
    public boolean hasHandler(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    private void handleMessageLog(Update update) {
        Date msgTime = new Date((long)update.getMessage().getDate() * 1000);
        User user = update.getMessage().getFrom();
        System.out.println(
                user + "\nHandle message " + "'" + update.getMessage().getText() + "'" + "\nDate: " + msgTime
        );
    }
}
