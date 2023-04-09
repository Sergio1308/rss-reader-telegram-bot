package com.company.RssReaderBot.commands;

import com.pengrad.telegrambot.request.BaseRequest;

public interface Command<T, V> {
    BaseRequest<?, ?> execute(T t, V v);
}
