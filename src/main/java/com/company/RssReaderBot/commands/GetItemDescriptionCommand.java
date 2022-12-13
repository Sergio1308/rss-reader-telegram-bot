package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.inlinekeyboard.DescriptionAndLinkInlineKeyboard;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class GetItemDescriptionCommand implements Command<Long> {

    @Override
    public void execute(Long chatId, Long messageId) {
        InlineKeyboardCreator inlineKeyboardCreator = new DescriptionAndLinkInlineKeyboard();
        String answer = SelectItemCommand.getCurrentlySelectedEpisode().getDescription();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        messageSender.sendEditMessageText(chatId, messageId, answer, markupInline);
    }
}
