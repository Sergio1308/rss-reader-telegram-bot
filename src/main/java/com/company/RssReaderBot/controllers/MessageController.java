package com.company.RssReaderBot.controllers;

import com.company.RssReaderBot.commands.*;
import com.company.RssReaderBot.config.BotConfig;
import com.company.RssReaderBot.commands.RssUrlValidationCommand;
import com.company.RssReaderBot.utils.parser.ParseElements;
import com.github.kshashov.telegram.api.MessageType;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.Date;

@BotController
public class MessageController implements TelegramMvcController, Controller {

    private final BotConfig botConfig;

    private final HelpCommand helpCommand;

    private final StartCommand startCommand;

    private final LoadItemsByTitleCommand loadItemsByTitleCommand;

    private final RssUrlValidationCommand rssUrlValidationCommand;

    private final ParseElements parseElements;

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

    @BotRequest(value = "/start", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest<?, ?> sendStartMessage(Update update) {
        if (update.callbackQuery() != null) {
            return startCommand.displayStartMenu(
                    update.callbackQuery().message().chat().id(),
                    update.callbackQuery().message().messageId());
        }
        return startCommand.execute(update.message());
    }

    @BotRequest(value = "/help")
    public BaseRequest<SendMessage, SendResponse> sendHelpMessage(Update update) {
        return helpCommand.execute(update.message());
    }

    @MessageRequest
    public BaseRequest<?, ?> handle(Update update) {
        String message = update.message().text();
        long chatId = update.message().chat().id();

        handleMessageLog(update);

        if (SubscribeCommand.hasEntered()) {
            botConfig.getTelegramBot().execute(new DeleteMessage(chatId, update.message().replyToMessage().messageId()));
            return rssUrlValidationCommand.execute(update.message());
        } else if (EnteringItemTitleCommand.hasEntered()) {
            // todo parse in loadItemsByTitleCommand
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
