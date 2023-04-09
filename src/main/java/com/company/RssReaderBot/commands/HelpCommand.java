package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.HelpCommandInlineKeyboard;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command<Long, Integer> {

    private String getAnswer() {
        return "Here you can read about what an RSS feed is, see and use feed examples." +
                "\nSome of the buttons are clickable and will redirect you to the page in the browser";
    }

    @Override
    public BaseRequest<SendMessage, SendResponse> execute(Long chatId, Integer messageId) {
        InlineKeyboardCreator inlineKeyboardCreator = new HelpCommandInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        return new SendMessage(chatId, getAnswer()).replyMarkup(markupInline);
    }
}
