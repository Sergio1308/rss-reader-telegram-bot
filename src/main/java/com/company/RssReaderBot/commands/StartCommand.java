package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.db.services.UserService;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.StartMenuInlineKeyboard;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command<Message> {

    private final UserService userService;

    public StartCommand(UserService userService) {
        this.userService = userService;
    }

    public String getAnswer() {
        return "RSS Reader Bot\n\n" +
                "Using the buttons below, you can subscribe\nto the RSS feeds you want,\n" +
                "or display individual feed items\nand save them to your favorites.";
    }

    @Override
    public BaseRequest<SendMessage, SendResponse> execute(Message message) {
        InlineKeyboardCreator inlineKeyboardCreator = new StartMenuInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        User tgUser = message.from();
        userService.addUser(tgUser);
        return new SendMessage(message.chat().id(), getAnswer()).replyMarkup(markupInline);
    }

    public BaseRequest<EditMessageText, BaseResponse> displayStartMenu(Long chatId, Integer messageId) {
        InlineKeyboardCreator inlineKeyboardCreator = new StartMenuInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();

        return new EditMessageText(chatId, messageId, getAnswer()).replyMarkup(markupInline);
    }
}
