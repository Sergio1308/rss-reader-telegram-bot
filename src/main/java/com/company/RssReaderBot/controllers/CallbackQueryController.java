package com.company.RssReaderBot.controllers;

import com.company.RssReaderBot.commands.*;
import com.company.RssReaderBot.commands.SettingsMenuCommand;
import com.company.RssReaderBot.config.BotConfig;
import com.company.RssReaderBot.db.models.RssFeed;
import com.company.RssReaderBot.db.models.UserDB;
import com.company.RssReaderBot.db.repositories.UserRepository;
import com.company.RssReaderBot.entities.ItemsPagination;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.RssFeedsInlineKeyboard;
import com.company.RssReaderBot.services.parser.ParseElements;
import com.company.RssReaderBot.services.FeedService;
import com.company.RssReaderBot.services.UserService;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final FeedService feedService;

    private final Map<String, Command<Message>> commandsMap = new HashMap<>();

    private Integer feedId = null;

    public CallbackQueryController(BotConfig botConfig, SubscribeCommand subscribeCommand,
                                   UnsubscribeCommand unsubscribeCommand, GetItemsCommand getItemsCommand,
                                   SettingsMenuCommand settingsMenuCommand, PageTurnerCommand pageTurnerCommand,
                                   FeedService feedService, LoadAllItemsCommand loadAllItemsCommand,
                                   EnteringItemTitleCommand enteringItemTitleCommand,
                                   SelectItemCommand selectItemCommand,
                                   LoadItemsByTitleCommand loadItemsByTitleCommand,
                                   ParseElements parseElements, UserService userService) {
//        commandsMap.put(CallbackDataConstants.PARSING_ELEMENTS_MENU, new GetItemsCommand());
//        commandsMap.put(CallbackDataConstants.START_MENU, new StartCommand());
//        commandsMap.put(CallbackDataConstants.CHANGE_RSS_URL, new SubscribeCommand());
//        commandsMap.put(CallbackDataConstants.RSS_FAQ, new ShowRssFaqCommand());
//        commandsMap.put(CallbackDataConstants.SETTINGS, new SettingsMenuCommand());
//        commandsMap.put(CallbackDataConstants.LOAD_ALL_ITEMS, new LoadAllItemsCommand());
//        commandsMap.put(CallbackDataConstants.LOAD_BY_TITLE, new EnteringItemTitleCommand());
//        commandsMap.put(CallbackDataConstants.SHOW_ITEM, new SelectItemCommand());
//        commandsMap.put(CallbackDataConstants.RETURN_LOAD_BY_TITLE, new LoadItemsByTitleCommand());
////        loadItemsByTitleCommand
//        commandsMap.put(CallbackDataConstants.NEXT_PAGE, new PageTurnerCommand());
//        commandsMap.put(CallbackDataConstants.PREVIOUS_PAGE, new PageTurnerCommand());
//        commandsMap.put(CallbackDataConstants.FIRST_PAGE, new TurnToFirstOrLastPageCommand());
//        commandsMap.put(CallbackDataConstants.LAST_PAGE, new TurnToFirstOrLastPageCommand());
        this.subscribeCommand = subscribeCommand;
        this.botConfig = botConfig;
        this.unsubscribeCommand = unsubscribeCommand;
        this.getItemsCommand = getItemsCommand;
        this.settingsMenuCommand = settingsMenuCommand;
        this.pageTurnerCommand = pageTurnerCommand;
        this.feedService = feedService;
        this.loadAllItemsCommand = loadAllItemsCommand;
        this.enteringItemTitleCommand = enteringItemTitleCommand;
        this.selectItemCommand = selectItemCommand;
        this.loadItemsByTitleCommand = loadItemsByTitleCommand;
        this.parseElements = parseElements;
        this.userService = userService;
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
        feedService.addFeed(userDB, CallbackDataConstants.SUB_FEED_SAMPLE);

        return new AnswerCallbackQuery(callbackQuery.id()).text(
                "Subscribed successfully!\nYou are now following the feed: "
                        + CallbackDataConstants.SUB_FEED_SAMPLE) // todo feed title
                .cacheTime(10);
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
        long feedCounter = feedService.countUserFeeds(chatId);
        if (feedCounter >= 3)
            return new AnswerCallbackQuery(callbackQuery.id())
                    .text("Subscription limit reached! You cannot subscribe to more than 3 RSS feeds, " +
                            "unsubscribe from one feed first.")
                    .showAlert(true)
                    .cacheTime(20);
        botConfig.getTelegramBot().execute(new AnswerCallbackQuery(callbackQuery.id()));
        return subscribeCommand.execute(callbackQuery.message());
    }

    @CallbackQueryRequest(value = CallbackDataConstants.UNSUBSCRIBE)
    public BaseRequest<?, ?> handleUnsubscribeCallback(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        botConfig.getTelegramBot().execute(new AnswerCallbackQuery(callbackQuery.id()));
        return unsubscribeCommand.execute(callbackQuery.message());
    }

    private BaseRequest<?, ?> handleCallback(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        String callData = callbackQuery.data();
        Message message = callbackQuery.message();
        long chatId = callbackQuery.message().chat().id();
        Integer messageId = callbackQuery.message().messageId();
        String callbackQueryId = callbackQuery.id();

        // todo: polymorphism
        if (callData.equals(CallbackDataConstants.PARSING_ELEMENTS_MENU)) {
            ItemsPagination.getInstance().clear();
            return getItemsCommand.execute(message);
        } else if (callData.startsWith(CallbackDataConstants.FEED_BUTTON)) {
            feedId = Integer.parseInt(callData.substring(CallbackDataConstants.FEED_BUTTON.length()));
            if (message.text().startsWith("Your subscriptions")) {
                feedService.removeFeed(feedId);
                List<RssFeed> feedList = feedService.getAllFeeds(chatId);
                InlineKeyboardCreator inlineKeyboardCreator = new RssFeedsInlineKeyboard(feedList);
                botConfig.getTelegramBot().execute(new AnswerCallbackQuery(callbackQueryId).text("Unsubscribed!"));
                return new EditMessageReplyMarkup(chatId, messageId)
                        .replyMarkup(inlineKeyboardCreator.createInlineKeyboard());
            }
            return new AnswerCallbackQuery(callbackQueryId).text("Successfully selected!");
        } else if (callData.equals(CallbackDataConstants.SETTINGS)) {
            return settingsMenuCommand.execute(message);
        } else if (callData.equals(CallbackDataConstants.LOAD_ALL_ITEMS)) {
            //
            if (feedId == null) {
                return new AnswerCallbackQuery(callbackQueryId).text("You have not selected an RSS feed!");
            }
            parseElements.parseAllElements(feedId);
            return loadAllItemsCommand.execute(message);
        } else if (callData.equals(CallbackDataConstants.LOAD_BY_TITLE)) {
            //
            if (feedId == null) {
                return new AnswerCallbackQuery(callbackQueryId).text("You have not selected an RSS feed!");
            }
            parseElements.getRssFeedAndSetToParser(feedId);
            return enteringItemTitleCommand.execute(message);
        } else if (callData.startsWith(CallbackDataConstants.SHOW_ITEM)) {
            SelectItemCommand.setCallData(callData);
            return selectItemCommand.execute(message);
        } else if (callData.equals(CallbackDataConstants.RETURN_LOAD_BY_TITLE)) {
            // todo return to already generated items list, remember previous page
            // edit reply markup
            return loadItemsByTitleCommand.process(update, chatId);
        } else if (ItemsPagination.getCallbackDataPaginationButtons() != null &&
                ItemsPagination.getCallbackDataPaginationButtons().contains(callData)) {
            // todo update only message text & items list
            return loadItemsByTitleCommand.execute(chatId, messageId, callData);
        } else if (callData.equals(CallbackDataConstants.NEXT_PAGE) || callData.equals(CallbackDataConstants.PREVIOUS_PAGE)) {
            pageTurnerCommand.changePaginationIndex(callData);
            return pageTurnerCommand.execute(message);
        } else if (callData.equals(CallbackDataConstants.FIRST_PAGE) || callData.equals(CallbackDataConstants.LAST_PAGE)) {
            return pageTurnerCommand.execute(chatId, messageId, callData);
        }
        return null;
    }

    @Override
    public String getToken() {
        return botConfig.getBotToken();
    }
}
