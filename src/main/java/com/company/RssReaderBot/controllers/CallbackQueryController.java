package com.company.RssReaderBot.controllers;

import com.company.RssReaderBot.commands.*;
import com.company.RssReaderBot.commands.SettingsMenuCommand;
import com.company.RssReaderBot.config.BotConfig;
import com.company.RssReaderBot.db.entities.FavoriteItem;
import com.company.RssReaderBot.db.entities.RssFeed;
import com.company.RssReaderBot.db.entities.UserDB;
import com.company.RssReaderBot.db.services.FavoriteItemService;
import com.company.RssReaderBot.inlinekeyboard.GetItemsInlineKeyboard;
import com.company.RssReaderBot.inlinekeyboard.SettingsMenuInlineKeyboard;
import com.company.RssReaderBot.models.ItemModel;
import com.company.RssReaderBot.models.ItemsPagination;
import com.company.RssReaderBot.inlinekeyboard.RssFeedsInlineKeyboard;
import com.company.RssReaderBot.models.RssFeedCheckerRegistry;
import com.company.RssReaderBot.utils.RssFeedChecker;
import com.company.RssReaderBot.utils.parser.ParseElements;
import com.company.RssReaderBot.db.services.RssFeedService;
import com.company.RssReaderBot.db.services.UserService;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.BaseResponse;

import java.util.Arrays;
import java.util.List;

@BotController
public class CallbackQueryController implements TelegramMvcController, Controller {

    private final BotConfig botConfig;

    private final SubscribeCommand subscribeCommand;

    private final UnsubscribeCommand unsubscribeCommand;

    private final GetItemsCommand getItemsCommand;

    private final SettingsMenuCommand settingsMenuCommand;

    private final PageTurnerCommand pageTurnerCommand;

    private final LoadAllItemsCommand loadAllItemsCommand;

    private final EnteringItemTitleCommand enteringItemTitleCommand;

    private final SelectItemCommand selectItemCommand;

    private final LoadItemsByTitleCommand loadItemsByTitleCommand;

    private final ParseElements parseElements;

    private final UserService userService;

    private final RssFeedService rssFeedService;

    private final FavoriteItemService favoriteItemService;

    private final ItemsPagination itemsPagination;

    private final RssFeedCheckerRegistry feedCheckerRegistry;

    private final RssFeedsInlineKeyboard feedsInlineKeyboard;

    private final GetItemsInlineKeyboard getItemsInlineKeyboard;

    public CallbackQueryController(BotConfig botConfig, SubscribeCommand subscribeCommand,
                                   UnsubscribeCommand unsubscribeCommand, GetItemsCommand getItemsCommand,
                                   SettingsMenuCommand settingsMenuCommand, PageTurnerCommand pageTurnerCommand,
                                   RssFeedService rssFeedService, LoadAllItemsCommand loadAllItemsCommand,
                                   EnteringItemTitleCommand enteringItemTitleCommand,
                                   SelectItemCommand selectItemCommand, LoadItemsByTitleCommand loadItemsByTitleCommand,
                                   ParseElements parseElements, UserService userService,
                                   FavoriteItemService favoriteItemService, ItemsPagination itemsPagination,
                                   RssFeedCheckerRegistry feedCheckerRegistry,
                                   RssFeedsInlineKeyboard feedsInlineKeyboard,
                                   GetItemsInlineKeyboard getItemsInlineKeyboard) {
        this.subscribeCommand = subscribeCommand;
        this.botConfig = botConfig;
        this.unsubscribeCommand = unsubscribeCommand;
        this.getItemsCommand = getItemsCommand;
        this.settingsMenuCommand = settingsMenuCommand;
        this.pageTurnerCommand = pageTurnerCommand;
        this.rssFeedService = rssFeedService;
        this.loadAllItemsCommand = loadAllItemsCommand;
        this.enteringItemTitleCommand = enteringItemTitleCommand;
        this.selectItemCommand = selectItemCommand;
        this.loadItemsByTitleCommand = loadItemsByTitleCommand;
        this.parseElements = parseElements;
        this.userService = userService;
        this.favoriteItemService = favoriteItemService;
        this.itemsPagination = itemsPagination;
        this.feedCheckerRegistry = feedCheckerRegistry;
        this.feedsInlineKeyboard = feedsInlineKeyboard;
        this.getItemsInlineKeyboard = getItemsInlineKeyboard;
    }

    @CallbackQueryRequest
    public BaseRequest<?, ?> handle(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        System.out.println("Handle callback - " + callbackQuery.data());
        return handleCallback(update);
    }

    @CallbackQueryRequest(value = CallbackDataConstants.SUB_FEED_SAMPLE)
    public BaseRequest<AnswerCallbackQuery, BaseResponse> handleSampleFeedCallback(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        UserDB userDB = userService.findUser(callbackQuery.message().chat().id());
        rssFeedService.addFeed(userDB, CallbackDataConstants.SUB_FEED_SAMPLE);

        return new AnswerCallbackQuery(callbackQuery.id()).text("Subscribed successfully!" +
                "\nYou are now following the feed: " + CallbackDataConstants.SUB_FEED_SAMPLE)
                .cacheTime(5);
    }

    @CallbackQueryRequest(value = CallbackDataConstants.HIDE_MESSAGE)
    public BaseRequest<DeleteMessage, BaseResponse> handleHideMessageCallback(Update update) {
        long chatId = update.callbackQuery().message().chat().id();
        return new DeleteMessage(chatId, update.callbackQuery().message().messageId());
    }

    @CallbackQueryRequest(value = CallbackDataConstants.SUBSCRIBE)
    public BaseRequest<?, ?> handleSubscribeCallback(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        long chatId = callbackQuery.message().chat().id();
        Integer messageId = callbackQuery.message().messageId();
        // check if user tries to subscribe from unsubscribe menu when he has no subscribed feeds
        if (callbackQuery.message().text().equals(unsubscribeCommand.getAnswer())) {
            botConfig.getTelegramBot().execute(new DeleteMessage(chatId, messageId));
        }
        long feedCounter = rssFeedService.countUserFeeds(chatId);
        if (feedCounter >= 3)
            return new AnswerCallbackQuery(callbackQuery.id())
                    .text("Subscription limit reached! You cannot subscribe to more than 3 RSS feeds, " +
                            "unsubscribe from one feed first.")
                    .showAlert(true)
                    .cacheTime(5);
        botConfig.getTelegramBot().execute(new AnswerCallbackQuery(callbackQuery.id()));
        return subscribeCommand.execute(callbackQuery.message());
    }

    @CallbackQueryRequest(value = CallbackDataConstants.UNSUBSCRIBE)
    public BaseRequest<?, ?> handleUnsubscribeCallback(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        botConfig.getTelegramBot().execute(new AnswerCallbackQuery(callbackQuery.id()));
        return unsubscribeCommand.execute(callbackQuery.message());
    }

    @CallbackQueryRequest(value = CallbackDataConstants.GET_ITEMS)
    public BaseRequest<?, ?> handleGetItemsCallBack(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        itemsPagination.clear();
        feedsInlineKeyboard.setStateMenu(CallbackDataConstants.GET_ITEMS);
        return getItemsCommand.execute(callbackQuery.message());
    }

    @CallbackQueryRequest(value = CallbackDataConstants.LOAD_ALL_ITEMS)
    public BaseRequest<?, ?> handleLoadAllItemsCallback(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        RssFeed feed = feedsInlineKeyboard.getFeedList().get(feedsInlineKeyboard.getCurrentFeedIndex());
        parseElements.parseAllElements(feed.getId());
        selectItemCommand.setStateMenu(CallbackDataConstants.GET_ITEMS);
        return loadAllItemsCommand.execute(callbackQuery.message());
    }

    @CallbackQueryRequest(value = CallbackDataConstants.LOAD_BY_TITLE)
    public BaseRequest<?, ?> handleLoadItemsByTitleCallback(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        RssFeed feed = feedsInlineKeyboard.getFeedList().get(feedsInlineKeyboard.getCurrentFeedIndex());
        parseElements.findFeedAndParseRss(feed.getId());
        selectItemCommand.setStateMenu(CallbackDataConstants.GET_ITEMS);
        return enteringItemTitleCommand.execute(callbackQuery.message());
    }

    @CallbackQueryRequest(value = CallbackDataConstants.ITEMS_LIST)
    public BaseRequest<?, ?> handleItemsListCallback(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        long chatId = callbackQuery.message().chat().id();
        if (selectItemCommand.getStateMenu().equals(CallbackDataConstants.SETTINGS_MENU)) {
            List<FavoriteItem> favoriteItems = favoriteItemService.getAllFavoriteItems(chatId);
            return settingsMenuCommand.displayFavoritesItems(callbackQuery.message(), favoriteItems);
        }
        return loadItemsByTitleCommand.process(update, chatId);
    }

    @CallbackQueryRequest(value = CallbackDataConstants.SETTINGS_MENU)
    public BaseRequest<?, ?> handleSettingsCallback(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        feedsInlineKeyboard.setStateMenu(CallbackDataConstants.SETTINGS_MENU);
        return settingsMenuCommand.execute(callbackQuery.message());
    }

    @CallbackQueryRequest(value = CallbackDataConstants.FEED_SETTINGS)
    public BaseRequest<?, ?> handleFeedSettingsCallback(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        long chatId = callbackQuery.message().chat().id();
        List<RssFeed> feedList = rssFeedService.getAllFeeds(chatId);
        if (feedList.isEmpty()) {
            return new AnswerCallbackQuery(callbackQuery.id()).text("You don't have any subscribed RSS feeds yet!");
        }
        return settingsMenuCommand.displayRssFeedSettings(callbackQuery.message(), feedList);
    }

    @CallbackQueryRequest(value = CallbackDataConstants.FAVORITES)
    public BaseRequest<?, ?> handleFavoriteItemsCallback(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        long chatId = callbackQuery.message().chat().id();
        List<FavoriteItem> favoriteItems = favoriteItemService.getAllFavoriteItems(chatId);
        if (favoriteItems.isEmpty()) {
            return new AnswerCallbackQuery(callbackQuery.id()).text("You don't have any items in your favorites!");
        }
        selectItemCommand.setStateMenu(CallbackDataConstants.SETTINGS_MENU);
        return settingsMenuCommand.displayFavoritesItems(callbackQuery.message(), favoriteItems);
    }

    @CallbackQueryRequest(
            value = { CallbackDataConstants.ADD_TO_FAVORITES, CallbackDataConstants.REMOVE_FROM_FAVORITES }
    )
    public BaseRequest<?, ?> handleAddOrRemoveFavoriteItemCallback(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        long chatId = callbackQuery.message().chat().id();
        Integer messageId = callbackQuery.message().messageId();
        TelegramBot bot = botConfig.getTelegramBot();

        ItemModel currentItem = selectItemCommand.getCurrentlySelectedItemModel();
        UserDB user = userService.findUser(chatId);
        String answerCallbackText;
        if (callbackQuery.data().equals(CallbackDataConstants.ADD_TO_FAVORITES)) {
            rssFeedService.addFavoriteItem(user, currentItem);
            answerCallbackText = "Added to favorites!";
        } else {
            favoriteItemService.removeFromFavorites(user, currentItem.getTitle());
            answerCallbackText = "Removed from favorites!";
        }
        bot.execute(new AnswerCallbackQuery(callbackQuery.id()).text(answerCallbackText));
        return new EditMessageReplyMarkup(chatId, messageId)
                .replyMarkup(selectItemCommand.getSelectedItemInlineKeyboard().createInlineKeyboard());
    }

    @CallbackQueryRequest(
            value = { CallbackDataConstants.NEXT_PAGE, CallbackDataConstants.PREVIOUS_PAGE }
    )
    public BaseRequest<?, ?> handleTurningNextOrPreviousPageCallback(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        pageTurnerCommand.changePaginationIndex(callbackQuery.data());
        return pageTurnerCommand.execute(callbackQuery.message());
    }

    @CallbackQueryRequest(
            value = { CallbackDataConstants.FIRST_PAGE, CallbackDataConstants.LAST_PAGE }
    )
    public BaseRequest<?, ?> handleTurningFirstOrLastPageCallback(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        long chatId = callbackQuery.message().chat().id();
        return pageTurnerCommand.execute(chatId, callbackQuery.message().messageId(), callbackQuery.data());
    }

    @CallbackQueryRequest(
            value = { CallbackDataConstants.SCROLL_BACKWARD_FEED, CallbackDataConstants.SCROLL_FORWARD_FEED }
    )
    public BaseRequest<?, ?> handleScrollingFeedForwardOrBackwardCallback(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (feedsInlineKeyboard.getStateMenu().equals(CallbackDataConstants.SETTINGS_MENU)) {
            return settingsMenuCommand.updateFeedRowButtons(callbackQuery);
        } else {
            return feedsInlineKeyboard.handleFeedNavigation(callbackQuery, getItemsInlineKeyboard);
        }
    }

    @CallbackQueryRequest(
            value = { CallbackDataConstants.SETTINGS_FEED_POSTING, CallbackDataConstants.SETTINGS_FEED_INTERVAL }
    )
    public BaseRequest<?, ?> handleSettingsFeedOptionsCallback(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        return settingsMenuCommand.configureFeed(callbackQuery);
    }

    private BaseRequest<?, ?> handleCallback(Update update) { // handle other cases
        CallbackQuery callbackQuery = update.callbackQuery();
        Message message = callbackQuery.message();
        long chatId = message.chat().id();
        String callData = callbackQuery.data();
        Integer messageId = message.messageId();
        String callbackQueryId = callbackQuery.id();

        if (callData.startsWith(CallbackDataConstants.FEED_BUTTON)) {
            Integer feedId = Integer.parseInt(callData.substring(CallbackDataConstants.FEED_BUTTON.length()));
            if (message.text().startsWith("Your subscriptions")) {
                RssFeedChecker feedChecker = feedCheckerRegistry.getRssFeedChecker(feedId);
                if (feedChecker != null) {
                    feedChecker.stopChecking();
                    feedCheckerRegistry.removeRssFeedChecker(feedId);
                }
                rssFeedService.removeFeed(feedId);
                List<RssFeed> feedList = rssFeedService.getAllFeeds(chatId);
                feedsInlineKeyboard.setFeedList(feedList);

                botConfig.getTelegramBot().execute(new AnswerCallbackQuery(callbackQueryId).text("Unsubscribed!"));
                return new EditMessageReplyMarkup(chatId, messageId)
                        .replyMarkup(feedsInlineKeyboard.createInlineKeyboard());
            }
            return new AnswerCallbackQuery(callbackQueryId);
        } else if (Arrays.stream(CallbackDataConstants.DisplayOptions.values())
                .anyMatch(f -> f.getCallback().equals(callData))) {
            return settingsMenuCommand.saveDisplayOption(callbackQuery);
        } else if (callData.startsWith(CallbackDataConstants.SHOW_ITEM)) {
            selectItemCommand.setUser(userService.findUser(chatId));
            selectItemCommand.setCallData(callData);
            return selectItemCommand.execute(message);
        } else if (callData.startsWith(CallbackDataConstants.INTERVAL)) {
            SettingsMenuInlineKeyboard settingsInlineKeyboard = settingsMenuCommand.getSettingsMenuInlineKeyboard();
            List<RssFeed> feedList = settingsInlineKeyboard.getFeedList();
            RssFeed selectedFeed = feedList.get(feedsInlineKeyboard.getCurrentFeedIndex());

            int monitoringInterval = Integer.parseInt(callData.substring(CallbackDataConstants.INTERVAL.length()));
            selectedFeed.setInterval(monitoringInterval);
            rssFeedService.updateFeed(selectedFeed);

            RssFeedChecker feedChecker = feedCheckerRegistry.getRssFeedChecker(selectedFeed.getId());
            feedChecker.setInterval(monitoringInterval);

            return settingsMenuCommand.displayRssFeedSettings(message, feedList);
        } else if (itemsPagination.getCallbackDataPaginationButtons() != null &&
                itemsPagination.getCallbackDataPaginationButtons().contains(callData)) {
            return loadItemsByTitleCommand.execute(chatId, messageId, callData);
        }
        return new AnswerCallbackQuery(callbackQueryId);
    }

    @Override
    public String getToken() {
        return botConfig.getBotToken();
    }
}
