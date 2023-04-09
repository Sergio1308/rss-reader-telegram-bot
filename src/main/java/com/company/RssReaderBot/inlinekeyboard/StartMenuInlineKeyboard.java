package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.controllers.CallbackQueryConstants;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public class StartMenuInlineKeyboard implements InlineKeyboardCreator {
    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Subscribe to the feed")
                                .callbackData(CallbackQueryConstants.SUBSCRIBE)
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Unsubscribe from the feed")
                                .callbackData(CallbackQueryConstants.UNSUBSCRIBE)
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Process / research a specific feed")
                                .callbackData(CallbackQueryConstants.PARSING_ELEMENTS_MENU)
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Settings")
                                .callbackData(CallbackQueryConstants.SETTINGS)
                }
        );
    }
}
