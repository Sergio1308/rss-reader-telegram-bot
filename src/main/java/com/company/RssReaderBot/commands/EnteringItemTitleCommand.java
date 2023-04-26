package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.inlinekeyboard.EnteringItemTitleInlineKeyboard;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.stereotype.Component;

@Component
public class EnteringItemTitleCommand implements Command<Message> {

    private static boolean hasEntered;

    public static boolean hasEntered() {
        return hasEntered;
    }

    public static void setEntered(boolean hasEntered) {
        EnteringItemTitleCommand.hasEntered = hasEntered;
    }

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Message message) {
        InlineKeyboardCreator inlineKeyboardCreator = new EnteringItemTitleInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        String answer = "Enter the correct title of the item below (part or full name)\uD83D\uDC47:";
        setEntered(true);
        return new EditMessageText(message.chat().id(), message.messageId(), answer).replyMarkup(markupInline);
    }
}
