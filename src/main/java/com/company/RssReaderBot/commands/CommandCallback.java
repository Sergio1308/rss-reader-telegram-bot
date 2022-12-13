package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.services.TelegramSendMessage;

public interface CommandCallback<T, V> {

    TelegramSendMessage messageSender = new TelegramSendMessage();

    void execute(T t1, T t2, V v);

}
