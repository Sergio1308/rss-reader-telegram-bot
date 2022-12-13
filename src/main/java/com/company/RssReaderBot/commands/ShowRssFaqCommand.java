package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.RssFaqInlineKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class ShowRssFaqCommand implements Command<Long> {

    private String getAnswer() {
        return "<Info about RSS>";
    }

    @Override
    public void execute(Long chatId, Long messageId) {
        InlineKeyboardCreator inlineKeyboardCreator = new RssFaqInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        messageSender.sendEditMessageText(chatId, messageId, getAnswer(), markupInline);
    }
}
