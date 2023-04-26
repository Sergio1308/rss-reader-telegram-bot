package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.inlinekeyboard.LoadAllItemsInlineKeyboard;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.stereotype.Component;

@Component
public class LoadAllItemsCommand extends LoadItemsByTitleCommand implements Command<Message> {

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Message message) {
        LoadAllItemsInlineKeyboard loadAllItemsInlineKeyboard = new LoadAllItemsInlineKeyboard();
        InlineKeyboardMarkup markupInline = loadAllItemsInlineKeyboard.createInlineKeyboard();
        return new EditMessageText(message.chat().id(), message.messageId(), getAnswer()).replyMarkup(markupInline);
    }
}
