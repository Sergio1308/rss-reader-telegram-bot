package com.company.RssReaderBot.models;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.company.RssReaderBot.utils.Partition;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemsPagination {

    private static final int DISPLAY_BUTTONS_COLUMN = 6;
    private static final int MAX_BUTTONS_PAGINATION_ROW = 6;

    private final ItemsList itemsList;

    @Getter
    private int currentIndexPagination;
    @Getter
    private int startButtonsIndex;
    @Getter
    private int buttonsInRowSize;

    @Getter
    private Partition<ItemModel> chunkedItemListModel;
    @Getter
    private Partition<List<ItemModel>> pagesPartition;

    @Getter
    private List<String> callbackDataPaginationButtons;

    @Getter @Setter
    private int currentPage;

    public ItemsPagination(ItemsList itemsList) {
        this.itemsList = itemsList;
    }

    public boolean isLessThanPaginationButtons(int currentPagesNumber) {
        return currentPagesNumber < MAX_BUTTONS_PAGINATION_ROW;
    }

    public void toSplit() {
        callbackDataPaginationButtons = new ArrayList<>();
        chunkedItemListModel = Partition.toChunk(itemsList.getItemsList(), DISPLAY_BUTTONS_COLUMN);
        pagesPartition = Partition.toChunk(chunkedItemListModel, MAX_BUTTONS_PAGINATION_ROW);
        calculateIndex();
    }

    public void clear() {
        if (chunkedItemListModel != null) {
            chunkedItemListModel.clear();
            pagesPartition.clear();
            currentIndexPagination = 0;
        }
    }

    public void calculateIndex() {
        startButtonsIndex = ((currentIndexPagination + 1) * MAX_BUTTONS_PAGINATION_ROW) - MAX_BUTTONS_PAGINATION_ROW;
        buttonsInRowSize = (currentIndexPagination + 1) * MAX_BUTTONS_PAGINATION_ROW;
        if (chunkedItemListModel.size() < buttonsInRowSize) buttonsInRowSize = chunkedItemListModel.size();
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
