package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.handlers.CallbackVars;
import com.company.RssReaderBot.inlinekeyboard.LoadItemsByTitleInlineKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class TurnToFirstOrLastPageCommand extends LoadItemsByTitleCommand implements CommandCallback<Long, String> {

    @Override
    public void execute(Long chatId, Long messageId, String callData) {
        LoadItemsByTitleInlineKeyboard loadItemsByTitleInlineKeyboard = new LoadItemsByTitleInlineKeyboard();
        InlineKeyboardMarkup markupInline;

        loadItemsByTitleInlineKeyboard.itemsPagination.changePaginationIndex(callData);
        loadItemsByTitleInlineKeyboard.itemsPagination.calculateIndex();

        if (callData.equals(CallbackVars.FIRST_PAGE)) {
            markupInline = loadItemsByTitleInlineKeyboard.executeCreation(0);
        } else {
            markupInline = loadItemsByTitleInlineKeyboard.executeCreation(
                    loadItemsByTitleInlineKeyboard.itemsPagination.getChunkedItemList().size() - 1
            );
        }
        messageSender.sendEditMessageText(chatId, messageId, getAnswer(), markupInline);
    }
}
