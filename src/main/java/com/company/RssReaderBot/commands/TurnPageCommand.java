package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.LoadItemsByTitleInlineKeyboard;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.BaseResponse;

public class TurnPageCommand extends LoadItemsByTitleCommand implements Command<Long, Integer> {

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Long chatId, Integer messageId) {
        InlineKeyboardCreator inlineKeyboardCreator = new LoadItemsByTitleInlineKeyboard();
        InlineKeyboardMarkup markup = inlineKeyboardCreator.createInlineKeyboard();
        return new EditMessageText(chatId, messageId, getAnswer()).replyMarkup(markup);
    }

    public void changePaginationIndex(String callData) {
        LoadItemsByTitleInlineKeyboard loadItemsByTitleInlineKeyboard = new LoadItemsByTitleInlineKeyboard();
        loadItemsByTitleInlineKeyboard.itemsPagination.changePaginationIndex(callData);
    }
}
