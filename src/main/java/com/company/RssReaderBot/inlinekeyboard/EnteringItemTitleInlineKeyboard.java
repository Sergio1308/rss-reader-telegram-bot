package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.handlers.CallbackVars;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class EnteringItemTitleInlineKeyboard implements InlineKeyboardCreator {

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rowInline = createInlineKeyboardButtonRowInline(
                createInlineKeyboardButton("Cancel", CallbackVars.MAIN_MENU)
        );
        List<List<InlineKeyboardButton>> rowList = createInlineKeyboardButtonRowList(rowInline);
        markupInline.setKeyboard(rowList);

        return markupInline;
    }
}
