package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Component;

@Component
public class GetItemsInlineKeyboard implements InlineKeyboardCreator {

    private final RssFeedsInlineKeyboard feedsInlineKeyboard;

    public GetItemsInlineKeyboard(RssFeedsInlineKeyboard feedsInlineKeyboard) {
        this.feedsInlineKeyboard = feedsInlineKeyboard;
    }

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton[] feedsButtons = feedsInlineKeyboard.createFeedListButton();
        markup.addRow(feedsButtons);
        createGetItemsButtons(markup);
        return markup;
    }

    public void createGetItemsButtons(InlineKeyboardMarkup inlineMarkup) {
        InlineKeyboardButton[] getItemsButtons = new InlineKeyboardButton[] {
                new InlineKeyboardButton("Get all items")
                        .callbackData(CallbackDataConstants.LOAD_ALL_ITEMS),
                new InlineKeyboardButton("Get items by title")
                        .callbackData(CallbackDataConstants.LOAD_BY_TITLE),
                new InlineKeyboardButton("Get items by date")
                        .callbackData(CallbackDataConstants.LOAD_BY_DATE)
        };
        inlineMarkup
                .addRow(getItemsButtons)
                .addRow(new InlineKeyboardButton("Back to main menu")
                        .callbackData(CallbackDataConstants.START_MENU));
    }
}
