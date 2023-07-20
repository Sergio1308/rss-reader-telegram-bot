package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.controllers.MessageController;
import com.company.RssReaderBot.controllers.core.BotState;
import com.company.RssReaderBot.inlinekeyboard.EnteringItemTitleInlineKeyboard;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.BaseResponse;
import lombok.Getter;

public class UserInputCommand implements Command<Message> {

    @Getter
    private final String answer;

    public UserInputCommand(String answer) {
        this.answer = answer;
    }

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Message message) {
        InlineKeyboardCreator inlineKeyboardCreator = new EnteringItemTitleInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        if (answer.startsWith("Enter a correct title")) {
            MessageController.getUserStates().put(message.chat().id(), BotState.ENTER_TITLE);
        } else if (answer.startsWith("Enter a correct date")) {
            MessageController.getUserStates().put(message.chat().id(), BotState.ENTER_DATE);
        }
        return new EditMessageText(message.chat().id(), message.messageId(), answer).replyMarkup(markupInline);
    }
}
