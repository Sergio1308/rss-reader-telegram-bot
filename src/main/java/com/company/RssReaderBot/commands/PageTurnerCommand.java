package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.LoadItemsByTitleInlineKeyboard;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.stereotype.Component;

@Component
public class PageTurnerCommand extends LoadItemsByTitleCommand implements Command<Message> {

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Message message) {
        InlineKeyboardCreator inlineKeyboardCreator = new LoadItemsByTitleInlineKeyboard();
        InlineKeyboardMarkup markup = inlineKeyboardCreator.createInlineKeyboard();
        return new EditMessageText(message.chat().id(), message.messageId(), getAnswer()).replyMarkup(markup);
    }

    /**
     * Turn to first or last page
     * @param chatId user chat id
     * @param messageId message id
     * @param callData callback query data
     * @return EditMessageText
     */
    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Long chatId, Integer messageId, String callData) {
        LoadItemsByTitleInlineKeyboard loadItemsByTitleInlineKeyboard = new LoadItemsByTitleInlineKeyboard();
        InlineKeyboardMarkup markupInline;
        loadItemsByTitleInlineKeyboard.itemsPagination.changePaginationIndex(callData);
        loadItemsByTitleInlineKeyboard.itemsPagination.calculateIndex();

        if (callData.equals(CallbackDataConstants.FIRST_PAGE)) {
            markupInline = loadItemsByTitleInlineKeyboard.executeCreation(0);
        } else {
            markupInline = loadItemsByTitleInlineKeyboard.executeCreation(
                    loadItemsByTitleInlineKeyboard.itemsPagination.getChunkedItemList().size() - 1
            );
        }
        return new EditMessageText(chatId, messageId, getAnswer()).replyMarkup(markupInline);
    }

    public void changePaginationIndex(String callData) {
        LoadItemsByTitleInlineKeyboard loadItemsByTitleInlineKeyboard = new LoadItemsByTitleInlineKeyboard();
        loadItemsByTitleInlineKeyboard.itemsPagination.changePaginationIndex(callData);
    }
}
