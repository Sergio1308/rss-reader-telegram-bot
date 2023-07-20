package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.controllers.MessageController;
import com.company.RssReaderBot.controllers.core.BotState;
import com.company.RssReaderBot.models.ItemsList;
import com.company.RssReaderBot.models.ItemsPagination;
import com.company.RssReaderBot.inlinekeyboard.LoadItemsByTitleInlineKeyboard;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.stereotype.Component;

@Component
public class LoadItemsByTitleCommand implements Command<Message> {

    private final LoadItemsByTitleInlineKeyboard loadItemsByTitleInlineKeyboard;

    private final ItemsList itemsList;

    private final ItemsPagination itemsPagination;

    public LoadItemsByTitleCommand(LoadItemsByTitleInlineKeyboard loadItemsByTitleInlineKeyboard,
                                   ItemsList itemsList, ItemsPagination itemsPagination) {
        this.loadItemsByTitleInlineKeyboard = loadItemsByTitleInlineKeyboard;
        this.itemsList = itemsList;
        this.itemsPagination = itemsPagination;
    }

    public BaseRequest<EditMessageText, BaseResponse> execute(Long chatId, Integer messageId, String callData) {
        InlineKeyboardMarkup markupInline = loadItemsByTitleInlineKeyboard.createInlineKeyboard(callData);
        return new EditMessageText(chatId, messageId, getAnswer()).replyMarkup(markupInline);
    }

    public BaseRequest<? extends BaseRequest<?,?>, ? extends BaseResponse> process(Update update, Long chatId) {
        if (itemsList.getItemsList().isEmpty()) {
            InlineKeyboardMarkup markupInline = loadItemsByTitleInlineKeyboard.createInlineKeyboard();
            String answer = "No results were found for this query. Please, retype or press the cancel button.";
            return new SendMessage(chatId, answer).replyMarkup(markupInline);
        } else if (update.callbackQuery() != null) {
            // callbackHandler: return to the item list, remember previous page
            int messageId = update.callbackQuery().message().messageId();
            InlineKeyboardMarkup markupInline = loadItemsByTitleInlineKeyboard.createInlineKeyboard();
            return new EditMessageText(chatId, messageId, getAnswer()).replyMarkup(markupInline);
        } else {
            // messageHandler
            MessageController.getUserStates().put(chatId, BotState.NONE);
            InlineKeyboardMarkup markupInline = loadItemsByTitleInlineKeyboard.createInlineKeyboard();
            return new SendMessage(chatId, getAnswer()).replyMarkup(markupInline);
        }
    }

    public String getAnswer() {
        return "Found: " + itemsList.getItemsList().size() +
                " items.\nCurrent page: " + itemsPagination.getCurrentPage();
    }

    @Override
    public BaseRequest<?, ?> execute(Message message) {
        return null;
    }
}
