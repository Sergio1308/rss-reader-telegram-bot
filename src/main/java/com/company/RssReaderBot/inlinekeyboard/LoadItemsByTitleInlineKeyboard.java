package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.entities.ItemsList;
import com.company.RssReaderBot.entities.ItemsPagination;
import com.company.RssReaderBot.entities.Item;
import com.company.RssReaderBot.handlers.CallbackVars;
import com.company.RssReaderBot.services.CallbackDataCutter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoadItemsByTitleInlineKeyboard implements InlineKeyboardCreator {

    public final ItemsPagination itemsPagination = ItemsPagination.getInstance();

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        if (ItemsList.getItemsList().isEmpty()) { // if no results were found for the query
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<InlineKeyboardButton> rowInline = createInlineKeyboardButtonRowInline(
                    createInlineKeyboardButton("Cancel", CallbackVars.MAIN_MENU)
            );
            List<List<InlineKeyboardButton>> rowList = createInlineKeyboardButtonRowList(rowInline);
            markupInline.setKeyboard(rowList);
            return markupInline;
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
        /*InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        EditMessageReplyMarkup.EditMessageReplyMarkupBuilder editMessageReplyMarkupBuilder =
                EditMessageReplyMarkup.builder().replyMarkup(markup);
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setReplyMarkup(markup);*/
        ItemsList.setItemsMap(new HashMap<>());
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> pagesList = new ArrayList<>();

        ItemsPagination.setCurrentPage(buttonIndex + 1);
        displaySelectedItemsList(rowsInline, buttonIndex);
        System.out.println("get start btn index" + itemsPagination.getStartButtonsIndex() +
                "\t btn in row size" + itemsPagination.getButtonsInRowSize());
        displayPageListButtons(
                pagesList, itemsPagination.getStartButtonsIndex(), itemsPagination.getButtonsInRowSize()
        );
        rowsInline.add(pagesList);
        // check
        if (!itemsPagination.isLessThanPaginationButtons(itemsPagination.getButtonsInRowSize())) {
            navigationPagesButtons(rowsInline);
        }
        List<InlineKeyboardButton> backButtonRow = createInlineKeyboardButtonRowInline(
                createInlineKeyboardButton("Back to main menu", CallbackVars.MAIN_MENU)
        );
        rowsInline.add(backButtonRow);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    protected void displaySelectedItemsList(List<List<InlineKeyboardButton>> rowsInline, int index) {
        // items list buttons
        System.out.println("Items list has just displayed. Chunked items list index:" +
                index + " (current index pagination = " + itemsPagination.getCurrentIndexPagination() + ")");
        for (Item item : itemsPagination.getChunkedItemList().get(index)) {
            // using CallbackDataCutter which return cut text if title is too long
            ItemsList.putInMap(CallbackDataCutter.cutText(item.getTitle()), item);
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(item.getTitle());
            // an error will appear if callback text is longer than 64 characters
            button.setCallbackData(CallbackVars.SELECTED_BY_TITLE_CALLBACK +
                    CallbackDataCutter.cutText(item.getTitle()));
            rowInline.add(button);
            rowsInline.add(rowInline);
        }
    }

    protected void displayPageListButtons(List<InlineKeyboardButton> pagesList, int index, int buttonsInRowSize) {
        // pagination, first row (nmb of pages)
        ItemsPagination.getCallbackDataPaginationButtons().clear();
        System.out.println("i = " + index + ";\ti < " + buttonsInRowSize + " (buttonsInRowSize)");
        for (int i = index; i < buttonsInRowSize; i++) {
            InlineKeyboardButton page = new InlineKeyboardButton();
            if (i == ItemsPagination.getCurrentPage() - 1) { page.setText(">" + (i + 1) + "<"); }
            else { page.setText("" + (i + 1)); }
            page.setCallbackData("paginationButton_" + i);
            ItemsPagination.getCallbackDataPaginationButtons().add(page.getCallbackData());
            pagesList.add(page);
        }
    }

    protected void navigationPagesButtons(List<List<InlineKeyboardButton>> rowsInline) {
        // second row, page turning with back btn
        List<InlineKeyboardButton> pageTurningRowInline = createInlineKeyboardButtonRowInline(
                createInlineKeyboardButton
                        ("⏮1", CallbackVars.FIRST_PAGE),
                createInlineKeyboardButton
                        ("◀", CallbackVars.PREVIOUS_PAGE),
                createInlineKeyboardButton
                        ("▶", CallbackVars.NEXT_PAGE),
                createInlineKeyboardButton
                        ("" + itemsPagination.getChunkedItemList().size() + "⏭", CallbackVars.LAST_PAGE)
        );
        rowsInline.add(pageTurningRowInline);
    }
}
