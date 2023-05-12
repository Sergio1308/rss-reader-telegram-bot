package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.company.RssReaderBot.db.entities.RssFeed;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetItemsInlineKeyboard implements InlineKeyboardCreator {

    private final List<RssFeed> feedList;

    public GetItemsInlineKeyboard(List<RssFeed> feedList) {
        this.feedList = feedList;
    }

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        RssFeedsInlineKeyboard feedsInlineKeyboard = new RssFeedsInlineKeyboard(feedList);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton[] feedsButtons = feedsInlineKeyboard.createFeedListButton();

//        InlineKeyboardButton feedButton = new InlineKeyboardButton("Selected feed: " + feedList.get(0));
        InlineKeyboardButton[] getItemsButtons = new InlineKeyboardButton[] {
                new InlineKeyboardButton("Get all items")
                        .callbackData(CallbackDataConstants.LOAD_ALL_ITEMS),
                new InlineKeyboardButton("Get items by title")
                        .callbackData(CallbackDataConstants.LOAD_BY_TITLE),
                new InlineKeyboardButton("Get items by date")
                        .callbackData(CallbackDataConstants.LOAD_BY_DATE)
        };
        markup.addRow(feedsButtons).addRow(new InlineKeyboardButton(" ").callbackData("null")).addRow(getItemsButtons)
                .addRow(new InlineKeyboardButton("Back to main menu")
                        .callbackData(CallbackDataConstants.START_MENU));
        return markup;
    }
}
