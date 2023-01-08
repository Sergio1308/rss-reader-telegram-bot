package com.company.RssReaderBot.handlers;

import com.company.RssReaderBot.commands.*;
import com.company.RssReaderBot.commands.personal_menu.ShowPersonalMenuCommand;
import com.company.RssReaderBot.config.BotConfig;
import com.company.RssReaderBot.core.RssReaderBot;
import com.company.RssReaderBot.entities.ItemsPagination;
import com.company.RssReaderBot.parser.ParseElements;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import org.springframework.beans.factory.annotation.Autowired;

@BotController
public class CallbackHandler implements TelegramMvcController, Handler {

    @Autowired
    private BotConfig botConfig;

    // commands classes fields
    // todo
    private final StartCommand startCommand = new StartCommand();
    private final LoadMainMenuCommand loadMainMenuCommand = new LoadMainMenuCommand();
    private final ShowRssFaqCommand showRssFaqCommand = new ShowRssFaqCommand();
    private final ShowPersonalMenuCommand showPersonalMenuCommand = new ShowPersonalMenuCommand();
    private final LoadAllItemsCommand loadAllItemsCommand = new LoadAllItemsCommand();
    private final EnteringItemTitleCommand enteringItemTitleCommand = new EnteringItemTitleCommand();
    private final SelectItemCommand selectItemCommand = new SelectItemCommand();
    private final GetItemDescriptionCommand getItemDescriptionCommand = new GetItemDescriptionCommand();
    private final GetItemOtherDetailsCommand getItemOtherDetailsCommand = new GetItemOtherDetailsCommand();
    private final TurnPageCommand turnPageCommand = new TurnPageCommand();
    private final LoadItemsByTitleCommand loadItemsByTitleCommand = new LoadItemsByTitleCommand();
    private final TurnToFirstOrLastPageCommand turnToFirstOrLastPageCommand = new TurnToFirstOrLastPageCommand();

    @CallbackQueryRequest
    public BaseRequest handle(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        System.out.println("Handle callback - " + callbackQuery.data());
        return handleCallback(callbackQuery, update);
    }

    private BaseRequest handleCallback(CallbackQuery callbackQuery, Update update) {
        String callData = callbackQuery.data();
        Integer messageId = callbackQuery.message().messageId();
        long chatId = callbackQuery.message().chat().id();
        // todo: polymorphism instead of if
        if (callData.equals(CallbackVars.MAIN_MENU)) {
            ItemsPagination.getInstance().clear();
            return loadMainMenuCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackVars.START_MENU) || callData.equals(CallbackVars.CHANGE_RSS_URL)) {
            return startCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackVars.RSS_FAQ)) {
            return showRssFaqCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackVars.PERSONAL_MENU)) {
            return showPersonalMenuCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackVars.LOAD_ALL_ITEMS)) {
            ParseElements parseElements = new ParseElements();
            parseElements.parseAllElements();
            return loadAllItemsCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackVars.LOAD_BY_TITLE)) {
            return enteringItemTitleCommand.execute(chatId, messageId);
        } else if (callData.startsWith(CallbackVars.SELECTED_BY_TITLE_CALLBACK)) {
            SelectItemCommand.setCallData(callData);
            return selectItemCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackVars.RETURN_LOAD_BY_TITLE)) {
            // todo return to already generated items list, remember previous page
            // edit reply markup
            return loadItemsByTitleCommand.process(update, chatId);
        } else if (callData.equals(CallbackVars.ITEM_DESCRIPTION)) {
            return getItemDescriptionCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackVars.ITEM_OTHER_DETAILS)) {
            return getItemOtherDetailsCommand.execute(chatId, messageId);
        } else if (ItemsPagination.getCallbackDataPaginationButtons() != null && // ?
                    ItemsPagination.getCallbackDataPaginationButtons().contains(callData)) {
            // todo update only message text & items list
            return loadItemsByTitleCommand.execute(chatId, messageId, callData);
        } else if (callData.equals(CallbackVars.NEXT_PAGE) || callData.equals(CallbackVars.PREVIOUS_PAGE)) {
            turnPageCommand.changePaginationIndex(callData);
            return turnPageCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackVars.FIRST_PAGE) || callData.equals(CallbackVars.LAST_PAGE)) {
            return turnToFirstOrLastPageCommand.execute(chatId, messageId, callData);
        }
        return null;
    }

    @Override
    public String getToken() {
        return botConfig.getBotToken();
    }
}
