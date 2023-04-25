package com.company.RssReaderBot.commands;

import com.pengrad.telegrambot.request.BaseRequest;

public interface Command<T> {
    BaseRequest<?, ?> execute(T t);
}
