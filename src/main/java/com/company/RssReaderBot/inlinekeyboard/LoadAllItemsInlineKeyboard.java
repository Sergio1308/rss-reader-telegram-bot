package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.entities.ItemsPagination;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class LoadAllItemsInlineKeyboard extends LoadItemsByTitleInlineKeyboard implements InlineKeyboardCreator {

    public final ItemsPagination itemsPagination = ItemsPagination.getInstance();

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        /*InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rowInline = createInlineKeyboardButtonRowInline(
                createInlineKeyboardButton("Cancel", CallbackVars.START_MENU)
        );
        List<List<InlineKeyboardButton>> rowList = createInlineKeyboardButtonRowList(rowInline);
        markupInline.setKeyboard(rowList);

        return markupInline;*/
        itemsPagination.toSplit();
        return executeCreation(itemsPagination.getStartButtonsIndex());
    }
}
