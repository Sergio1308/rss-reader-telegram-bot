package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.handlers.CallbackVars;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.List;

public class MenuItemsLoaderInlineKeyboard implements InlineKeyboardCreator {

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Load all items")
                                .callbackData(CallbackVars.LOAD_ALL_ITEMS),
                        new InlineKeyboardButton("Load items by title")
                                .callbackData(CallbackVars.LOAD_BY_TITLE),
                        new InlineKeyboardButton("Load items by date")
                                .callbackData(CallbackVars.LOAD_BY_DATE)
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Personal menu / Settings")
                                .callbackData(CallbackVars.PERSONAL_MENU)
                }
        );
    }
}
