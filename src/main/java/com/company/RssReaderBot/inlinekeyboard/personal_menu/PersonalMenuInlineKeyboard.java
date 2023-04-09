package com.company.RssReaderBot.inlinekeyboard.personal_menu;

import com.company.RssReaderBot.controllers.CallbackQueryConstants;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public class PersonalMenuInlineKeyboard implements InlineKeyboardCreator {

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{new InlineKeyboardButton("My Favorites")
                        .callbackData(CallbackQueryConstants.FAVORITES)},
                new InlineKeyboardButton[]{new InlineKeyboardButton("Change my RSS URL")
                        .callbackData(CallbackQueryConstants.CHANGE_RSS_URL)},
                new InlineKeyboardButton[]{new InlineKeyboardButton("Alerts & Saved title for item parsing")
                        .callbackData(CallbackQueryConstants.ALERTS_AND_TITLE_CONF)},
                new InlineKeyboardButton[]{new InlineKeyboardButton("Update my user data")
                        .callbackData(CallbackQueryConstants.UPDATE_USER_DATA)},
                new InlineKeyboardButton[]{new InlineKeyboardButton("Back to main menu")
                        .callbackData(CallbackQueryConstants.PARSING_ELEMENTS_MENU)}
        );
    }
}
