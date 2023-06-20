package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.company.RssReaderBot.db.entities.FavoriteItem;
import com.company.RssReaderBot.db.entities.RssFeed;
import com.company.RssReaderBot.db.entities.UserSettings;
import com.company.RssReaderBot.db.repositories.RssFeedRepository;
import com.company.RssReaderBot.db.repositories.UserSettingsRepository;
import com.company.RssReaderBot.inlinekeyboard.RssFeedsInlineKeyboard;
import com.company.RssReaderBot.inlinekeyboard.SettingsMenuInlineKeyboard;
import com.company.RssReaderBot.models.RssFeedCheckerRegistry;
import com.company.RssReaderBot.utils.RssFeedChecker;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.BaseResponse;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SettingsMenuCommand implements Command<Message> {

    @Getter
    private final SettingsMenuInlineKeyboard settingsMenuInlineKeyboard;

    private final UserSettingsRepository userSettingsRepository;

    private final RssFeedRepository feedRepository;

    private final RssFeedsInlineKeyboard feedsInlineKeyboard;

    private final RssFeedCheckerRegistry feedCheckerRegistry;

    public SettingsMenuCommand(UserSettingsRepository userSettingsRepository, RssFeedRepository feedRepository,
                               SettingsMenuInlineKeyboard settingsMenuInlineKeyboard,
                               RssFeedsInlineKeyboard feedsInlineKeyboard,
                               RssFeedCheckerRegistry feedCheckerRegistry) {
        this.userSettingsRepository = userSettingsRepository;
        this.feedRepository = feedRepository;
        this.settingsMenuInlineKeyboard = settingsMenuInlineKeyboard;
        this.feedsInlineKeyboard = feedsInlineKeyboard;
        this.feedCheckerRegistry = feedCheckerRegistry;
    }

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Message message) {
        UserSettings userSettings = userSettingsRepository.findByUserUserid(message.chat().id()).orElseThrow();

        settingsMenuInlineKeyboard.setUserSettings(userSettings);
        InlineKeyboardMarkup markup = settingsMenuInlineKeyboard.createInlineKeyboard();
        String answer = "Settings menu\nBy clicking on the button with '⚙️', you can change the parameter";
        return new EditMessageText(message.chat().id(), message.messageId(), answer).replyMarkup(markup);
    }

    public BaseRequest<EditMessageText, BaseResponse> displayFavoritesItems(Message message,
                                                                            List<FavoriteItem> favoriteItems) {
        InlineKeyboardMarkup markup = settingsMenuInlineKeyboard.displayFavorites(favoriteItems);
        String answer = "Favorites";
        return new EditMessageText(message.chat().id(), message.messageId(), answer).replyMarkup(markup);
    }

    public BaseRequest<EditMessageText, BaseResponse> displayRssFeedSettings(Message message,
                                                                             List<RssFeed> feedList) {
        settingsMenuInlineKeyboard.setFeedList(feedList);
        InlineKeyboardMarkup markup = settingsMenuInlineKeyboard.displayRssFeedSettings();
        String answer = "RSS feeds Management\n\nCurrently selected RSS feed:";
        return new EditMessageText(message.chat().id(), message.messageId(), answer).replyMarkup(markup);
    }

    public BaseRequest<?, BaseResponse> saveDisplayOption(CallbackQuery callbackQuery) {
        UserSettings userSettings = settingsMenuInlineKeyboard.getUserSettings();
        CallbackDataConstants.DisplayOptions displayOptions = Arrays
                .stream(CallbackDataConstants.DisplayOptions.values())
                        .filter(f -> f.getCallback().equals(callbackQuery.data()))
                        .findFirst()
                        .orElse(null);
        if (displayOptions != null) {
            displayOptions.setDisplayValue(userSettings);
            userSettingsRepository.save(userSettings);

            return new EditMessageReplyMarkup(callbackQuery.message().chat().id(), callbackQuery.message().messageId())
                    .replyMarkup(settingsMenuInlineKeyboard.createInlineKeyboard()); // update inline keyboard
        }
        return new AnswerCallbackQuery(callbackQuery.id());
    }

    public BaseRequest<? , BaseResponse> updateFeedRowButtons(CallbackQuery callbackQuery) {
        if (settingsMenuInlineKeyboard.getFeedList().size() == 1) {
            // do not scroll if there is only 1 feed, just return answer to callback
            return new AnswerCallbackQuery(callbackQuery.id());
        }
        feedsInlineKeyboard.setFeedList(settingsMenuInlineKeyboard.getFeedList());
        InlineKeyboardMarkup markupInline;
        if (callbackQuery.data().equals(CallbackDataConstants.SCROLL_BACKWARD_FEED)) {
            markupInline = feedsInlineKeyboard.scrollBackward();
        } else {
            markupInline = feedsInlineKeyboard.scrollForward();
        }
        settingsMenuInlineKeyboard.createFeedSettingsButtons(markupInline);
        return new EditMessageReplyMarkup(callbackQuery.message().chat().id(), callbackQuery.message().messageId())
                .replyMarkup(markupInline);
    }

    public BaseRequest<EditMessageReplyMarkup, BaseResponse> configureFeed(CallbackQuery callbackQuery) {
        int index = feedsInlineKeyboard.getCurrentFeedIndex();
        List<RssFeed> feedList = feedsInlineKeyboard.getFeedList();
        RssFeed currentFeed = feedList.get(index);

        if (callbackQuery.data().equals(CallbackDataConstants.SETTINGS_FEED_POSTING)) {
            boolean posting = currentFeed.isPosting();
            currentFeed.setPosting(!posting);
            feedList.set(index, currentFeed);
            settingsMenuInlineKeyboard.setFeedList(feedList);
            feedRepository.save(currentFeed);

            RssFeedChecker feedChecker = feedCheckerRegistry.getRssFeedChecker(currentFeed.getId());
            feedChecker.toggleChecking(currentFeed.isPosting());
        } else if (callbackQuery.data().equals(CallbackDataConstants.SETTINGS_FEED_INTERVAL)) {
            return new EditMessageReplyMarkup(callbackQuery.message().chat().id(),
                    callbackQuery.message().messageId())
                    .replyMarkup(settingsMenuInlineKeyboard.createIntervalFeedMenu());
        }
        return new EditMessageReplyMarkup(callbackQuery.message().chat().id(), callbackQuery.message().messageId())
                .replyMarkup(settingsMenuInlineKeyboard.displayRssFeedSettings());
    }
}
