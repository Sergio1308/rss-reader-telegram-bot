package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.handlers.CallbackVars;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.List;

public class EnteringItemTitleInlineKeyboard implements InlineKeyboardCreator {

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton("Cancel").callbackData(CallbackVars.MAIN_MENU)
        );
    }
}
