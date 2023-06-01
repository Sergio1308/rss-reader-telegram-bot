package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public class StartMenuInlineKeyboard implements InlineKeyboardCreator {
    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Subscribe to the feed")
                                .callbackData(CallbackDataConstants.SUBSCRIBE)
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Unsubscribe from the feed")
                                .callbackData(CallbackDataConstants.UNSUBSCRIBE)
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Process / research a specific feed")
                                .callbackData(CallbackDataConstants.GET_ITEMS)
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Settings")
                                .callbackData(CallbackDataConstants.SETTINGS_MENU)
                }
        );
    }
}
