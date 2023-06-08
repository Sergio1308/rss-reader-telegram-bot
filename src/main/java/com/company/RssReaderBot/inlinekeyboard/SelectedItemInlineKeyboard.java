package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.company.RssReaderBot.db.entities.UserDB;
import com.company.RssReaderBot.db.services.FavoriteItemService;
import com.company.RssReaderBot.models.ItemModel;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class SelectedItemInlineKeyboard implements InlineKeyboardCreator {

    private final FavoriteItemService favoriteItemService;

    @Getter @Setter
    private UserDB user;

    @Getter @Setter
    private ItemModel currentItem;

    public SelectedItemInlineKeyboard(FavoriteItemService favoriteItemService) {
        this.favoriteItemService = favoriteItemService;
    }

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton backButton = new InlineKeyboardButton("Back")
                .callbackData(CallbackDataConstants.ITEMS_LIST);
        if (!favoriteItemService.hasItem(user, currentItem.getTitle())) {
            return markup.addRow(new InlineKeyboardButton("Add to favorites")
                    .callbackData(CallbackDataConstants.ADD_TO_FAVORITES)).addRow(backButton);
        }
        return markup.addRow(new InlineKeyboardButton("Remove from favorites")
                .callbackData(CallbackDataConstants.REMOVE_FROM_FAVORITES)).addRow(backButton);
    }
}
