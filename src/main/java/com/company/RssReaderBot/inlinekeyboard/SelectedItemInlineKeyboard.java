package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.controllers.CallbackQueryConstants;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public class SelectedItemInlineKeyboard implements InlineKeyboardCreator {
    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {

        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Save to favorites / Remove from favorites")
                                .callbackData("empty"),
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Back")
                                .callbackData(CallbackQueryConstants.RETURN_LOAD_BY_TITLE)
                }
        );
    }
}
