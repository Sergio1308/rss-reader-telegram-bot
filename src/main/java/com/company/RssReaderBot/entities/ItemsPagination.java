package com.company.RssReaderBot.entities;

import com.company.RssReaderBot.controllers.CallbackQueryConstants;
import com.company.RssReaderBot.services.Partition;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ItemsPagination {

    @Getter
    private int currentIndexPagination;
    @Getter
    private int startButtonsIndex;
    @Getter
    private int buttonsInRowSize;

    @Getter
    private Partition<Item> chunkedItemList;
    @Getter
    private Partition<List<Item>> pagesPartition;

    @Getter
    private static List<String> callbackDataPaginationButtons;

    @Getter @Setter
    private static int currentPage;

    private static final int DISPLAY_BUTTONS_COLUMN = 6; // config
    private static final int MAX_BUTTONS_PAGINATION_ROW = 6; // config

    private static final ItemsPagination INSTANCE = new ItemsPagination();

    public static ItemsPagination getInstance() {
        return INSTANCE;
    }

    public boolean isLessThanPaginationButtons(int currentPagesNumber) {
        return currentPagesNumber < MAX_BUTTONS_PAGINATION_ROW;
    }

    public void toSplit() {
        callbackDataPaginationButtons = new ArrayList<>();
        chunkedItemList = Partition.toChunk(ItemsList.getItemsList(), DISPLAY_BUTTONS_COLUMN);
        pagesPartition = Partition.toChunk(chunkedItemList, MAX_BUTTONS_PAGINATION_ROW);

        calculateIndex();
    }

    public void clear() {
        if (chunkedItemList != null) {
            chunkedItemList.clear();
            pagesPartition.clear();
            currentIndexPagination = 0;
        }
    }

    public void calculateIndex() {
        startButtonsIndex = ((currentIndexPagination + 1) * MAX_BUTTONS_PAGINATION_ROW) - MAX_BUTTONS_PAGINATION_ROW;
        buttonsInRowSize = (currentIndexPagination + 1) * MAX_BUTTONS_PAGINATION_ROW;
        if (chunkedItemList.size() < buttonsInRowSize) buttonsInRowSize = chunkedItemList.size();
    }

    public void changePaginationIndex(String pressedCallbackButton) {
        // todo replace statements
        if (pagesPartition.size() == 1 || pressedCallbackButton.equals(CallbackQueryConstants.FIRST_PAGE)) {
            currentIndexPagination = 0;
        } else if (pressedCallbackButton.equals(CallbackQueryConstants.LAST_PAGE)) {
            currentIndexPagination = pagesPartition.size() - 1;
        } else if (pressedCallbackButton.equals(CallbackQueryConstants.NEXT_PAGE) && pagesPartition.size() - 1 > currentIndexPagination) {
            ++currentIndexPagination;
        } else if (pressedCallbackButton.equals(CallbackQueryConstants.PREVIOUS_PAGE) && currentIndexPagination > 0) {
            --currentIndexPagination;
        }
    }
}
