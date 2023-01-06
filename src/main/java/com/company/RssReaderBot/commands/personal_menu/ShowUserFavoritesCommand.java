package com.company.RssReaderBot.commands.personal_menu;

import com.company.RssReaderBot.commands.Command;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

public class ShowUserFavoritesCommand implements Command<Long, Integer> {

    @Override
    public BaseRequest execute(Long chatId, Integer messageId) {
        return new SendMessage(chatId, "Not working yet.");
    }
}
