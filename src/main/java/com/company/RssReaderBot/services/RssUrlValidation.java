package com.company.RssReaderBot.services;

import com.company.RssReaderBot.commands.Command;
import com.company.RssReaderBot.commands.SubscribeCommand;
import com.company.RssReaderBot.config.BotConfig;
import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.company.RssReaderBot.db.models.UserDB;
import com.company.RssReaderBot.services.parser.RssUrlValidator;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RssUrlValidation implements Command<Message> {

    @Autowired
    private BotConfig botConfig;

    @Autowired
    private FeedService feedService;

    @Autowired
    private UserService userService;

    public BaseRequest<SendMessage, SendResponse> execute(Message message) {
        Long chatId = message.chat().id();
        RssUrlValidator urlValidator = new RssUrlValidator();
        String result = urlValidator.validateRssUrl(message.text());
        botConfig.getTelegramBot().execute(new DeleteMessage(chatId, message.messageId()));
        if (urlValidator.isValid()) {
            SubscribeCommand.setEntered(false);

            UserDB userDB = userService.findUser(chatId);
            feedService.addFeed(userDB, result);

            return new SendMessage(chatId,
                    "Subscribed successfully.\nYou are now following the feed: " + feedService.getFeedTitle());
        } else {
            return new SendMessage(
                    chatId,
                    result + "\n▶Send me a valid URL again by replying to this message\uD83D\uDC47" +
                    "\n▶or use /help for more info"
            ).replyMarkup(new ForceReply(true)
                    .inputFieldPlaceholder(CallbackDataConstants.SUB_FEED_SAMPLE)
                    .selective(true));
        }
    }
}
