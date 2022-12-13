package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.handlers.CallbackVars;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class MenuItemsLoaderInlineKeyboard implements InlineKeyboardCreator {

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rowInline = createInlineKeyboardButtonRowInline(
                createInlineKeyboardButton("Load all items", CallbackVars.LOAD_ALL_ITEMS),
                createInlineKeyboardButton("Load items by title", CallbackVars.LOAD_BY_TITLE),
                createInlineKeyboardButton("Load items by date", CallbackVars.LOAD_BY_DATE)
        );
        List<InlineKeyboardButton> rowInline2 = createInlineKeyboardButtonRowInline(
                createInlineKeyboardButton("Personal menu / Settings", CallbackVars.PERSONAL_MENU));

        List<List<InlineKeyboardButton>> rowList = createInlineKeyboardButtonRowList(rowInline);
        rowList.add(rowInline2);
        markupInline.setKeyboard(rowList);
        return markupInline;
    }
}
