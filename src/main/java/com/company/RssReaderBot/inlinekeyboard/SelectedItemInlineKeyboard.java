package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.handlers.CallbackVars;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class SelectedItemInlineKeyboard implements InlineKeyboardCreator {
    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rowInline = createInlineKeyboardButtonRowInline(
                createInlineKeyboardButton
                        ("Description & download link", CallbackVars.ITEM_DESCRIPTION),
                createInlineKeyboardButton
                        ("Get other details", CallbackVars.ITEM_OTHER_DETAILS)
        );
        List<InlineKeyboardButton> rowInlineWithBackButton = createInlineKeyboardButtonRowInline(
                createInlineKeyboardButton
                        ("Back", CallbackVars.RETURN_LOAD_BY_TITLE)
        );
        List<List<InlineKeyboardButton>> rowList = createInlineKeyboardButtonRowList(rowInline);
        rowList.add(rowInlineWithBackButton);
        markupInline.setKeyboard(rowList);
        return markupInline;
    }
}
