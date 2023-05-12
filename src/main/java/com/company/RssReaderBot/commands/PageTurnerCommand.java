package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.company.RssReaderBot.inlinekeyboard.LoadItemsByTitleInlineKeyboard;
import com.company.RssReaderBot.inlinekeyboard.PaginationInlineKeyboard;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.stereotype.Component;

@Component
public class PageTurnerCommand implements Command<Message> {

    private final LoadItemsByTitleInlineKeyboard loadItemsByTitleInlineKeyboard;

    private final PaginationInlineKeyboard paginationInlineKeyboard;

    public PageTurnerCommand(LoadItemsByTitleInlineKeyboard loadItemsByTitleInlineKeyboard,
                             PaginationInlineKeyboard paginationInlineKeyboard) {
        this.loadItemsByTitleInlineKeyboard = loadItemsByTitleInlineKeyboard;
        this.paginationInlineKeyboard = paginationInlineKeyboard;
    }

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Message message) {
        InlineKeyboardMarkup markup = loadItemsByTitleInlineKeyboard.createInlineKeyboard();
        return new EditMessageText(
                message.chat().id(), message.messageId(), paginationInlineKeyboard.getAnswer()
        ).replyMarkup(markup);
    }

    /**
     * Turn to first or last page
     * @param chatId user chat id
     * @param messageId message id
     * @param callData callback query data
     * @return EditMessageText
     */
    public BaseRequest<EditMessageText, BaseResponse> execute(Long chatId, Integer messageId, String callData) {
        InlineKeyboardMarkup markupInline;
        paginationInlineKeyboard.itemsPagination.changePaginationIndex(callData);
        paginationInlineKeyboard.itemsPagination.calculateIndex();

        if (callData.equals(CallbackDataConstants.FIRST_PAGE)) {
            markupInline = paginationInlineKeyboard.execute(0);
        } else {
            markupInline = paginationInlineKeyboard.execute(
                    loadItemsByTitleInlineKeyboard.itemsPagination.getChunkedItemListModel().size() - 1
            );
        }
        return new EditMessageText(chatId, messageId, paginationInlineKeyboard.getAnswer()).replyMarkup(markupInline);
    }

    public void changePaginationIndex(String callData) {
        paginationInlineKeyboard.itemsPagination.changePaginationIndex(callData);
    }
}
