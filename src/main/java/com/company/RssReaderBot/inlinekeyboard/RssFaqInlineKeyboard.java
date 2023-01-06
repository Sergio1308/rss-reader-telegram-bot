package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.handlers.CallbackVars;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.List;

public class RssFaqInlineKeyboard implements InlineKeyboardCreator {

    private static final String rssWikiLink = "https://en.wikipedia.org/wiki/RSS";
    private static final String rssExampleLink = "https://news.yahoo.com/rss/us";

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("More about RSS (link to Wiki)").url(rssWikiLink)
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("RSS link example").url(rssExampleLink)
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Back").callbackData(CallbackVars.START_MENU)
                }
        );
    }
}
