package com.company.RssReaderBot.commands.personal_menu;

import com.company.RssReaderBot.commands.Command;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.personal_menu.PersonalMenuInlineKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class ShowPersonalMenuCommand implements Command<Long> {

    @Override
    public void execute(Long chatId, Long messageId) {
        InlineKeyboardCreator inlineKeyboardCreator = new PersonalMenuInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        String answer = "Your Personal Menu & Settings";
        messageSender.sendEditMessageText(chatId, messageId, answer, markupInline);
    }
}
