package com.company.RssReaderBot.handlers;

import com.company.RssReaderBot.commands.*;
import com.company.RssReaderBot.config.BotConfig;
import com.company.RssReaderBot.core.RssReaderBot;
import com.company.RssReaderBot.parser.ParseElements;
import com.github.kshashov.telegram.api.MessageType;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@BotController
public class MessageHandler implements TelegramMvcController, Handler {

    // commands instance fields
    @Autowired
    private BotConfig botConfig;

    @Autowired
    private StartCommand startCommand;

    private final LoadItemsByTitleCommand loadItemsByTitleCommand = new LoadItemsByTitleCommand();

    @Autowired
    private RssUrlValidationCommand rssUrlValidationCommand;

    // commandHandlersMap

    @BotRequest(value = "/start", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest sendStartMessage(Update update) {
        return startCommand.execute(update);
    }

    @MessageRequest
    public BaseRequest handle(Update update) {
        String message = update.message().text();
        long chatId = update.message().chat().id();

        handleMessageLog(update);

        if (message.equals("/start")) {
            return startCommand.execute(update);
        } else if (StartCommand.hasEntered()) {
            return rssUrlValidationCommand.execute(update, chatId);
        } else if (EnteringItemTitleCommand.hasEntered()) {
            // todo parse in loadItemsByTitleCommand
            ParseElements parseElements = new ParseElements();
            parseElements.parseElementsByTitle(message);
            return loadItemsByTitleCommand.process(update, chatId);
        } else {
            String answer = "Invalid command. Use inline menu above\uD83D\uDC46";
            return new SendMessage(chatId, answer);
        }
    }

    private void handleMessageLog(Update update) {
        Date msgTime = new Date((long)update.message().date() * 1000);
        User user = update.message().from();
        System.out.println(
                user + "\nHandle message " + "'" + update.message().text() + "'" + "\nDate: " + msgTime
        );
    }

    @Override
    public String getToken() {
        return botConfig.getBotToken();
    }
}
