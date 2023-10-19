package com.company.RssReaderBot;

import com.company.RssReaderBot.utils.HtmlStringFormatter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HtmlStringFormatterTest {

    @Test
    void testSanitizeString() {
        String simpleString = "some text without html tags";
        String stringWithSimpleTags = "<p>5 is &lt; 6.</p>";
        String stringWithTags = "<![CDATA[ <p><strong>SOME. Test. String.</strong> with \"HTML\" tags " +
                "Lorem ipsum \"dolor sit\" amet ut <strong>labore et dolore magna aliqua</strong>. Ut.</p> ]]>";

        String expectedSimpleString = "some text without html tags";
        String expectedStringWithSimpleTags = "5 is &lt; 6.";
        String expectedStringWithTags = "&lt;p&gt;&lt;strong&gt;SOME. Test. String.&lt;/strong&gt; " +
                "with \"HTML\" tags Lorem ipsum \"dolor sit\" amet ut &lt;strong&gt;labore " +
                "et dolore magna aliqua&lt;/strong&gt;. Ut.&lt;/p&gt;";

        assertAll(
                () -> assertEquals(expectedSimpleString,
                        HtmlStringFormatter.sanitizeString(simpleString)),
                () -> assertEquals(expectedStringWithSimpleTags,
                        HtmlStringFormatter.sanitizeString(stringWithSimpleTags)),
                () -> assertEquals(expectedStringWithTags,
                        HtmlStringFormatter.sanitizeString(stringWithTags))
        );
    }
}
