package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.HelpCommandInlineKeyboard;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command<Message> {

    private String getAnswer() {
        return "Welcome to the help menu. Here, you can read about what an RSS feed is, see and use feed examples. " +
                "Also, you can use the first button to subscribe to the sample feed." +
                "\nSome of the buttons are clickable and will redirect you to the page in the browser.";
    }

    @Override
    public BaseRequest<SendMessage, SendResponse> execute(Message message) {
        InlineKeyboardCreator inlineKeyboardCreator = new HelpCommandInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        return new SendMessage(message.chat().id(), getAnswer()).replyMarkup(markupInline);
    }
}
