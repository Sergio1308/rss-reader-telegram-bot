package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.inlinekeyboard.EnteringItemTitleInlineKeyboard;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class EnteringItemTitleCommand implements Command<Long> {

    private static boolean hasEntered;

    public static boolean hasEntered() {
        return hasEntered;
    }

    public static void setEntered(boolean hasEntered) {
        EnteringItemTitleCommand.hasEntered = hasEntered;
    }

    @Override
    public void execute(Long chatId, Long messageId) {
        InlineKeyboardCreator inlineKeyboardCreator = new EnteringItemTitleInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        String answer = "Enter the correct title of the item below (part or full name)\uD83D\uDC47:";
        messageSender.sendEditMessageText(chatId, messageId, answer, markupInline);
        setEntered(true);
    }
}
