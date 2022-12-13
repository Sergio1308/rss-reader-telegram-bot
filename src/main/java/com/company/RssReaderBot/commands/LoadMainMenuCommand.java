package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.MenuItemsLoaderInlineKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class LoadMainMenuCommand implements Command<Long> {

    private String getAnswer() {
        return "\tMain menu\nChoose what you need";
    }

    public void execute(Long chatId) {
        sendStartMessage(chatId);
    }

    @Override
    public void execute(Long chatId, Long messageId) {
        InlineKeyboardCreator inlineKeyboardCreator = new MenuItemsLoaderInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        messageSender.sendEditMessageText(chatId, messageId, getAnswer(), markupInline);
    }

    public void sendStartMessage(Long chatId) {
        InlineKeyboardCreator inlineKeyboardCreator = new MenuItemsLoaderInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        messageSender.sendText(chatId, getAnswer(),  markupInline);
    }
}
