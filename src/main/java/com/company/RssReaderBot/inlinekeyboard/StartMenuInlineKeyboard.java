package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.handlers.CallbackVars;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

/**
 * Entering RSS URL (only during the first launch of the bot and the user is not in DB yet)
 */
public class StartMenuInlineKeyboard implements InlineKeyboardCreator {

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rowInline = createInlineKeyboardButtonRowInline(
                createInlineKeyboardButton("What is RSS? [FAQ]", CallbackVars.RSS_FAQ)
        );
        List<List<InlineKeyboardButton>> rowList = createInlineKeyboardButtonRowList(rowInline);
        markupInline.setKeyboard(rowList);
        return markupInline;
    }
}
