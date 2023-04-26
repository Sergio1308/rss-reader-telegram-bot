package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.db.models.UserSettings;
import com.company.RssReaderBot.db.repositories.UserSettingsRepository;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.SettingsMenuInlineKeyboard;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.stereotype.Component;

@Component
public class SettingsMenuCommand implements Command<Message> {

    private final UserSettingsRepository userSettingsRepository;

    public SettingsMenuCommand(UserSettingsRepository userSettingsRepository) {
        this.userSettingsRepository = userSettingsRepository;
    }

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Message message) {
        UserSettings userSettings = userSettingsRepository.findByUserUserid(message.chat().id()).orElseThrow();

        InlineKeyboardCreator inlineKeyboardCreator = new SettingsMenuInlineKeyboard(userSettings);
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        String answer = "Settings menu\nBy clicking on the button with '⚙️', you can change the parameter";
        return new EditMessageText(message.chat().id(), message.messageId(), answer).replyMarkup(markupInline);
    }
}
