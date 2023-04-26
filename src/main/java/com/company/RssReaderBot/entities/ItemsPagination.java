package com.company.RssReaderBot.entities;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.company.RssReaderBot.services.Partition;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

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

    @Value("${bot.ui.display.buttons.column}")
    private int DISPLAY_BUTTONS_COLUMN;
    @Value("${bot.ui.max.buttons.pagination.row}")
    private int MAX_BUTTONS_PAGINATION_ROW;

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

    public void changePaginationIndex(String pressedButton) {
        if (pagesPartition.size() == 1) {
            currentIndexPagination = 0;
            return;
        }
        int lastIndex = pagesPartition.size() - 1;
        switch (pressedButton) {
            case CallbackDataConstants.FIRST_PAGE:
                currentIndexPagination = 0;
                break;
            case CallbackDataConstants.LAST_PAGE:
                currentIndexPagination = lastIndex;
                break;
            case CallbackDataConstants.NEXT_PAGE:
                if (canMoveToNextPage()) {
                    ++currentIndexPagination;
                }
                break;
            case CallbackDataConstants.PREVIOUS_PAGE:
                if (canMoveToPreviousPage()) {
                    --currentIndexPagination;
                }
                break;
            default:
                break;
        }
    }

    private boolean canMoveToNextPage() {
        return pagesPartition.size() - 1 > currentIndexPagination;
    }

    private boolean canMoveToPreviousPage() {
        return currentIndexPagination > 0;
    }
}
