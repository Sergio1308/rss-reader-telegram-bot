package com.company.RssReaderBot.handlers;

import com.company.RssReaderBot.commands.*;
import com.company.RssReaderBot.commands.personal_menu.ShowPersonalMenuCommand;
import com.company.RssReaderBot.entities.ItemsPagination;
import com.company.RssReaderBot.parser.ParseElements;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CallbackHandler implements Handler {

    // commands classes fields
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

    @Override
    public void handle(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        if (callbackQuery == null) return;
        System.out.println("Handle callback - " + callbackQuery.getData());
        handleCallback(callbackQuery, update);
    }

    @Override
    public boolean hasHandler(Update update) {
        return update.hasCallbackQuery();
    }

    private void handleCallback(CallbackQuery callbackQuery, Update update) {
        String callData = callbackQuery.getData();
        long messageId = callbackQuery.getMessage().getMessageId();
        long chatId = callbackQuery.getMessage().getChatId();
        // todo: polymorphism instead of if
        if (callData.equals(CallbackVars.MAIN_MENU)) {
            ItemsPagination.getInstance().clear();
            loadMainMenuCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackVars.START_MENU) || callData.equals(CallbackVars.CHANGE_RSS_URL)) {
            startCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackVars.RSS_FAQ)) {
            showRssFaqCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackVars.PERSONAL_MENU)) {
            showPersonalMenuCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackVars.LOAD_ALL_ITEMS)) {
            ParseElements parseElements = new ParseElements();
            parseElements.parseAllElements();
            loadAllItemsCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackVars.LOAD_BY_TITLE)) {
            enteringItemTitleCommand.execute(chatId, messageId);
        } else if (callData.startsWith(CallbackVars.SELECTED_BY_TITLE_CALLBACK)) {
            SelectItemCommand.setCallData(callData);
            selectItemCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackVars.RETURN_LOAD_BY_TITLE)) {
            // todo return to already generated items list, remember previous page
            // edit reply markup
            loadItemsByTitleCommand.process(update, chatId);
        } else if (callData.equals(CallbackVars.ITEM_DESCRIPTION)) {
            getItemDescriptionCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackVars.ITEM_OTHER_DETAILS)) {
            getItemOtherDetailsCommand.execute(chatId, messageId);
        } else if (ItemsPagination.getCallbackDataPaginationButtons() != null && // ?
                    ItemsPagination.getCallbackDataPaginationButtons().contains(callData)) {
            // todo update only message text & items list
            loadItemsByTitleCommand.execute(chatId, messageId, callData);
        } else if (callData.equals(CallbackVars.NEXT_PAGE) || callData.equals(CallbackVars.PREVIOUS_PAGE)) {
            turnPageCommand.changePaginationIndex(callData);
            turnPageCommand.execute(chatId, messageId);
        } else if (callData.equals(CallbackVars.FIRST_PAGE) || callData.equals(CallbackVars.LAST_PAGE)) {
            turnToFirstOrLastPageCommand.execute(chatId, messageId, callData);
        }
    }
}
