package com.company.RssReaderBot.services;

/**
 * This class has a method which return cut text if the title is too long
 * (avoiding error 400 Bad Request: BUTTON_DATA_INVALID, callback text is longer than 64 characters)
 * According to Telegram API, the max text length in the button callback data shouldn't exceed 64 bytes
 */
public class CallbackDataCutter {

    private static final int MAX_TITLE_LENGTH = 40;
    private static final int MAX_BUTTON_TEXT_LENGTH = 20;

    private static boolean isLonger(String title) {
        return title.length() >= MAX_TITLE_LENGTH;
    }

    public static String cutText(String title) {
        return isLonger(title) ? title.substring(title.length() - MAX_BUTTON_TEXT_LENGTH) : title;
    }
}
