package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.company.RssReaderBot.entities.Item;
import com.company.RssReaderBot.entities.ItemsList;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.SelectedItemInlineKeyboard;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for selecting a specific item
 * (by pressing on the button) from items-list.
 */
@Component
public class SelectItemCommand implements Command<Message> {

    private static String callData;

    private static Item currentlySelectedItem;

    public static Item getCurrentlySelectedEpisode() {
        return currentlySelectedItem;
    }

    public static String getCallData() {
        return callData;
    }

    public static void setCallData(String callData) {
        SelectItemCommand.callData = callData;
    }

    private String getAnswer() {
        final String URL_HREF_OPEN_TAG = "<a href='";
        return "<b>" + currentlySelectedItem.getTitle() + "</b>\n\n"
                + currentlySelectedItem.getDescription() + "\n\n"
                + currentlySelectedItem.getPubDate() + "\n\n"
                + URL_HREF_OPEN_TAG + currentlySelectedItem.getMediaUrl() + "'>Media</a>" + "\n\n"
                + "Source: " + URL_HREF_OPEN_TAG + currentlySelectedItem.getGuid()
                + "'>" + "RssParser.getChannelTitleStatic()" + "</a>";
    }

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Message message) {
        if (callData == null) throw new NullPointerException();  // todo
        // here callDate contains the title of the episode selected by the user by clicking on the button
        if (!callData.equals(CallbackDataConstants.SHOW_ITEM)) { // equals only when user pressed inline button
            String currentEpisodeTitle = callData.substring(CallbackDataConstants.SHOW_ITEM.length());
            // get item object instantly from HashMap (O1)
            currentlySelectedItem = ItemsList.getItemsMap().get(currentEpisodeTitle);
        }
        InlineKeyboardCreator inlineKeyboardCreator = new SelectedItemInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();

        return new EditMessageText(message.chat().id(), message.messageId(), getAnswer())
                .replyMarkup(markupInline)
                .parseMode(ParseMode.HTML);
    }
}
