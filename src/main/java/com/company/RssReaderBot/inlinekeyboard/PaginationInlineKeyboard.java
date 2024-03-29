package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.company.RssReaderBot.models.ItemModel;
import com.company.RssReaderBot.models.ItemsList;
import com.company.RssReaderBot.models.ItemsPagination;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class PaginationInlineKeyboard implements InlineKeyboardCreator {

    public final ItemsPagination itemsPagination;

    private final ItemsList itemsList;

    public PaginationInlineKeyboard(ItemsPagination itemsPagination, ItemsList itemsList) {
        this.itemsPagination = itemsPagination;
        this.itemsList = itemsList;
    }

    public String getAnswer() {
        return "Found: " + itemsList.getItemsList().size() +
                " items.\nCurrent page: " + itemsPagination.getCurrentPage();
    }

    public InlineKeyboardMarkup execute(int buttonIndex) {
        // todo: edit reply markup, update only items list
        itemsList.setItemsMap(new HashMap<>());
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        itemsPagination.setCurrentPage(buttonIndex + 1);
        displaySelectedItemsList(markupInline, buttonIndex);

        InlineKeyboardButton[] pagesButtons = displayPageListButtons(
                itemsPagination.getStartButtonsIndex(), itemsPagination.getButtonsInRowSize()
        );
        markupInline.addRow(pagesButtons);

        if (!itemsPagination.isLessThanPaginationButtons(itemsPagination.getButtonsInRowSize())) {
            InlineKeyboardButton[] inlineKeyboardButtons = new InlineKeyboardButton[]{
                    new InlineKeyboardButton("⏮1").callbackData(CallbackDataConstants.FIRST_PAGE),
                    new InlineKeyboardButton("◀").callbackData(CallbackDataConstants.PREVIOUS_PAGE),
                    new InlineKeyboardButton("").callbackData(CallbackDataConstants.NEXT_PAGE),
                    new InlineKeyboardButton("▶").callbackData(CallbackDataConstants.NEXT_PAGE),
                    new InlineKeyboardButton("" + itemsPagination.getChunkedItemListModel().size() + "⏭")
                            .callbackData(CallbackDataConstants.LAST_PAGE),
            };
            markupInline.addRow(inlineKeyboardButtons);
        }
        return markupInline;
    }

    public InlineKeyboardMarkup createButton(InlineKeyboardMarkup markup, String text, String callbackData) {
        return markup.addRow(new InlineKeyboardButton(text).callbackData(callbackData));
    }

    protected void displaySelectedItemsList(InlineKeyboardMarkup markup, int index) {
        // items list buttons
        for (int i = 0; i < itemsPagination.getChunkedItemListModel().get(index).size(); i++) {
            ItemModel itemModel = itemsPagination.getChunkedItemListModel().get(index).get(i);
            String callbackData = Integer.toString(i);
            itemsList.putInMap(callbackData, itemModel);
            markup.addRow(new InlineKeyboardButton(itemModel.getTitle()).callbackData(
                    CallbackDataConstants.SHOW_ITEM + callbackData));
        }
    }

    protected InlineKeyboardButton[] displayPageListButtons(int index, int buttonsInRowSize) {
        // pagination, first row (nmb of pages)
        List<InlineKeyboardButton> pagesList = new ArrayList<>();
        itemsPagination.getCallbackDataPaginationButtons().clear();
        for (int i = index; i < buttonsInRowSize; i++) {
            InlineKeyboardButton page = new InlineKeyboardButton(
                    i == itemsPagination.getCurrentPage() - 1 ? ">" + (i + 1) + "<" : "" + (i + 1)
            ).callbackData("paginationButton_" + i);
            itemsPagination.getCallbackDataPaginationButtons().add("paginationButton_" + i);
            pagesList.add(page);
        }
        return pagesList.toArray(new InlineKeyboardButton[0]);
    }

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        // todo
        return null;
    }
}
