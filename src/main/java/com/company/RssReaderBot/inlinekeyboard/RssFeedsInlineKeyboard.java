package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.controllers.CallbackQueryConstants;
import com.company.RssReaderBot.db.models.RssFeed;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import lombok.Getter;

import java.util.List;

/**
 * Display all subscribed feeds.
 */
public class RssFeedsInlineKeyboard implements InlineKeyboardCreator {

    @Getter
    private List<RssFeed> feedList;

    public RssFeedsInlineKeyboard(List<RssFeed> feedList) {
        this.feedList = feedList;
    }

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton backButton = new InlineKeyboardButton("Back")
                .callbackData(CallbackQueryConstants.HIDE_MESSAGE);
        if (feedList.isEmpty()) {
            return markup.addRow(new InlineKeyboardButton("Subscribe to the feed")
                    .callbackData(CallbackQueryConstants.SUBSCRIBE)).addRow(backButton);
        }
        InlineKeyboardButton[] buttons = new InlineKeyboardButton[feedList.size()];
        for (int i = 0; i < feedList.size(); i++) {
            RssFeed feed = feedList.get(i);
            buttons[i] = new InlineKeyboardButton(feed.getTitle())
                    .callbackData(CallbackQueryConstants.FEED_BUTTON + feed.getId());
        }
        return markup.addRow(buttons).addRow(backButton);
    }
}
