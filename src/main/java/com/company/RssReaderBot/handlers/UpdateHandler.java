package com.company.RssReaderBot.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.LinkedHashSet;
import java.util.Set;

public class UpdateHandler implements Handler {

    private final CallbackHandler callbackHandler = new CallbackHandler();
    private final MessageHandler messageHandler = new MessageHandler();

    private Set<Handler> getHandlers() {
        Set<Handler> handlers = new LinkedHashSet<>();

        handlers.add(messageHandler);
        handlers.add(callbackHandler);

        return handlers;
    }

    @Override
    public void handle(Update update) {
        try {
            handleUpdate(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasHandler(Update update) {
        return true;
    }

    private void handleUpdate(Update update) {
        getHandlers().stream()
                .filter(handler -> handler.hasHandler(update))
                .findFirst()
                .ifPresent(handler -> handler.handle(update));
    }
}
