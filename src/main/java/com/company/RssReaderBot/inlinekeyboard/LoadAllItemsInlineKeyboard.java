package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.company.RssReaderBot.models.ItemsPagination;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Component;

@Component
public class LoadAllItemsInlineKeyboard implements InlineKeyboardCreator {

    public final ItemsPagination itemsPagination;

    private final PaginationInlineKeyboard paginationInlineKeyboard;

    public LoadAllItemsInlineKeyboard(ItemsPagination itemsPagination, PaginationInlineKeyboard paginationInlineKeyboard) {
        this.itemsPagination = itemsPagination;
        this.paginationInlineKeyboard = paginationInlineKeyboard;
    }

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        itemsPagination.toSplit();
        return paginationInlineKeyboard.createButton(
                paginationInlineKeyboard.execute(itemsPagination.getStartButtonsIndex()),
                "Back",
                CallbackDataConstants.GET_ITEMS
        );
    }
}
