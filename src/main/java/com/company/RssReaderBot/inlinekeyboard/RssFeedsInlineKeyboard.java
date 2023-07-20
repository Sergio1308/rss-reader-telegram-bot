package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.company.RssReaderBot.db.entities.RssFeed;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Display subscribed feed list.
 */
@Component
public class RssFeedsInlineKeyboard implements InlineKeyboardCreator {

    @Getter @Setter
    private List<RssFeed> feedList;

    @Getter @Setter
    private int currentFeedIndex = 0;

    public RssFeedsInlineKeyboard() {

    }

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        // unsubscribe menu
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton backButton = new InlineKeyboardButton("Back")
                .callbackData(CallbackDataConstants.HIDE_MESSAGE);
        if (feedList.isEmpty()) {
            return markup.addRow(new InlineKeyboardButton("The list is empty.")
                    .callbackData(CallbackDataConstants.SUBSCRIBE)).addRow(backButton);
        }
        InlineKeyboardButton[] buttons = new InlineKeyboardButton[feedList.size()];
        for (int i = 0; i < feedList.size(); i++) {
            RssFeed feed = feedList.get(i);
            buttons[i] = new InlineKeyboardButton(feed.getTitle())
                    .callbackData(CallbackDataConstants.FEED_BUTTON + feed.getId());
        }
        return markup.addRow(buttons).addRow(backButton);
    }

    public BaseRequest<?, BaseResponse> handleFeedNavigation(CallbackQuery callbackQuery,
                                                             GetItemsInlineKeyboard getItemsInlineKeyboard) {
        if (feedList.isEmpty()) {
            return new AnswerCallbackQuery(callbackQuery.id());
        }
        InlineKeyboardMarkup markup = scrollFeedForwardOrBackward(callbackQuery.data());
        getItemsInlineKeyboard.createGetItemsButtons(markup);
        return new EditMessageReplyMarkup(callbackQuery.message().chat().id(), callbackQuery.message().messageId())
                .replyMarkup(markup);
    }

    private InlineKeyboardMarkup scrollFeedForwardOrBackward(String callData) {
        InlineKeyboardMarkup markupInline;
        if (callData.equals(CallbackDataConstants.SCROLL_BACKWARD_FEED)) {
            markupInline = scrollBackward();
        } else {
            markupInline = scrollForward();
        }
        return markupInline;
    }

    public InlineKeyboardButton[] createFeedListButton() {
        adjustCurrentFeedIndex();
        RssFeed selectedFeed = feedList.get(currentFeedIndex);
        return new InlineKeyboardButton[] {
                new InlineKeyboardButton("◀️")
                        .callbackData(CallbackDataConstants.SCROLL_BACKWARD_FEED),
                new InlineKeyboardButton(showCurrentSelectedFeedMonitor() + " " + getCurrentFeedTitle())
                        .callbackData(CallbackDataConstants.FEED_BUTTON + selectedFeed.getId()),
                new InlineKeyboardButton("▶️")
                        .callbackData(CallbackDataConstants.SCROLL_FORWARD_FEED)
        };
    }

    private String showCurrentSelectedFeedMonitor() {
        return "(" + (currentFeedIndex + 1) + "/" + feedList.size() + ")";
    }

    public InlineKeyboardMarkup scrollForward() {
        currentFeedIndex++;
        if (currentFeedIndex >= feedList.size()) {
            currentFeedIndex = 0; // first feed in list
        }
        return updateFeedButton(getCurrentFeedTitle());
    }

    public InlineKeyboardMarkup scrollBackward() {
        currentFeedIndex--;
        if (currentFeedIndex < 0) {
            currentFeedIndex = feedList.size() - 1; // last feed in list
        }
        return updateFeedButton(getCurrentFeedTitle());
    }

    /**
     * Adjusts the current feed index based on the specified conditions.
     * If the current feed index is greater than or equal to the size of the feed list,
     * it sets the current feed index to the previous index.
     * If the feed list contains only one feed, it sets the current feed index to 0.
     */
    private void adjustCurrentFeedIndex() {
        if (feedList.size() == 1) {
            currentFeedIndex = 0;
        } else if (currentFeedIndex >= feedList.size()) {
            currentFeedIndex -= 1;
        }
    }

    private InlineKeyboardMarkup updateFeedButton(String title) {
        InlineKeyboardButton feedButton = new InlineKeyboardButton(showCurrentSelectedFeedMonitor() + " " + title)
                .callbackData(CallbackDataConstants.SETTINGS_SELECTED_FEED);
        InlineKeyboardButton[] buttonsFeed = createFeedListButton();
        buttonsFeed[1] = feedButton;
        return new InlineKeyboardMarkup(buttonsFeed);
    }

    private String getCurrentFeedTitle() {
        if (feedList.isEmpty()) {
            return "No Feeds";
        }
        return feedList.get(currentFeedIndex).getTitle();
    }
}
