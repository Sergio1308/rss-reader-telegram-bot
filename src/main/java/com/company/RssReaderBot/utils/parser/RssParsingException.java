package com.company.RssReaderBot.utils.parser;

public class RssParsingException extends Exception {
    public RssParsingException(String message) {
        super(message);
    }

    public RssParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
