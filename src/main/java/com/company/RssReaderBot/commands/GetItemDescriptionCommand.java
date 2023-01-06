package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.inlinekeyboard.DescriptionAndLinkInlineKeyboard;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.BaseResponse;

public class GetItemDescriptionCommand implements Command<Long, Integer> {

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Long chatId, Integer messageId) {
        InlineKeyboardCreator inlineKeyboardCreator = new DescriptionAndLinkInlineKeyboard();
        String answer = SelectItemCommand.getCurrentlySelectedEpisode().getDescription();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        return new EditMessageText(chatId, messageId, answer).replyMarkup(markupInline);
    }
}
