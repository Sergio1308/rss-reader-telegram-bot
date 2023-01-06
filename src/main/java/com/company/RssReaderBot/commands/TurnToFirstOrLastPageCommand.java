package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.handlers.CallbackVars;
import com.company.RssReaderBot.inlinekeyboard.LoadItemsByTitleInlineKeyboard;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.BaseResponse;

public class TurnToFirstOrLastPageCommand extends LoadItemsByTitleCommand implements CommandCallback<Long, Integer, String> {

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Long chatId, Integer messageId, String callData) {
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
        return new EditMessageText(chatId, messageId, getAnswer()).replyMarkup(markupInline);
    }
}
