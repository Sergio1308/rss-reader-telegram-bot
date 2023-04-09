package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.controllers.CallbackQueryConstants;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public class EnteringItemTitleInlineKeyboard implements InlineKeyboardCreator {

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton("Cancel").callbackData(CallbackQueryConstants.PARSING_ELEMENTS_MENU)
        );
    }
}
