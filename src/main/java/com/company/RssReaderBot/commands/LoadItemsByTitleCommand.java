package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.entities.ItemsList;
import com.company.RssReaderBot.entities.ItemsPagination;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.LoadItemsByTitleInlineKeyboard;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class LoadItemsByTitleCommand implements CommandCallback<Long, String> {

    private final InlineKeyboardCreator inlineKeyboardCreator = new LoadItemsByTitleInlineKeyboard();

    @Override
    public void execute(Long chatId, Long messageId, String callData) {
        LoadItemsByTitleInlineKeyboard loadItemsByTitleInlineKeyboard = new LoadItemsByTitleInlineKeyboard();
        InlineKeyboardMarkup markupInline = loadItemsByTitleInlineKeyboard.createInlineKeyboard(callData);
        messageSender.sendEditMessageText(chatId, messageId, getAnswer(), markupInline);
    }

    public void process(Update update, Long chatId) {
        if (ItemsList.getItemsList().isEmpty()) {
            InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
            String answer = "No results were found for this query. Please, retype or press the cancel button.";
            messageSender.sendText(chatId, answer, markupInline);
        } else if (update.hasCallbackQuery()) {
            // callbackHandler: return to the item list, remember previous page
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
            messageSender.sendEditMessageText(chatId, messageId, getAnswer(), markupInline);
        } else {
            // messageHandler
            EnteringItemTitleCommand.setEntered(false);
            InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
            LoadItemsByTitleInlineKeyboard loadItemsByTitleInlineKeyboard = new LoadItemsByTitleInlineKeyboard(); // edit
            System.out.println("chunked items list: " + loadItemsByTitleInlineKeyboard.itemsPagination.getChunkedItemList().size());
            messageSender.sendText(chatId, getAnswer(), markupInline);
        }
    }

    public String getAnswer() {
        return "Found: " + ItemsList.getItemsList().size() +
                " items.\nCurrent page: " + ItemsPagination.getCurrentPage();
    }
}
