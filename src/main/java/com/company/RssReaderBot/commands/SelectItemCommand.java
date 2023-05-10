package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.company.RssReaderBot.models.ItemModel;
import com.company.RssReaderBot.models.ItemsList;
import com.company.RssReaderBot.inlinekeyboard.SelectedItemInlineKeyboard;
import com.company.RssReaderBot.utils.DateUtils;
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

    @Getter
    private ItemModel currentlySelectedItemModel;

    private final SelectedItemInlineKeyboard selectedItemInlineKeyboard;

    private final DateUtils dateUtils;

    private final ItemsList itemsList;

    public SelectItemCommand(SelectedItemInlineKeyboard selectedItemInlineKeyboard, DateUtils dateUtils, ItemsList itemsList) {
        this.selectedItemInlineKeyboard = selectedItemInlineKeyboard;
        this.dateUtils = dateUtils;
        this.itemsList = itemsList;
    }

    private String getAnswer() {
        final String URL_HREF_OPEN_TAG = "<a href='";
        String title = currentlySelectedItemModel.getTitle();
        String date = dateUtils.formatDate(currentlySelectedItemModel.getPubDate());
        return "<b>" + title + "</b>\n\n"
                + currentlySelectedItemModel.getDescription() + "\n\n"
                + date + "\n\n"
                + URL_HREF_OPEN_TAG + currentlySelectedItemModel.getMediaUrl() + "'>Media</a>" + "\n\n"
                + "Source: " + URL_HREF_OPEN_TAG + currentlySelectedItemModel.getGuid()
                + "'>" + title + "</a>";
    }

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Message message) {
        // callDate contains the title of the episode selected by the user by clicking on the button
        if (!callData.equals(CallbackDataConstants.SHOW_ITEM)) { // equals only when user pressed inline button
            String currentEpisodeTitle = callData.substring(CallbackDataConstants.SHOW_ITEM.length());
            // get item object instantly from HashMap (O1)
            currentlySelectedItemModel = itemsList.getItemsMap().get(currentEpisodeTitle);
        }
        InlineKeyboardMarkup markupInline = selectedItemInlineKeyboard.createInlineKeyboard();

        return new EditMessageText(message.chat().id(), message.messageId(), getAnswer())
                .replyMarkup(markupInline)
                .parseMode(ParseMode.HTML);
    }
}
