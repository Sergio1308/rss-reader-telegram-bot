package com.company.RssReaderBot.controllers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;

public interface Controller {
    BaseRequest<?, ?> handle(Update update);
}
