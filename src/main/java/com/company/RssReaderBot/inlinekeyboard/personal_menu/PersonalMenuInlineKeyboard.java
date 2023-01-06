package com.company.RssReaderBot.inlinekeyboard.personal_menu;

import com.company.RssReaderBot.handlers.CallbackVars;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.List;

public class PersonalMenuInlineKeyboard implements InlineKeyboardCreator {

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{new InlineKeyboardButton("My Favorites")
                        .callbackData(CallbackVars.FAVORITES)},
                new InlineKeyboardButton[]{new InlineKeyboardButton("Change my RSS URL")
                        .callbackData(CallbackVars.CHANGE_RSS_URL)},
                new InlineKeyboardButton[]{new InlineKeyboardButton("Alerts & Saved title for item parsing")
                        .callbackData(CallbackVars.ALERTS_AND_TITLE_CONF)},
                new InlineKeyboardButton[]{new InlineKeyboardButton("Update my user data")
                        .callbackData(CallbackVars.UPDATE_USER_DATA)},
                new InlineKeyboardButton[]{new InlineKeyboardButton("Back to main menu")
                        .callbackData(CallbackVars.MAIN_MENU)}
        );
    }
}
