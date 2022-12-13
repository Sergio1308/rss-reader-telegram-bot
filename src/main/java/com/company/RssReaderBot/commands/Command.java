package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.services.TelegramSendMessage;

public interface Command<T> {

    TelegramSendMessage messageSender = new TelegramSendMessage();

    void execute(T t1, T t2);

}
