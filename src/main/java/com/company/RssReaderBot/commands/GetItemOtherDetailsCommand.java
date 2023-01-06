package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.OtherDetailsInlineKeyboard;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.BaseResponse;

public class GetItemOtherDetailsCommand implements Command<Long, Integer> {

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Long chatId, Integer messageId) {
        InlineKeyboardCreator inlineKeyboardCreator = new OtherDetailsInlineKeyboard();
        String answer = SelectItemCommand.getCurrentlySelectedEpisode().getTitle() + "\nPublication date: " +
                SelectItemCommand.getCurrentlySelectedEpisode().getDate().getItemDate();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        return new EditMessageText(chatId, messageId, answer).replyMarkup(markupInline);
    }
}
