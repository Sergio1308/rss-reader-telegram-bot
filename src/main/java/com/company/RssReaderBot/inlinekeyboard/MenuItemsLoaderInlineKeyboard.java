package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.controllers.CallbackQueryConstants;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public class MenuItemsLoaderInlineKeyboard implements InlineKeyboardCreator {

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Load all items")
                                .callbackData(CallbackQueryConstants.LOAD_ALL_ITEMS),
                        new InlineKeyboardButton("Load items by title")
                                .callbackData(CallbackQueryConstants.LOAD_BY_TITLE),
                        new InlineKeyboardButton("Load items by date")
                                .callbackData(CallbackQueryConstants.LOAD_BY_DATE)
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Personal menu / Settings")
                                .callbackData(CallbackQueryConstants.SETTINGS)
                }
        );
    }
}
