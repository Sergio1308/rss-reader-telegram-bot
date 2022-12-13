package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.inlinekeyboard.LoadAllItemsInlineKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class LoadAllItemsCommand extends LoadItemsByTitleCommand implements Command<Long> {

    @Override
    public void execute(Long chatId, Long messageId) {
        LoadAllItemsInlineKeyboard loadAllItemsInlineKeyboard = new LoadAllItemsInlineKeyboard();
        InlineKeyboardMarkup markupInline = loadAllItemsInlineKeyboard.createInlineKeyboard();
        Command.messageSender.sendEditMessageText(chatId, messageId, getAnswer(), markupInline);
    }
}
