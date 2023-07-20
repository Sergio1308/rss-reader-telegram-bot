package com.company.RssReaderBot.controllers.core;

/**
 * Enumeration representing different states of the bot's interaction with the user.
 * The bot can be in one of the following states:
 * 1. SUBSCRIBE: The user is in the process of subscribing to an RSS feed by entering a URL.
 * 2. ENTER_TITLE: The user is in the process of entering a title for searching items.
 * 3. ENTER_DATE: The user is in the process of entering a date for searching items.
 * 4. GET_ELEMENTS: The user is in the process of viewing elements/items from an RSS feed.
 * 5. SETTINGS: The user is in the settings menu to configure bot options.
 * 6. NONE: The default state when the bot is not in any specific interaction with the user.
 */
public enum BotState {
    SUBSCRIBE,
    ENTER_TITLE,
    ENTER_DATE,
    GET_ELEMENTS,
    SETTINGS,
    NONE
}
