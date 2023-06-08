package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.company.RssReaderBot.models.ItemsList;
import com.company.RssReaderBot.models.ItemsPagination;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class LoadItemsByTitleInlineKeyboard implements InlineKeyboardCreator {

    public final ItemsPagination itemsPagination;

    private final PaginationInlineKeyboard paginationInlineKeyboard;

    private final ItemsList itemsList;

    @Getter @Setter
    private String callbackData;

    public LoadItemsByTitleInlineKeyboard(ItemsPagination itemsPagination,
                                          PaginationInlineKeyboard paginationInlineKeyboard,
                                          ItemsList itemsList) {
        this.itemsPagination = itemsPagination;
        this.paginationInlineKeyboard = paginationInlineKeyboard;
        this.itemsList = itemsList;
    }

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        if (itemsList.getItemsList().isEmpty()) { // if no results were found for the query
            return new InlineKeyboardMarkup(
                    new InlineKeyboardButton("Cancel").callbackData(CallbackDataConstants.GET_ITEMS)
            );
        }
        itemsPagination.toSplit();
        Optional<String> callbackDataButton = Optional.ofNullable(this.callbackData);
        return paginationInlineKeyboard.createButton(
                paginationInlineKeyboard.execute(itemsPagination.getStartButtonsIndex()),
                "Back",
                callbackDataButton.orElse(CallbackDataConstants.GET_ITEMS)
        );
    }

    public InlineKeyboardMarkup createInlineKeyboard(String callbackData) {
        int buttonIndex = 0;
        try {
            String pressedButtonIndex = callbackData.substring(callbackData.indexOf("_") + 1);
            buttonIndex = Integer.parseInt(pressedButtonIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paginationInlineKeyboard.createButton(
                paginationInlineKeyboard.execute(buttonIndex),
                "Back",
                CallbackDataConstants.GET_ITEMS
        );
    }
}
