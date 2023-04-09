package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.commands.personal_menu.CommandN;
import com.company.RssReaderBot.controllers.CallbackQueryConstants;
import com.company.RssReaderBot.db.models.RssFeed;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.RssFeedsInlineKeyboard;
import com.company.RssReaderBot.inlinekeyboard.StartMenuInlineKeyboard;
import com.company.RssReaderBot.services.FeedService;
import com.company.RssReaderBot.services.UserService;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UnsubscribeCommand implements CommandN<Message> {

    @Autowired
    private FeedService feedService;

    @Getter
    private String answer;

    @Override
    public BaseRequest<?, ?> execute(Message message) {
        List<RssFeed> feedList = feedService.getAllFeeds(message.chat().id());
        InlineKeyboardCreator inlineKeyboardCreator = new RssFeedsInlineKeyboard(feedList);
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        if (markupInline.inlineKeyboard()[0][0].callbackData().equals(CallbackQueryConstants.SUBSCRIBE)) {
            answer = "You don't have any subscribed feeds yet\uD83D\uDE05\nClick on the button to subscribe.";
        } else {
            answer = "Your subscriptions.\nClick on the button with the name of the feed to unsubscribe.";
        }
        return new SendMessage(message.chat().id(), answer).replyMarkup(markupInline);
    }
}
