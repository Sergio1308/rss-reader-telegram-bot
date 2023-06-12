package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.company.RssReaderBot.db.entities.UserDB;
import com.company.RssReaderBot.db.repositories.UserSettingsRepository;
import com.company.RssReaderBot.models.ItemModel;
import com.company.RssReaderBot.models.ItemsList;
import com.company.RssReaderBot.inlinekeyboard.SelectedItemInlineKeyboard;
import com.company.RssReaderBot.utils.ElementDisplayFormatter;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for selecting a specific item
 * (by pressing on the button) from items-list.
 */
@Component
public class SelectItemCommand implements Command<Message> {

    @Getter @Setter
    private String callData;

    @Getter @Setter
    private ItemModel currentlySelectedItemModel;

    @Getter
    private final SelectedItemInlineKeyboard selectedItemInlineKeyboard;

    private final ItemsList itemsList;

    private final UserSettingsRepository userSettingsRepository;

    @Getter @Setter
    private String stateMenu;

    @Getter @Setter
    private UserDB user;

    public SelectItemCommand(SelectedItemInlineKeyboard selectedItemInlineKeyboard, ItemsList itemsList,
                             UserSettingsRepository userSettingsRepository) {
        this.selectedItemInlineKeyboard = selectedItemInlineKeyboard;
        this.itemsList = itemsList;
        this.userSettingsRepository = userSettingsRepository;
    }

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Message message) {
        // callDate contains the title of the episode selected by the user by clicking on the button
        if (!callData.equals(CallbackDataConstants.SHOW_ITEM)) { // equals only when user pressed inline button
            String currentEpisodeTitle = callData.substring(CallbackDataConstants.SHOW_ITEM.length());
            // get item object instantly from HashMap (O1)
            currentlySelectedItemModel = itemsList.getItemsMap().get(currentEpisodeTitle);
        }
        selectedItemInlineKeyboard.setUser(user);
        selectedItemInlineKeyboard.setCurrentItem(currentlySelectedItemModel);
        InlineKeyboardMarkup markupInline = selectedItemInlineKeyboard.createInlineKeyboard();

        return new EditMessageText(message.chat().id(), message.messageId(), ElementDisplayFormatter.displayElement(
                user.getUserid(), userSettingsRepository, currentlySelectedItemModel
        )).replyMarkup(markupInline).parseMode(ParseMode.HTML);
    }
}
