package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.config.BotConfig;
import com.company.RssReaderBot.controllers.MessageController;
import com.company.RssReaderBot.controllers.core.BotState;
import com.company.RssReaderBot.db.entities.RssFeed;
import com.company.RssReaderBot.db.entities.UserDB;
import com.company.RssReaderBot.db.repositories.UserSettingsRepository;
import com.company.RssReaderBot.db.services.RssFeedService;
import com.company.RssReaderBot.db.services.UserService;
import com.company.RssReaderBot.models.RssFeedCheckerRegistry;
import com.company.RssReaderBot.utils.ElementDisplayFormatter;
import com.company.RssReaderBot.utils.RssFeedChecker;
import com.company.RssReaderBot.utils.parser.RssUrlValidator;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Component;

@Component
public class RssUrlValidationCommand implements Command<Message> {

    private final BotConfig botConfig;

    private final RssFeedService rssFeedService;

    private final UserService userService;

    private final RssUrlValidator rssUrlValidator;

    private final RssFeedCheckerRegistry feedCheckerRegistry;

    private final UserSettingsRepository userSettingsRepository;

    public RssUrlValidationCommand(BotConfig botConfig, RssFeedService rssFeedService, UserService userService,
                                   RssUrlValidator rssUrlValidator, RssFeedCheckerRegistry feedCheckerRegistry,
                                   UserSettingsRepository userSettingsRepository) {
        this.botConfig = botConfig;
        this.rssFeedService = rssFeedService;
        this.userService = userService;
        this.rssUrlValidator = rssUrlValidator;
        this.feedCheckerRegistry = feedCheckerRegistry;
        this.userSettingsRepository = userSettingsRepository;
    }

    public static String invalidUrlAnswer(String validatedRssUrl) {
        return validatedRssUrl + "\n\n▶Send me a valid URL again\uD83D\uDC47" +
                "\n▶or press Cancel button above\uD83D\uDC46" +
                "\n\uD83D\uDCA1Hint: you can use /help for more info";
    }

    public BaseRequest<SendMessage, SendResponse> execute(Message message) {
        Long chatId = message.chat().id();
        String validatedRssUrl = rssUrlValidator.validateRssUrl(message.text());
        botConfig.getTelegramBot().execute(new DeleteMessage(chatId, message.messageId()));
        if (rssUrlValidator.isValid()) {
            MessageController.getUserStates().put(chatId, BotState.NONE);
            return subscribeToRssFeed(message, validatedRssUrl);
        } else {
            return new SendMessage(
                    chatId,
                    invalidUrlAnswer(validatedRssUrl)
            );
        }
    }

    public BaseRequest<SendMessage, SendResponse> subscribeToRssFeed(Message message, String validatedRssUrl) {
        Long chatId = message.chat().id();
        UserDB userDB = userService.findUser(chatId);
        if (rssFeedService.hasSubscribedFeed(userDB, validatedRssUrl)) {
            return new SendMessage(chatId,
                    "Error! You are already subscribed to this feed.");
        }
        rssFeedService.addSubscribedFeed(userDB, validatedRssUrl);
        RssFeed addedFeed = rssFeedService.getCurrentlyAddedFeed();

        RssFeedChecker feedChecker = new RssFeedChecker(
                botConfig, message, userSettingsRepository, addedFeed
        );
        feedCheckerRegistry.addRssFeedChecker(addedFeed.getId(), feedChecker);
        feedChecker.startChecking();

        return new SendMessage(chatId,
                "Subscribed successfully!\nYou are now following "
                        + ElementDisplayFormatter.createHyperlink(addedFeed.getUrl(), addedFeed.getTitle()))
                .parseMode(ParseMode.HTML);
    }
}
