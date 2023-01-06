package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.entities.ItemsList;
import com.company.RssReaderBot.entities.ItemsPagination;
import com.company.RssReaderBot.entities.Item;
import com.company.RssReaderBot.handlers.CallbackVars;
import com.company.RssReaderBot.services.CallbackDataCutter;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoadItemsByTitleInlineKeyboard implements InlineKeyboardCreator {

    public final ItemsPagination itemsPagination = ItemsPagination.getInstance();

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        if (ItemsList.getItemsList().isEmpty()) { // if no results were found for the query
            return new InlineKeyboardMarkup(
                    new InlineKeyboardButton("Cancel").callbackData(CallbackVars.MAIN_MENU)
            );
        }
        itemsPagination.toSplit();
        return executeCreation(itemsPagination.getStartButtonsIndex());
    }

    public InlineKeyboardMarkup createInlineKeyboard(String callbackData) {
        int buttonIndex = 0;
        try {
            String pressedButtonIndex = callbackData.substring(callbackData.indexOf("_") + 1);
            buttonIndex = Integer.parseInt(pressedButtonIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executeCreation(buttonIndex);
    }

    public InlineKeyboardMarkup executeCreation(int buttonIndex) {
        // todo: edit reply markup, update only items list
        ItemsList.setItemsMap(new HashMap<>());
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        ItemsPagination.setCurrentPage(buttonIndex + 1);
        displaySelectedItemsList(markupInline, buttonIndex);
        System.out.println("get start btn index" + itemsPagination.getStartButtonsIndex() +
                "\t btn in row size" + itemsPagination.getButtonsInRowSize());
        InlineKeyboardButton[] pagesButtons = displayPageListButtons(
                itemsPagination.getStartButtonsIndex(), itemsPagination.getButtonsInRowSize()
        );
        markupInline.addRow(pagesButtons);
        // check
        if (!itemsPagination.isLessThanPaginationButtons(itemsPagination.getButtonsInRowSize())) {
            InlineKeyboardButton[] inlineKeyboardButtons = new InlineKeyboardButton[]{
                    new InlineKeyboardButton("⏮1").callbackData(CallbackVars.FIRST_PAGE),
                    new InlineKeyboardButton("◀").callbackData(CallbackVars.PREVIOUS_PAGE),
                    new InlineKeyboardButton("").callbackData(CallbackVars.NEXT_PAGE),
                    new InlineKeyboardButton("▶").callbackData(CallbackVars.NEXT_PAGE),
                    new InlineKeyboardButton("" + itemsPagination.getChunkedItemList().size() + "⏭")
                            .callbackData(CallbackVars.LAST_PAGE),
            };
            markupInline.addRow(inlineKeyboardButtons);
        }
        markupInline.addRow(new InlineKeyboardButton("Back to main menu").callbackData(CallbackVars.MAIN_MENU));
        return markupInline;
    }

    protected void displaySelectedItemsList(InlineKeyboardMarkup markup, int index) {
        // items list buttons
        System.out.println("Items list has just displayed. Chunked items list index:" +
                index + " (current index pagination = " + itemsPagination.getCurrentIndexPagination() + ")");
        for (Item item : itemsPagination.getChunkedItemList().get(index)) {
            // using CallbackDataCutter which return cut text if title is too long
            ItemsList.putInMap(CallbackDataCutter.cutText(item.getTitle()), item);
            markup.addRow(new InlineKeyboardButton(item.getTitle()).callbackData(
                    CallbackVars.SELECTED_BY_TITLE_CALLBACK + CallbackDataCutter.cutText(item.getTitle())));
        }
    }

    protected InlineKeyboardButton[] displayPageListButtons(int index, int buttonsInRowSize) {
        // pagination, first row (nmb of pages)
        List<InlineKeyboardButton> pagesList = new ArrayList<>();
        ItemsPagination.getCallbackDataPaginationButtons().clear();
        System.out.println("i = " + index + ";\ti < " + buttonsInRowSize + " (buttonsInRowSize)");
        for (int i = index; i < buttonsInRowSize; i++) {
            InlineKeyboardButton page = new InlineKeyboardButton(
                    i == ItemsPagination.getCurrentPage() - 1 ? ">" + (i + 1) + "<" : "" + (i + 1)
            ).callbackData("paginationButton_" + i);
            ItemsPagination.getCallbackDataPaginationButtons().add("paginationButton_" + i);
            pagesList.add(page);
        }
        return pagesList.toArray(new InlineKeyboardButton[0]);
    }
}
