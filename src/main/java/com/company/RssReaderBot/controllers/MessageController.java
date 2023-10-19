package com.company.RssReaderBot.controllers;

import com.company.RssReaderBot.commands.*;
import com.company.RssReaderBot.config.BotConfig;
import com.company.RssReaderBot.controllers.core.BotState;
import com.company.RssReaderBot.utils.InvalidUserEnteredDateException;
import com.company.RssReaderBot.utils.parser.ParseElements;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.Getter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@BotController
public class MessageController implements TelegramMvcController, Controller {

    private final BotConfig botConfig;

    private final HelpCommand helpCommand;

    private final StartCommand startCommand;

    private final LoadItemsByTitleCommand loadItemsByTitleCommand;

    private final RssUrlValidationCommand rssUrlValidationCommand;

    private final ParseElements parseElements;

    @Getter
    private static final Map<Long, BotState> userStates = new HashMap<>();

    public MessageController(BotConfig botConfig, HelpCommand helpCommand, StartCommand startCommand,
                             RssUrlValidationCommand rssUrlValidationCommand, ParseElements parseElements,
                             LoadItemsByTitleCommand loadItemsByTitleCommand) {
        this.botConfig = botConfig;
        this.helpCommand = helpCommand;
        this.startCommand = startCommand;
        this.rssUrlValidationCommand = rssUrlValidationCommand;
        this.parseElements = parseElements;
        this.loadItemsByTitleCommand = loadItemsByTitleCommand;
    }

    @BotRequest(value = "/start")
    public BaseRequest<?, ?> sendStartMessage(Update update) {
        if (update.callbackQuery() != null) {
            Message message = update.callbackQuery().message();
            return startCommand.displayStartMenu(message.chat().id(), message.messageId());
        }
        return startCommand.execute(update.message());
    }

    @BotRequest(value = "/help")
    public BaseRequest<SendMessage, SendResponse> sendHelpMessage(Update update) {
        return helpCommand.execute(update.message());
    }

    @MessageRequest
    public BaseRequest<?, ?> handle(Update update) {
        String messageText = update.message().text();
        long chatId = update.message().chat().id();

        handleMessageLog(update);

        BotState currentState = userStates.getOrDefault(chatId, BotState.NONE);
        switch (currentState) {
            case SUBSCRIBE:
                return rssUrlValidationCommand.execute(update.message());
            case ENTER_TITLE:
                parseElements.parseElementsByTitle(messageText);
                return loadItemsByTitleCommand.process(update, chatId);
            case ENTER_DATE:
                try {
                    parseElements.parseElementsByDate(messageText);
                } catch (InvalidUserEnteredDateException e) {
                    return new SendMessage(chatId, e.getMessage() +
                            "\nEnter a valid date (for example, 10-05-2023).");
                }
                return loadItemsByTitleCommand.process(update, chatId);
            case NONE:
            default:
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
