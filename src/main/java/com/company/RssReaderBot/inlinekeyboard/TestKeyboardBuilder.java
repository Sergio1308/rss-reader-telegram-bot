package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.handlers.CallbackVars;
import com.company.RssReaderBot.services.TelegramSendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static java.util.Arrays.asList;

public class TestKeyboardBuilder {
    public void execute(long chatId, long messageId) {
        System.out.println("executed test keyboard builder");
        // example
        TelegramSendMessage sender = new TelegramSendMessage();

        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder keyboardMarkupBuilder = InlineKeyboardMarkup.builder();
        keyboardMarkupBuilder.keyboardRow(asList(
                InlineKeyboardButton.builder().text("test").callbackData("test").build(),
                InlineKeyboardButton.builder().text("test2").callbackData("test2").build(),
                InlineKeyboardButton.builder().text("cancel").callbackData(CallbackVars.RETURN_LOAD_BY_TITLE).build()
        ));

        EditMessageReplyMarkup.EditMessageReplyMarkupBuilder editMessageReplyMarkupBuilder =
                EditMessageReplyMarkup.builder().chatId(chatId).replyMarkup(keyboardMarkupBuilder.build());

        sender.sendEditMessageText(chatId, messageId, "test",  editMessageReplyMarkupBuilder.build().getReplyMarkup());
    }
}
