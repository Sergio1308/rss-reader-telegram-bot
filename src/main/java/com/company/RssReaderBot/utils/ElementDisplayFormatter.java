package com.company.RssReaderBot.utils;

import com.company.RssReaderBot.db.entities.UserSettings;
import com.company.RssReaderBot.db.repositories.UserSettingsRepository;
import com.company.RssReaderBot.models.ItemModel;

import java.util.Optional;

/**
 * ElementDisplayFormatter class provides a static method for formatting the answer
 * text based on the current item.
 */
public class ElementDisplayFormatter {

    private ElementDisplayFormatter() {

    }

    /**
     * Formats the answer text based on the selected item and user settings.
     *
     * @param chatId                    chat id
     * @param currentItem               Current item model.
     * @param userSettingsRepository    The repository for accessing user settings.
     * @return                          The formatted answer text.
     */
    public static String displayElement(long chatId, UserSettingsRepository userSettingsRepository,
                                        ItemModel currentItem) {
        StringBuilder stringBuilder = new StringBuilder();
        UserSettings userSettings = userSettingsRepository.findByUserUserid(chatId).orElseThrow();

        Optional.of(userSettings.isDisplayTitle())
                .filter(Boolean::booleanValue)
                .ifPresent(displayTitle -> {
                    String title = currentItem.getTitle();
                    stringBuilder.append("<b>").append(title).append("</b>\n\n");
                });
        Optional.of(userSettings.isDisplayDescription())
                .filter(Boolean::booleanValue)
                .ifPresent(displayDescription -> {
                    String description = currentItem.getDescription();
                    stringBuilder.append(HtmlStringFormatter.sanitizeString(description)).append("\n\n");
                });
        Optional.of(userSettings.isDisplayDate())
                .filter(Boolean::booleanValue)
                .ifPresent(displayDate -> {
                    String date = DateUtils.formatDate(currentItem.getPubDate());
                    stringBuilder.append(date).append("\n\n");
                });
        Optional.of(userSettings.isDisplayMedia())
                .filter(Boolean::booleanValue)
                .ifPresent(displayMedia -> stringBuilder
                        .append(createHyperlink(currentItem.getMediaUrl(), "Media"))
                        .append("\n\n"));
        Optional.of(userSettings.isDisplayLink())
                .filter(Boolean::booleanValue)
                .ifPresent(displayLink -> stringBuilder
                        .append("Source: ")
                        .append(createHyperlink(currentItem.getSourceLink(), currentItem.getTitle())));
        return stringBuilder.toString();
    }

    /**
     * Creates a hyperlink HTML string with the specified URL and text.
     *
     * @param href  The URL of the hyperlink.
     * @param text  The text to display for the hyperlink.
     * @return      The HTML string representing the hyperlink.
     */
    public static String createHyperlink(String href, String text) {
        return "<a href='" + href + "'>" + text + "</a>";
    }
}
