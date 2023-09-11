package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.controllers.MessageController;
import com.company.RssReaderBot.controllers.core.BotState;
import com.company.RssReaderBot.models.ItemsList;
import com.company.RssReaderBot.models.ItemsPagination;
import com.company.RssReaderBot.inlinekeyboard.FeedItemsViewInlineKeyboard;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.stereotype.Component;

@Component
public class FeedItemsViewCommand implements Command<Message> {

    private final FeedItemsViewInlineKeyboard feedItemsViewInlineKeyboard;

    private final ItemsList itemsList;

    private final ItemsPagination itemsPagination;

    public FeedItemsViewCommand(FeedItemsViewInlineKeyboard feedItemsViewInlineKeyboard,
                                ItemsList itemsList, ItemsPagination itemsPagination) {
        this.feedItemsViewInlineKeyboard = feedItemsViewInlineKeyboard;
        this.itemsList = itemsList;
        this.itemsPagination = itemsPagination;
    }

    public BaseRequest<EditMessageText, BaseResponse> execute(Long chatId, Integer messageId, String callData) {
        InlineKeyboardMarkup markupInline = feedItemsViewInlineKeyboard.createInlineKeyboard(callData);
        return new EditMessageText(chatId, messageId, getAnswer()).replyMarkup(markupInline);
    }

    public BaseRequest<? extends BaseRequest<?,?>, ? extends BaseResponse> process(Update update, Long chatId) {
        if (itemsList.getItemsList().isEmpty()) {
            InlineKeyboardMarkup markupInline = feedItemsViewInlineKeyboard.createInlineKeyboard();
            String answer = "No results were found for this query. Please, retype or press the cancel button.";
            return new SendMessage(chatId, answer).replyMarkup(markupInline);
        } else if (update.callbackQuery() != null) {
            // callbackHandler: return to the item list, remember previous page
            int messageId = update.callbackQuery().message().messageId();
            InlineKeyboardMarkup markupInline = feedItemsViewInlineKeyboard.createInlineKeyboard();
            return new EditMessageText(chatId, messageId, getAnswer()).replyMarkup(markupInline);
        } else {
            // messageHandler
            MessageController.getUserStates().put(chatId, BotState.NONE);
            InlineKeyboardMarkup markupInline = feedItemsViewInlineKeyboard.createInlineKeyboard();
            return new SendMessage(chatId, getAnswer()).replyMarkup(markupInline);
        }
    }

    public String getAnswer() {
        return "Found: " + itemsList.getItemsList().size() +
                " items.\nCurrent page: " + itemsPagination.getCurrentPage();
    }

    @Override
    public BaseRequest<?, ?> execute(Message message) {
        InlineKeyboardMarkup markupInline = feedItemsViewInlineKeyboard.createInlineKeyboard();
        if (message.replyMarkup() != null) {
            return new EditMessageText(message.chat().id(), message.messageId(), getAnswer()).replyMarkup(markupInline);
        }
        return new SendMessage(message.chat().id(), getAnswer()).replyMarkup(markupInline);
    }
}
