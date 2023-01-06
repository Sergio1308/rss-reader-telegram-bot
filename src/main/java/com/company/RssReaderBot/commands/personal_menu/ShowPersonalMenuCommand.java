package com.company.RssReaderBot.commands.personal_menu;

import com.company.RssReaderBot.commands.Command;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.personal_menu.PersonalMenuInlineKeyboard;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.BaseResponse;

public class ShowPersonalMenuCommand implements Command<Long, Integer> {

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Long chatId, Integer messageId) {
        InlineKeyboardCreator inlineKeyboardCreator = new PersonalMenuInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        String answer = "Your Personal Menu & Settings";
        return new EditMessageText(chatId, messageId, answer).replyMarkup(markupInline);
    }
}
