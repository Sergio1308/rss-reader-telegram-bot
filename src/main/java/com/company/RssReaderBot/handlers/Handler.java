package com.company.RssReaderBot.handlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;

public interface Handler {
    BaseRequest handle(Update update);
}
