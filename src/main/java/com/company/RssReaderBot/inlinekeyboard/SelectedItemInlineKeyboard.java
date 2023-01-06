package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.handlers.CallbackVars;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.List;

public class SelectedItemInlineKeyboard implements InlineKeyboardCreator {
    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {

        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Description & download link")
                                .callbackData(CallbackVars.ITEM_DESCRIPTION),
                        new InlineKeyboardButton("Get other details")
                                .callbackData(CallbackVars.ITEM_OTHER_DETAILS)},
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Back")
                                .callbackData(CallbackVars.RETURN_LOAD_BY_TITLE)
                }
        );
    }
}
