package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.db.models.RssFeed;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.RssFeedsInlineKeyboard;
import com.company.RssReaderBot.services.FeedService;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UnsubscribeCommand implements Command<Message> {

    private final FeedService feedService;

    @Getter
    private String answer;

    public UnsubscribeCommand(FeedService feedService) {
        this.feedService = feedService;
    }

    @Override
    public BaseRequest<?, ?> execute(Message message) {
        List<RssFeed> feedList = feedService.getAllFeeds(message.chat().id());
        InlineKeyboardCreator inlineKeyboardCreator = new RssFeedsInlineKeyboard(feedList);
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        if (feedList.isEmpty()) {
            answer = "You don't have any subscribed feeds yet\uD83D\uDE05\nClick on the button to subscribe.";
        } else {
            answer = "Your subscriptions.\nClick on the button with the name of the feed to unsubscribe.";
        }
        return new SendMessage(message.chat().id(), answer).replyMarkup(markupInline);
    }
}
