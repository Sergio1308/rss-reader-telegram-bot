package com.company.RssReaderBot.controllers;

import com.company.RssReaderBot.db.entities.UserSettings;
import lombok.Getter;

public class CallbackDataConstants {
    // main menu
    public static final String START_MENU = "/start";
    public static final String SUBSCRIBE = "subscribe";
    public static final String UNSUBSCRIBE = "unsubscribe";
    public static final String SETTINGS_MENU = "settings_menu";
    // items list
    public static final String GET_ITEMS = "get_items";
    public static final String SHOW_ITEM = "show_item_";
    public static final String FEED_BUTTON = "rss_feed_";
    public static final String LOAD_ALL_ITEMS = "load_all_items";
    public static final String LOAD_BY_TITLE = "load_by_title";
    public static final String LOAD_BY_DATE = "load_by_date";
    public static final String FAVORITES = "favorites_items";
    public static final String ADD_TO_FAVORITES = "add_to_favorites";
    public static final String REMOVE_FROM_FAVORITES = "remove_from_favorites";
    public static final String ITEMS_LIST = "load_by_title_inline";
    // pagination items
    public static final String NEXT_PAGE = "next_page";
    public static final String PREVIOUS_PAGE = "previous_page";
    public static final String FIRST_PAGE = "first_page";
    public static final String LAST_PAGE = "last_page";
    // other
    public static final String SUB_FEED_SAMPLE = "https://rss.nytimes.com/services/xml/rss/nyt/Sports.xml";
    public static final String HIDE_MESSAGE = "hide_message";
    // settings menu
    public static final String SCROLL_BACKWARD_FEED = "scroll_backward";
    public static final String SCROLL_FORWARD_FEED = "scroll_forward";
    public static final String SETTINGS_SELECTED_FEED = "settings_selected_feed";
    public static final String SETTINGS_FEED_POSTING = "settings_feed_posting";
    public static final String SETTINGS_FEED_INTERVAL = "settings_feed_interval";
    public static final String FEED_SETTINGS = "feed_settings";
    public static final String INTERVAL = "interval_";

    public enum DisplayOptions {
        DISPLAY_TITLE("display_title") {
            @Override
            public void setDisplayValue(UserSettings userSettings) {
                boolean currentBool = userSettings.isDisplayTitle();
                userSettings.setDisplayTitle(!currentBool);
            }
        },
        DISPLAY_DESCRIPTION("display_description") {
            @Override
            public void setDisplayValue(UserSettings userSettings) {
                boolean currentBool = userSettings.isDisplayDescription();
                userSettings.setDisplayDescription(!currentBool);
            }
        },
        DISPLAY_DATE("display_date") {
            @Override
            public void setDisplayValue(UserSettings userSettings) {
                boolean currentBool = userSettings.isDisplayDate();
                userSettings.setDisplayDate(!currentBool);
            }
        },
        DISPLAY_MEDIA("display_media") {
            @Override
            public void setDisplayValue(UserSettings userSettings) {
                boolean currentBool = userSettings.isDisplayMedia();
                userSettings.setDisplayMedia(!currentBool);
            }
        },
        DISPLAY_LINK("display_link") {
            @Override
            public void setDisplayValue(UserSettings userSettings) {
                boolean currentBool = userSettings.isDisplayLink();
                userSettings.setDisplayLink(!currentBool);
            }
        };

        public abstract void setDisplayValue(UserSettings userSettings);

        @Getter
        private final String callback;

        DisplayOptions(String callback) {
            this.callback = callback;
        }
    }
}
