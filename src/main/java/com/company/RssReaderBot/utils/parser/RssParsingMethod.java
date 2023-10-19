package com.company.RssReaderBot.utils.parser;

/**
 * Functional interface representing a method that parses an RSS feed identified by a feed ID.
 * The method takes an integer feed ID as a parameter and may throw an RssParsingException.
 * Implementing classes or lambda expressions can use this interface to represent a specific parsing method
 * that operates on an RSS feed identified by the provided feed ID.
 */
@FunctionalInterface
public interface RssParsingMethod {

    /**
     * Parses an RSS feed identified by the given feed ID.
     *
     * @param feedId                The integer ID of the RSS feed to be parsed.
     * @throws RssParsingException  If an error occurs during the RSS feed parsing process.
     */
    void parse(int feedId) throws RssParsingException;
}
