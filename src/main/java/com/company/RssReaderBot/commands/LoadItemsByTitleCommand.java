package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.entities.ItemsList;
import com.company.RssReaderBot.entities.ItemsPagination;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.LoadItemsByTitleInlineKeyboard;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;

public class LoadItemsByTitleCommand implements CommandCallback<Long, Integer, String> {

    private final InlineKeyboardCreator inlineKeyboardCreator = new LoadItemsByTitleInlineKeyboard();

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Long chatId, Integer messageId, String callData) {
        LoadItemsByTitleInlineKeyboard loadItemsByTitleInlineKeyboard = new LoadItemsByTitleInlineKeyboard();
        InlineKeyboardMarkup markupInline = loadItemsByTitleInlineKeyboard.createInlineKeyboard(callData);
        return new EditMessageText(chatId, messageId, getAnswer()).replyMarkup(markupInline);
    }

    public BaseRequest<? extends BaseRequest<?,?>, ? extends BaseResponse> process(Update update, Long chatId) {
        if (ItemsList.getItemsList().isEmpty()) {
            InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
            String answer = "No results were found for this query. Please, retype or press the cancel button.";
            return new SendMessage(chatId, answer).replyMarkup(markupInline);
        } else if (update.callbackQuery() != null) {
            // callbackHandler: return to the item list, remember previous page
            int messageId = update.callbackQuery().message().messageId();
            InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
            return new EditMessageText(chatId, messageId, getAnswer()).replyMarkup(markupInline);
        } else {
            // messageHandler
            EnteringItemTitleCommand.setEntered(false);
            InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
            LoadItemsByTitleInlineKeyboard loadItemsByTitleInlineKeyboard = new LoadItemsByTitleInlineKeyboard();// edit
            System.out.println("chunked items list: " + loadItemsByTitleInlineKeyboard.itemsPagination.getChunkedItemList().size());
            return new SendMessage(chatId, getAnswer()).replyMarkup(markupInline);
        }
    }

    public String getAnswer() {
        return "Found: " + ItemsList.getItemsList().size() +
                " items.\nCurrent page: " + ItemsPagination.getCurrentPage();
    }
}
