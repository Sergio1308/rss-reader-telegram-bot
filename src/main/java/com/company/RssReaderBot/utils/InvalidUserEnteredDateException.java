package com.company.RssReaderBot.utils;

public class InvalidUserEnteredDateException extends Exception {
    public InvalidUserEnteredDateException(String message) {
        super(message);
    }

    public InvalidUserEnteredDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
