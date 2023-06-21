package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.db.entities.RssFeed;
import com.company.RssReaderBot.inlinekeyboard.GetItemsInlineKeyboard;
import com.company.RssReaderBot.db.services.RssFeedService;
import com.company.RssReaderBot.inlinekeyboard.RssFeedsInlineKeyboard;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ForceReply;
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
public class GetItemsCommand implements Command<Message> {

    private final RssFeedService rssFeedService;

    private final RssFeedsInlineKeyboard feedsInlineKeyboard;

    private final GetItemsInlineKeyboard getItemsInlineKeyboard;

    @Getter @Setter
    private List<RssFeed> feedList;

    public GetItemsCommand(RssFeedService rssFeedService, RssFeedsInlineKeyboard feedsInlineKeyboard,
                           GetItemsInlineKeyboard getItemsInlineKeyboard) {
        this.rssFeedService = rssFeedService;
        this.feedsInlineKeyboard = feedsInlineKeyboard;
        this.getItemsInlineKeyboard = getItemsInlineKeyboard;
    }

    private String getAnswer() {
        return "I found some subscribed feeds!" +
                "\n\nSelect one by pressing scroll buttons◀️▶️, " +
                "then press other option button below to get items: all, by title or by date" +
                "\n\nSelected feed:";
    }

    @Override
    public BaseRequest<?, ?> execute(Message message) {
        long chatId = message.chat().id();
        feedList = rssFeedService.getAllFeeds(chatId);
        if (feedList.isEmpty()) {
            return new SendMessage(
                    chatId,
                    "use this command with arguments, using key word specify which way you want to get items: " +
                            "all|title|date then write url.\nExample: <code>/get all url</code>")
                    .parseMode(ParseMode.HTML)
                    .replyMarkup(new ForceReply(true).inputFieldPlaceholder("all|title|date url"));
        }
        feedsInlineKeyboard.setFeedList(feedList);
        InlineKeyboardMarkup markupInline = getItemsInlineKeyboard.createInlineKeyboard();
        return new EditMessageText(chatId, message.messageId(), getAnswer())
                .replyMarkup(markupInline)
                .parseMode(ParseMode.HTML);
    }
}
