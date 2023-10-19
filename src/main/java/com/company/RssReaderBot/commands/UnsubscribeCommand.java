package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.db.entities.RssFeed;
import com.company.RssReaderBot.inlinekeyboard.RssFeedsInlineKeyboard;
import com.company.RssReaderBot.db.services.RssFeedService;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UnsubscribeCommand implements Command<Message> {

    private final RssFeedService rssFeedService;

    private final RssFeedsInlineKeyboard feedsInlineKeyboard;

    @Getter
    private String answer;

    public UnsubscribeCommand(RssFeedService rssFeedService, RssFeedsInlineKeyboard feedsInlineKeyboard) {
        this.rssFeedService = rssFeedService;
        this.feedsInlineKeyboard = feedsInlineKeyboard;
    }

    @Override
    public BaseRequest<?, ?> execute(Message message) {
        List<RssFeed> feedList = rssFeedService.getAllSubscribedFeeds(message.chat().id());
        if (feedList.isEmpty()) {
            answer = "You don't have any subscribed feeds yet\uD83D\uDE05\nClick on the button to subscribe.";
        } else {
            answer = "Your subscriptions.\nClick on the button with the name of the feed to unsubscribe.";
        }
        feedsInlineKeyboard.setFeedList(feedList);
        InlineKeyboardMarkup markupInline = feedsInlineKeyboard.createInlineKeyboard();
        return new SendMessage(message.chat().id(), answer).replyMarkup(markupInline);
    }
}
