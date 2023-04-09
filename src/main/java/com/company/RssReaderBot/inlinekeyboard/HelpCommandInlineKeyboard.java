package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.controllers.CallbackQueryConstants;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public class HelpCommandInlineKeyboard implements InlineKeyboardCreator {

    private static final String rssWikiLink = "https://en.wikipedia.org/wiki/RSS";
    private static final String rssExampleLink = "https://news.yahoo.com/rss/us";

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Subscribe to sample feed: NYT > Sports")
                                .callbackData(CallbackQueryConstants.SUB_FEED_SAMPLE)
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("More about RSS (What is RSS?)").url(rssWikiLink)
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Sample feed URL").url(rssExampleLink)
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Hide this message").callbackData(CallbackQueryConstants.HIDE_MESSAGE)
                }
        );
    }
}
