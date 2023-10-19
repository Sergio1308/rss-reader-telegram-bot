package com.company.RssReaderBot;

import com.company.RssReaderBot.utils.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DateUtilsTest {

    private GregorianCalendar expectedCalendar;

    private final String expectedFormattedDate = "Tue, 02 May 2023 21:00:00 GMT";

    @BeforeEach
    void setUp() {
        expectedCalendar = new GregorianCalendar();
        expectedCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        expectedCalendar.set(2023, Calendar.MAY, 2, 21, 0, 0);
    }

    @Test
    void testParseDate() {
        String firstDate = "Thu, 27 Apr 2023 13:00:00 GMT";
        String secondDate = "Thu, 27 Apr 2023 13:00:00 -0000";
        String thirdDate = "Thu, 27 Apr 2023 09:00:00 -0400";

        String firstParsedDateString = DateUtils.parseDate(firstDate).toString();
        String secondParsedDateString = DateUtils.parseDate(secondDate).toString();
        String thirdStringDateString = DateUtils.parseDate(thirdDate).toString();

        assertEquals(
                expectedCalendar.getTime().toString(),
                DateUtils.parseDate(expectedFormattedDate).orElseThrow().toString()
        );
        assertAll(
                () -> assertEquals(firstParsedDateString, secondParsedDateString),
                () -> assertEquals(secondParsedDateString, thirdStringDateString),
                () -> assertEquals(firstParsedDateString, thirdStringDateString)
        );
    }

    @Test
    void testParseDateWithInvalidFormat() {
        String pubDate = "Tue, 02 May";
        assertThrows(IllegalArgumentException.class, () -> DateUtils.parseDate(pubDate));
    }

    @Test
    void testFormatDate() {
        assertEquals(expectedFormattedDate, DateUtils.formatDate(expectedCalendar.getTime()));
    }

    @Test
    void testFormatDateWithNull() {
        assertEquals(DateUtils.getVALUE_IF_DATE_IS_NULL(), DateUtils.formatDate(null));
    }
}
