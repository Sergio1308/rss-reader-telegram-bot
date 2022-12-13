package com.company.RssReaderBot.services;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface TelegramSender {

    void sendText(long chatId, String text);

    void executeMessage(SendMessage sendMessage);
}
