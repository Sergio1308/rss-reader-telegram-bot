package com.company.RssReaderBot.utils;

import lombok.Getter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

/**
 * Utility class to work with date. It contains a method that convert a date string value to Date object
 * and a method that converts java Date to TimeZone format.
 */
public class DateUtils {

    @Getter
    private static final String DATE_FORMAT_PATTERN = "EEE, dd MMM yyyy HH:mm:ss zzz";
    @Getter
    private static final String TIME_ZONE = "GMT";
    @Getter
    private static final String VALUE_IF_DATE_IS_NULL = "no data";

    @Getter
    private static final DateFormat format;

    static {
        format = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
    }

    private DateUtils() {

    }

    public static Optional<Date> parseDate(String pubDate) {
        try {
            return Optional.of(format.parse(pubDate));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Cannot parse current date: '" + pubDate + "'");
        }
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return VALUE_IF_DATE_IS_NULL;
        }
        return format.format(date);
    }
}
