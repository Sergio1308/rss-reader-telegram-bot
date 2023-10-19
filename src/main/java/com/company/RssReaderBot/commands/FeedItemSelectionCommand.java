package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.db.entities.RssFeed;
import com.company.RssReaderBot.inlinekeyboard.FeedItemSelectionInlineKeyboard;
import com.company.RssReaderBot.db.services.RssFeedService;
import com.company.RssReaderBot.inlinekeyboard.RssFeedsInlineKeyboard;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeedItemSelectionCommand implements Command<Message> {

    private final RssFeedService rssFeedService;

    private final RssFeedsInlineKeyboard feedsInlineKeyboard;

    private final FeedItemSelectionInlineKeyboard feedItemSelectionInlineKeyboard;

    @Getter @Setter
    private List<RssFeed> feedList;

    public FeedItemSelectionCommand(RssFeedService rssFeedService, RssFeedsInlineKeyboard feedsInlineKeyboard,
                                    FeedItemSelectionInlineKeyboard feedItemSelectionInlineKeyboard) {
        this.rssFeedService = rssFeedService;
        this.feedsInlineKeyboard = feedsInlineKeyboard;
        this.feedItemSelectionInlineKeyboard = feedItemSelectionInlineKeyboard;
    }

    private String getAnswer() {
        return "I found some subscribed feeds!" +
                "\n\nSelect one by pressing scroll buttons◀️▶️, " +
                "then press other option button below to get items: all, by title or by date" +
                "\n\nHint: You can also get items from any other RSS feed. To do this, use the commands:"+
                "\n/get_all\n/get_title\n/get_date"+
                "\nJust type \"/\" to see the description of the commands";
    }

    @Override
    public BaseRequest<?, ?> execute(Message message) {
        long chatId = message.chat().id();
        feedList = rssFeedService.getAllSubscribedFeeds(chatId);
        if (feedList.isEmpty()) {
            return new SendMessage(
                    chatId,
                    "You don't have any subscriptions to the RSS feed yet. " +
                            "You can get the elements of any feed using the following commands:" +
                            "\n/get_all\n/get_title\n/get_date" +
                            "\nJust type \"/\" to see the description of the commands")
                    .parseMode(ParseMode.HTML);
        }
        feedsInlineKeyboard.setFeedList(feedList);
        InlineKeyboardMarkup markupInline = feedItemSelectionInlineKeyboard.createInlineKeyboard();
        return new EditMessageText(chatId, message.messageId(), getAnswer())
                .replyMarkup(markupInline)
                .parseMode(ParseMode.HTML);
    }
}
