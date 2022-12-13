package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.LoadItemsByTitleInlineKeyboard;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class TurnPageCommand extends LoadItemsByTitleCommand implements Command<Long> {

    @Override
    public void execute(Long chatId, Long messageId) {
        InlineKeyboardCreator inlineKeyboardCreator = new LoadItemsByTitleInlineKeyboard();
        InlineKeyboardMarkup markup = inlineKeyboardCreator.createInlineKeyboard();

        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setReplyMarkup(markup);
        Command.messageSender.sendEditMessageText(chatId, messageId, getAnswer(), editMessageReplyMarkup.getReplyMarkup());
    }

    public void changePaginationIndex(String callData) {
        LoadItemsByTitleInlineKeyboard loadItemsByTitleInlineKeyboard = new LoadItemsByTitleInlineKeyboard();
        loadItemsByTitleInlineKeyboard.itemsPagination.changePaginationIndex(callData);
    }
}
