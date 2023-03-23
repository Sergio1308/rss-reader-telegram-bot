package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.handlers.CallbackVars;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.List;

/**
 * Entering RSS URL (only during the first launch of the bot and the user is not in DB yet)
 */
public class StartMenuInlineKeyboard implements InlineKeyboardCreator {

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton("What is RSS? [FAQ]").callbackData(CallbackVars.RSS_FAQ)
        );
    }
}
