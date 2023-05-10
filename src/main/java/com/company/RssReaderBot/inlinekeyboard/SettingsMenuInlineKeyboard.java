package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.company.RssReaderBot.db.entities.UserSettings;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public class SettingsMenuInlineKeyboard implements InlineKeyboardCreator {

    private final UserSettings userSettings;

    public SettingsMenuInlineKeyboard(UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        // save only when exit menu
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("My Favorites")
                                .callbackData(CallbackDataConstants.FAVORITES)
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Feed settings")
                        .callbackData(CallbackDataConstants.FEED_SETTINGS)
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("⚙️Display title: " + userSettings.isDisplayTitle())
                                .callbackData(CallbackDataConstants.DISPLAY_OPTIONS.get(0))
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("⚙️Display description: " + userSettings.isDisplayDescription())
                                .callbackData(CallbackDataConstants.DISPLAY_OPTIONS.get(1))
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("⚙️Display date: " + userSettings.isDisplayDate())
                                .callbackData(CallbackDataConstants.DISPLAY_OPTIONS.get(2))
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("⚙️Display media: " + userSettings.isDisplayMedia())
                                .callbackData(CallbackDataConstants.DISPLAY_OPTIONS.get(3))
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("⚙️Display link to source: " + userSettings.isDisplayLink())
                                .callbackData(CallbackDataConstants.DISPLAY_OPTIONS.get(4))
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Back")
                                .callbackData(CallbackDataConstants.START_MENU)
                }
        );
    }
}
