package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.commands.SelectItemCommand;
import com.company.RssReaderBot.handlers.CallbackVars;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public class DescriptionAndLinkInlineKeyboard implements InlineKeyboardCreator {
    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{new InlineKeyboardButton("Open download link")
                        .url(SelectItemCommand.getCurrentlySelectedEpisode().getAudio().getUrl())},
                new InlineKeyboardButton[]{new InlineKeyboardButton("Back")
                        .callbackData(CallbackVars.SELECTED_BY_TITLE_CALLBACK)}
        );
    }
}
