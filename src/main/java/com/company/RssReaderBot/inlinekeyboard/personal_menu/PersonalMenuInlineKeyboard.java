package com.company.RssReaderBot.inlinekeyboard.personal_menu;

import com.company.RssReaderBot.handlers.CallbackVars;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class PersonalMenuInlineKeyboard implements InlineKeyboardCreator {

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        InlineKeyboardButton myFavoritesButton = createInlineKeyboardButton(
                "My Favorites", CallbackVars.FAVORITES);
        InlineKeyboardButton changeRssUrlButton = createInlineKeyboardButton(
                "Change my RSS URL", CallbackVars.CHANGE_RSS_URL);
        InlineKeyboardButton alertsAndTitleButton = createInlineKeyboardButton(
                "Alerts & Saved title for item parsing", CallbackVars.ALERTS_AND_TITLE_CONF);
        InlineKeyboardButton updateDataButton = createInlineKeyboardButton(
                "Update my user data", CallbackVars.UPDATE_USER_DATA);

        List<InlineKeyboardButton> rowInline1 = createInlineKeyboardButtonRowInline(myFavoritesButton);
        List<InlineKeyboardButton> rowInline2 = createInlineKeyboardButtonRowInline(changeRssUrlButton);
        List<InlineKeyboardButton> rowInline3 = createInlineKeyboardButtonRowInline(alertsAndTitleButton);
        List<InlineKeyboardButton> rowInline4 = createInlineKeyboardButtonRowInline(updateDataButton);
        List<InlineKeyboardButton> rowInline5 = createInlineKeyboardButtonRowInline(
                createInlineKeyboardButton("Back to main menu", CallbackVars.MAIN_MENU));

        List<List<InlineKeyboardButton>> rowList = createInlineKeyboardButtonRowList(rowInline1);
        rowList.add(rowInline2);
        rowList.add(rowInline3);
        rowList.add(rowInline4);
        rowList.add(rowInline5);
        markupInline.setKeyboard(rowList);
        return markupInline;
    }
}
