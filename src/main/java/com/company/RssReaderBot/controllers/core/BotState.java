package com.company.RssReaderBot.controllers.core;

/**
 * Enumeration representing different states of the bot's interaction with the user.
 * The bot can be in one of the following states:
 * 1. SUBSCRIBE: The user is in the process of subscribing to an RSS feed by entering a URL.
 * 2. ENTERING_TITLE: The user is in the process of entering a title for searching items.
 * 3. ENTERING_DATE: The user is in the process of entering a date for searching items.
 * 4. GET_ELEMENTS: The user is in the process of viewing elements (items) from an RSS feed.
 * 5. ENTERING_SPECIFIC_URL: The user is in the process of entering specific RSS feed URL to get items from it.
 * 6. SETTINGS: The user is in the settings menu to configure bot options.
 * 7. NONE: The default state when the bot is not in any specific interaction with the user.
 */
public enum BotState {
    SUBSCRIBE,
    ENTERING_TITLE,
    ENTERING_DATE,
    GET_ELEMENTS,
    ENTERING_SPECIFIC_URL,
    SETTINGS,
    NONE
}
