package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.entities.Item;
import com.company.RssReaderBot.entities.ItemsList;
import com.company.RssReaderBot.handlers.CallbackVars;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.SelectedItemInlineKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

/**
 * This class is responsible for selecting a specific item
 * (by pressing on the button) from items-list.
 */
public class SelectItemCommand implements Command<Long> {

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

    @Override
    public void execute(Long chatId, Long messageId) {
        if (callData == null) throw new NullPointerException();
        // here callDate contains the title of the episode selected by the user by clicking on the button
        if (!callData.equals(CallbackVars.SELECTED_BY_TITLE_CALLBACK)) { // equals only when user pressed inline button
            String currentEpisodeTitle = callData.substring(CallbackVars.SELECTED_BY_TITLE_CALLBACK.length());
            // get item object instantly from HashMap (O1)
            currentlySelectedItem = ItemsList.getItemsMap().get(currentEpisodeTitle);
        }
        InlineKeyboardCreator inlineKeyboardCreator = new SelectedItemInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        String answer = "Selected item with title:\n" + currentlySelectedItem.getTitle();
        messageSender.sendEditMessageText(chatId, messageId, answer, markupInline);
    }
}
