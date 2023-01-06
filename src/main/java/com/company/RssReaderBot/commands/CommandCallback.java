package com.company.RssReaderBot.commands;

import com.pengrad.telegrambot.request.BaseRequest;

public interface CommandCallback<T, V, A> {
    BaseRequest execute(T t1, V v, A a);
}
