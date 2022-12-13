package com.company.RssReaderBot.services;

import com.company.RssReaderBot.core.RssReaderBot;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.lang.Math.toIntExact;

public class TelegramSendMessage extends DefaultAbsSender implements TelegramSender {

    public TelegramSendMessage() {
        super(new DefaultBotOptions());
    }

    @Override
    public void sendText(long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.enableMarkdown(true);
        executeMessage(sendMessage);
    }

    public void sendText(long chatId, String text, InlineKeyboardMarkup markupInline) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(markupInline);
        executeMessage(sendMessage);
    }

    public void sendEditMessageText(long chatId, long messageId,
                                    String answer, InlineKeyboardMarkup markupInline) {
        EditMessageText newMessage = new EditMessageText();
        newMessage.setChatId(chatId);
        newMessage.setMessageId(toIntExact(messageId));
        newMessage.setText(answer);
        newMessage.setReplyMarkup(markupInline);
        executeMessage(newMessage);
    }

    @Override
    public void executeMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void executeMessage(EditMessageText sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return RssReaderBot.token;
    }
}
