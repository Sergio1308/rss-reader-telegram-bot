package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.inlinekeyboard.LoadAllItemsInlineKeyboard;
import com.company.RssReaderBot.models.ItemsList;
import com.company.RssReaderBot.models.ItemsPagination;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.stereotype.Component;

@Component
public class LoadAllItemsCommand implements Command<Message> {

    private final LoadAllItemsInlineKeyboard loadAllItemsInlineKeyboard;

    private final ItemsList itemsList;

    private final ItemsPagination itemsPagination;

    public LoadAllItemsCommand(LoadAllItemsInlineKeyboard loadAllItemsInlineKeyboard, ItemsList itemsList, ItemsPagination itemsPagination) {
        this.loadAllItemsInlineKeyboard = loadAllItemsInlineKeyboard;
        this.itemsList = itemsList;
        this.itemsPagination = itemsPagination;
    }

    public String getAnswer() {
        return "Found: " + itemsList.getItemsList().size() +
                " items.\nCurrent page: " + itemsPagination.getCurrentPage();
    }

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Message message) {
        InlineKeyboardMarkup markupInline = loadAllItemsInlineKeyboard.createInlineKeyboard();
        return new EditMessageText(message.chat().id(), message.messageId(), getAnswer()).replyMarkup(markupInline);
    }
}
