package com.company.RssReaderBot.utils;

public class InvalidPubDateException extends Exception {
    public InvalidPubDateException(String message) {
        super(message);
    }

    public InvalidPubDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
