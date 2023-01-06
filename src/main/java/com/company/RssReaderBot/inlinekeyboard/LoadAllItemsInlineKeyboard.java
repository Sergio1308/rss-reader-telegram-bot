package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.entities.ItemsPagination;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public class LoadAllItemsInlineKeyboard extends LoadItemsByTitleInlineKeyboard implements InlineKeyboardCreator {

    public final ItemsPagination itemsPagination = ItemsPagination.getInstance();

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        itemsPagination.toSplit();
        return executeCreation(itemsPagination.getStartButtonsIndex());
    }
}
