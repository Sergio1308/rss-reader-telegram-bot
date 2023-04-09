package com.company.RssReaderBot.controllers;

import com.company.RssReaderBot.commands.*;
import com.company.RssReaderBot.commands.personal_menu.ShowPersonalMenuCommand;
import com.company.RssReaderBot.config.BotConfig;
import com.company.RssReaderBot.db.models.UserDB;
import com.company.RssReaderBot.db.repositories.UserRepository;
import com.company.RssReaderBot.entities.ItemsPagination;
import com.company.RssReaderBot.services.parser.ParseElements;
import com.company.RssReaderBot.services.FeedService;
import com.company.RssReaderBot.services.UserService;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@BotController
public class CallbackQueryController implements TelegramMvcController, Controller {

    @Autowired
    private BotConfig botConfig;

    @Autowired
    private SubscribeCommand subscribeCommand;

    @Autowired
    private UnsubscribeCommand unsubscribeCommand;

    //todo
    private final LoadMainMenuCommand loadMainMenuCommand = new LoadMainMenuCommand();
    private final ShowPersonalMenuCommand showPersonalMenuCommand = new ShowPersonalMenuCommand();
    private final LoadAllItemsCommand loadAllItemsCommand = new LoadAllItemsCommand();
    private final EnteringItemTitleCommand enteringItemTitleCommand = new EnteringItemTitleCommand();
    private final SelectItemCommand selectItemCommand = new SelectItemCommand();
    private final TurnPageCommand turnPageCommand = new TurnPageCommand();
    private final LoadItemsByTitleCommand loadItemsByTitleCommand = new LoadItemsByTitleCommand();
    private final TurnToFirstOrLastPageCommand turnToFirstOrLastPageCommand = new TurnToFirstOrLastPageCommand();

    @Autowired
    private ParseElements parseElements;

    @Autowired
    private UserService userService;

    @Autowired
    private FeedService feedService;

    private final UserRepository userRepository;

    private final Map<String, Command<Long, Integer>> commandsMap = new HashMap<>();

    public CallbackQueryController(UserRepository userRepository) {
        this.userRepository = userRepository;
//        commandsMap.put(CallbackQueryConstants.PARSING_ELEMENTS_MENU, new LoadMainMenuCommand());
//        commandsMap.put(CallbackQueryConstants.START_MENU, new StartCommand());
//        commandsMap.put(CallbackQueryConstants.CHANGE_RSS_URL, new SubscribeCommand());
//        commandsMap.put(CallbackQueryConstants.RSS_FAQ, new ShowRssFaqCommand());
//        commandsMap.put(CallbackQueryConstants.SETTINGS, new ShowPersonalMenuCommand());
//        commandsMap.put(CallbackQueryConstants.LOAD_ALL_ITEMS, new LoadAllItemsCommand());
//        commandsMap.put(CallbackQueryConstants.LOAD_BY_TITLE, new EnteringItemTitleCommand());
//        commandsMap.put(CallbackQueryConstants.SHOW_ITEM, new SelectItemCommand());
//        commandsMap.put(CallbackQueryConstants.RETURN_LOAD_BY_TITLE, new LoadItemsByTitleCommand());
////        loadItemsByTitleCommand
//        commandsMap.put(CallbackQueryConstants.NEXT_PAGE, new TurnPageCommand());
//        commandsMap.put(CallbackQueryConstants.PREVIOUS_PAGE, new TurnPageCommand());
//        commandsMap.put(CallbackQueryConstants.FIRST_PAGE, new TurnToFirstOrLastPageCommand());
//        commandsMap.put(CallbackQueryConstants.LAST_PAGE, new TurnToFirstOrLastPageCommand());
    }

    @CallbackQueryRequest
    public BaseRequest<?, ?> handleOtherCallbacks(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        System.out.println("Handle callback - " + callbackQuery.data());
        return handleCallback(update);
    }

    @CallbackQueryRequest(value = CallbackQueryConstants.SUB_FEED_SAMPLE)
    public BaseRequest<AnswerCallbackQuery, BaseResponse> handleSampleFeedCallback(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        UserDB userDB = userService.findUser(callbackQuery.message().chat().id());
        feedService.addFeed(userDB, CallbackQueryConstants.SUB_FEED_SAMPLE);

        return new AnswerCallbackQuery(callbackQuery.id()).text(
                "Subscribed successfully!\nYou are now following the feed: "
                        + CallbackQueryConstants.SUB_FEED_SAMPLE)
                .cacheTime(10);
    }

    @CallbackQueryRequest(value = CallbackQueryConstants.HIDE_MESSAGE)
    public BaseRequest<DeleteMessage, BaseResponse> handleHideMessageCallback(Update update) {
        long chatId = update.callbackQuery().message().chat().id();
        return new DeleteMessage(chatId, update.callbackQuery().message().messageId());
    }

    @CallbackQueryRequest(value = CallbackQueryConstants.SUBSCRIBE)
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
        return subscribeCommand.execute(chatId, messageId);
    }

    @CallbackQueryRequest(value = CallbackQueryConstants.UNSUBSCRIBE)
    public BaseRequest<?, ?> handleUnsubscribeCallback(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        botConfig.getTelegramBot().execute(new AnswerCallbackQuery(callbackQuery.id()));
        return unsubscribeCommand.execute(callbackQuery.message());
    }

    private BaseRequest<?, ?> handleCallback(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        String callData = callbackQuery.data();
        long chatId = callbackQuery.message().chat().id();
        Integer messageId = callbackQuery.message().messageId();

        // todo: polymorphism
        if (callData.equals(CallbackQueryConstants.PARSING_ELEMENTS_MENU)) {
            ItemsPagination.getInstance().clear();
            return loadMainMenuCommand.execute(chatId, messageId);
        } else if (callData.startsWith(CallbackQueryConstants.FEED_BUTTON)) {
            // show all feeds as inline buttons, after pressing button - remove chosen feed from db
            System.out.println(callData);
        } else if (callData.equals(CallbackQueryConstants.SETTINGS)) {
            return showPersonalMenuCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackQueryConstants.LOAD_ALL_ITEMS)) {
            parseElements.parseAllElements(userRepository.findById(chatId).orElseThrow());
            return loadAllItemsCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackQueryConstants.LOAD_BY_TITLE)) {
            return enteringItemTitleCommand.execute(chatId, messageId);
        } else if (callData.startsWith(CallbackQueryConstants.SHOW_ITEM)) {
            SelectItemCommand.setCallData(callData);
            return selectItemCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackQueryConstants.RETURN_LOAD_BY_TITLE)) {
            // todo return to already generated items list, remember previous page
            // edit reply markup
            return loadItemsByTitleCommand.process(update, chatId);
        } else if (ItemsPagination.getCallbackDataPaginationButtons() != null &&
                ItemsPagination.getCallbackDataPaginationButtons().contains(callData)) {
            // todo update only message text & items list
            return loadItemsByTitleCommand.execute(chatId, messageId, callData);
        } else if (callData.equals(CallbackQueryConstants.NEXT_PAGE) || callData.equals(CallbackQueryConstants.PREVIOUS_PAGE)) {
            turnPageCommand.changePaginationIndex(callData);
            return turnPageCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackQueryConstants.FIRST_PAGE) || callData.equals(CallbackQueryConstants.LAST_PAGE)) {
            return turnToFirstOrLastPageCommand.execute(chatId, messageId, callData);
        }
        return null;
    }

    @Override
    public String getToken() {
        return botConfig.getBotToken();
    }
}
