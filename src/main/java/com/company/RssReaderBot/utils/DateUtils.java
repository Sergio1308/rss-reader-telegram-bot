package com.company.RssReaderBot.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;

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
@Component
public class DateUtils {

    @Getter
    private final String DATE_FORMAT_PATTERN = "EEE, dd MMM yyyy HH:mm:ss zzz";
    @Getter
    private final String TIME_ZONE = "GMT";

    @Getter
    DateFormat format;

    public DateUtils() {
        format = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
    }

    public Optional<Date> parseDate(String pubDate) {
        try {
            return Optional.of(format.parse(pubDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public String formatDate(Date date) {
        if (date == null) {
            return "no data";
        }
        return format.format(date);
    }
}
