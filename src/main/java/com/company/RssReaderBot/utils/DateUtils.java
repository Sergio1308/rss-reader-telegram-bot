package com.company.RssReaderBot.utils;

import lombok.Getter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * The DateUtils class provides utility methods for working with dates and date formatting.
 * It includes methods for parsing and formatting dates, comparing dates, and converting
 * between different date representations.
 */
public class DateUtils {

    private static final String DATE_FORMAT_PATTERN = "EEE, dd MMM yyyy HH:mm:ss zzz";
    private static final String DATE_TIME_FORMATTER_PATTERN = "dd-MM-yyyy";

    @Getter
    private static final String VALUE_IF_DATE_IS_NULL = "no data";
    private static final String TIME_ZONE = "GMT";

    private static final ZoneOffset ZONE_OFFSET = ZoneOffset.UTC;

    private static final DateFormat DATE_FORMAT;

    private static final DateTimeFormatter DATE_TIME_FORMATTER;

    static {
        DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.ENGLISH);
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_PATTERN);
    }

    private DateUtils() {

    }

    /**
     * Parses a date string into a {@link Date} object using the specified pattern.
     *
     * @param pubDate   the date string to parse
     * @return          the parsed {@link Date}
     * @throws          InvalidPubDateException if the date string is invalid
     */
    public static Date parseDateFromString(String pubDate) throws InvalidPubDateException {
        try {
            return DATE_FORMAT.parse(pubDate);
        } catch (ParseException e) {
            throw new InvalidPubDateException("Invalid publication date of the current element: '" + pubDate + "'", e);
        }
    }

    /**
     * Parses a date string into a {@link LocalDate} object using the specified pattern.
     *
     * @param userDate  the date string to parse
     * @return          the parsed {@link LocalDate}
     * @throws          InvalidUserEnteredDateException if the date string is invalid
     */
    public static LocalDate parseLocalDateFromString(String userDate) throws InvalidUserEnteredDateException {
        try {
            return LocalDate.parse(userDate, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidUserEnteredDateException("Invalid date entered by user: '" + userDate + "'", e);
        }
    }

    /**
     * Compares a {@link LocalDate} object with a {@link Date} object, comparing only the day, month, and year.
     *
     * @param userDate  {@link LocalDate} to compare
     * @param itemDate  {@link Date} to compare
     * @return          {@code true} if the dates match, {@code false} otherwise
     */
    public static boolean compareDates(LocalDate userDate, Date itemDate) {
        if (userDate == null || itemDate == null) {
            return false;
        }
        LocalDate itemLocalDate = itemDate.toInstant().atZone(ZONE_OFFSET).toLocalDate();
        return userDate.isEqual(itemLocalDate);
    }

    /**
     * Formats a {@link Date} object to a string using the specified date format pattern.
     *
     * @param date      {@link Date} object to format
     * @return          formatted date string, or a default value if the date is null
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return VALUE_IF_DATE_IS_NULL;
        }
        return DATE_FORMAT.format(date);
    }
}
