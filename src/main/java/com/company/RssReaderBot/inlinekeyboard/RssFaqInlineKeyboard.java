package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.handlers.CallbackVars;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class RssFaqInlineKeyboard implements InlineKeyboardCreator {

    private static final String rssWikiLink = "https://en.wikipedia.org/wiki/RSS";
    private static final String rssExampleLink = "https://news.yahoo.com/rss/us";

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        InlineKeyboardButton wikiLinkButton = createInlineKeyboardButton(
                "More about RSS (link to Wiki)", "");
        wikiLinkButton.setUrl(rssWikiLink);
        InlineKeyboardButton rssExampleLinkButton = createInlineKeyboardButton(
                "RSS link example", "");
        rssExampleLinkButton.setUrl(rssExampleLink);

        List<InlineKeyboardButton> rowInline1 = createInlineKeyboardButtonRowInline(wikiLinkButton);
        List<InlineKeyboardButton> rowInline2 = createInlineKeyboardButtonRowInline(rssExampleLinkButton);
        List<InlineKeyboardButton> rowInline3 = createInlineKeyboardButtonRowInline(
                createInlineKeyboardButton("Back", CallbackVars.START_MENU));

        List<List<InlineKeyboardButton>> rowList = createInlineKeyboardButtonRowList(rowInline1);
        rowList.add(rowInline2);
        rowList.add(rowInline3);
        markupInline.setKeyboard(rowList);
        return markupInline;
    }
}
