package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.commands.SelectItemCommand;
import com.company.RssReaderBot.handlers.CallbackVars;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class DescriptionAndLinkInlineKeyboard implements InlineKeyboardCreator {
    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        InlineKeyboardButton linkButton = createInlineKeyboardButton("Open download link", "");
        linkButton.setUrl(SelectItemCommand.getCurrentlySelectedEpisode().getAudio().getUrl());
        List<InlineKeyboardButton> rowInline1 = createInlineKeyboardButtonRowInline(linkButton);

        List<InlineKeyboardButton> rowInline2 = createInlineKeyboardButtonRowInline(
                createInlineKeyboardButton("Back", CallbackVars.SELECTED_BY_TITLE_CALLBACK)
        );
        List<List<InlineKeyboardButton>> rowList = createInlineKeyboardButtonRowList(rowInline1);
        rowList.add(rowInline2);

        markupInline.setKeyboard(rowList);
        return markupInline;
    }
}
