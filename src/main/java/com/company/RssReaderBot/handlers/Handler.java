package com.company.RssReaderBot.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Handler {

    void handle(Update update);

    boolean hasHandler(Update update);
}
