package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.db.models.RssFeed;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.GetItemsInlineKeyboard;
import com.company.RssReaderBot.services.FeedService;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetItemsCommand implements Command<Message> {

    private final FeedService feedService;

    public GetItemsCommand(FeedService feedService) {
        this.feedService = feedService;
    }

    private String getAnswer() {
        return "I found some subscribed feeds, select one by pressing the button, " +
                "then press other option button below to get items: all, by title, date OR" +
                "\nif you want get items from another feed use command <code>/get</code> with one of this arguments:" +
                "\nall|title|date, then write URL of your feed. For example, " +
                "<code>/get title https://example.com/rss.xml</code> - if you want to get items by title";
    }

    @Override
    public BaseRequest<?, ?> execute(Message message) {
        long chatId = message.chat().id();
        List<RssFeed> feedList = feedService.getAllFeeds(chatId);
        if (feedList.isEmpty()) {
            return new SendMessage(
                    chatId,
                    "use this command with arguments, using key word specify which way you want to get items: " +
                            "all|title|date then write url.\nExample: <code>/get all url</code>")
                    .parseMode(ParseMode.HTML)
                    .replyMarkup(new ForceReply(true).inputFieldPlaceholder("all|title|date url"));
        }
        InlineKeyboardCreator inlineKeyboardCreator = new GetItemsInlineKeyboard(feedList);
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        return new EditMessageText(chatId, message.messageId(), getAnswer()).replyMarkup(markupInline).parseMode(ParseMode.HTML);
    }
}
