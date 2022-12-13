package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.OtherDetailsInlineKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class GetItemOtherDetailsCommand implements Command<Long> {

    @Override
    public void execute(Long chatId, Long messageId) {
        InlineKeyboardCreator inlineKeyboardCreator = new OtherDetailsInlineKeyboard();
        String answer = SelectItemCommand.getCurrentlySelectedEpisode().getTitle() + "\nPublication date: " +
                SelectItemCommand.getCurrentlySelectedEpisode().getDate().getItemDate();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        messageSender.sendEditMessageText(chatId, messageId, answer, markupInline);
    }
}
