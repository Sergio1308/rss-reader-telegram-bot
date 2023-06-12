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
     * @param chatId chat id
     * @param currentItem Current item model.
     * @param userSettingsRepository The repository for accessing user settings.
     * @return The formatted answer text.
     */
    public static String displayElement(long chatId, UserSettingsRepository userSettingsRepository,
                                        ItemModel currentItem) {
        final String URL_HREF_OPEN_TAG = "<a href='";
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
                .ifPresent(displayMedia -> stringBuilder.append(URL_HREF_OPEN_TAG)
                        .append(currentItem.getMediaUrl())
                        .append("'>Media</a>").append("\n\n"));
        Optional.of(userSettings.isDisplayLink())
                .filter(Boolean::booleanValue)
                .ifPresent(displayLink -> stringBuilder.append("Source: " + URL_HREF_OPEN_TAG)
                        .append(currentItem.getSourceLink())
                        .append("'>").append(currentItem.getTitle()).append("</a>"));
        return stringBuilder.toString();
    }
}
