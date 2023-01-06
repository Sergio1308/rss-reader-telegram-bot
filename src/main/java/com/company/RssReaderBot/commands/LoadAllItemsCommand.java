package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.inlinekeyboard.LoadAllItemsInlineKeyboard;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.BaseResponse;

public class LoadAllItemsCommand extends LoadItemsByTitleCommand implements Command<Long, Integer> {

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Long chatId, Integer messageId) {
        LoadAllItemsInlineKeyboard loadAllItemsInlineKeyboard = new LoadAllItemsInlineKeyboard();
        InlineKeyboardMarkup markupInline = loadAllItemsInlineKeyboard.createInlineKeyboard();
        return new EditMessageText(chatId, messageId, getAnswer()).replyMarkup(markupInline);
    }
}
