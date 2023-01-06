package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.MenuItemsLoaderInlineKeyboard;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;

public class LoadMainMenuCommand implements Command<Long, Integer> {

    private String getAnswer() {
        return "\tMain menu\nChoose what you need";
    }

    public BaseRequest<SendMessage, SendResponse> execute(Long chatId) {
        return sendStartMessage(chatId);
    }

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Long chatId, Integer messageId) {
        InlineKeyboardCreator inlineKeyboardCreator = new MenuItemsLoaderInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        return new EditMessageText(chatId, messageId, getAnswer()).replyMarkup(markupInline);
    }

    public BaseRequest<SendMessage, SendResponse> sendStartMessage(Long chatId) {
        InlineKeyboardCreator inlineKeyboardCreator = new MenuItemsLoaderInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        return new SendMessage(chatId, getAnswer()).replyMarkup(markupInline);
    }
}
