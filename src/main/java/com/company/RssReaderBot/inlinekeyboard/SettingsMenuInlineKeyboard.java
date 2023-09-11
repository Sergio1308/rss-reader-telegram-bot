package com.company.RssReaderBot.inlinekeyboard;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.company.RssReaderBot.db.entities.FavoriteItem;
import com.company.RssReaderBot.db.entities.RssFeed;
import com.company.RssReaderBot.db.entities.UserSettings;
import com.company.RssReaderBot.models.ItemModel;
import com.company.RssReaderBot.models.ItemsList;
import com.company.RssReaderBot.models.ItemsPagination;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SettingsMenuInlineKeyboard implements InlineKeyboardCreator {

    @Getter @Setter
    private UserSettings userSettings;

    private final ItemsList itemsList;

    public final ItemsPagination itemsPagination;

    private final PaginationInlineKeyboard paginationInlineKeyboard;

    private final RssFeedsInlineKeyboard feedsInlineKeyboard;

    @Getter @Setter
    private List<RssFeed> feedList;

    public SettingsMenuInlineKeyboard(ItemsList itemsList, ItemsPagination itemsPagination,
                                      PaginationInlineKeyboard paginationInlineKeyboard,
                                      RssFeedsInlineKeyboard feedsInlineKeyboard) {
        this.itemsList = itemsList;
        this.itemsPagination = itemsPagination;
        this.paginationInlineKeyboard = paginationInlineKeyboard;
        this.feedsInlineKeyboard = feedsInlineKeyboard;
    }

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("My Favorites")
                                .callbackData(CallbackDataConstants.FAVORITES)
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Feed settings")
                        .callbackData(CallbackDataConstants.FEED_SETTINGS)
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("⚙️Display title: " + userSettings.isDisplayTitle())
                                .callbackData(CallbackDataConstants.DisplayOptions.DISPLAY_TITLE.getCallback())
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("⚙️Display description: " + userSettings.isDisplayDescription())
                                .callbackData(CallbackDataConstants.DisplayOptions.DISPLAY_DESCRIPTION.getCallback())
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("⚙️Display date: " + userSettings.isDisplayDate())
                                .callbackData(CallbackDataConstants.DisplayOptions.DISPLAY_DATE.getCallback())
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("⚙️Display media: " + userSettings.isDisplayMedia())
                                .callbackData(CallbackDataConstants.DisplayOptions.DISPLAY_MEDIA.getCallback())
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("⚙️Display link to source: " + userSettings.isDisplayLink())
                                .callbackData(CallbackDataConstants.DisplayOptions.DISPLAY_LINK.getCallback())
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Back")
                                .callbackData(CallbackDataConstants.START_MENU)
                }
        );
    }

    public InlineKeyboardMarkup displayFavorites(List<FavoriteItem> favoriteItems) {
        List<ItemModel> itemsModelList = favoriteItems.stream().map(
                        item -> ItemModel.builder()
                                .feedId(item.getFeed().getId())
                                .title(item.getItemTitle())
                                .description(item.getItemDescription())
                                .pubDate(item.getItemDate())
                                .mediaUrl(item.getItemMediaUrl())
                                .sourceLink(item.getItemSourceLink())
                                .build()
                )
                .collect(Collectors.toList());
        itemsList.setItemsList(itemsModelList);
        itemsPagination.toSplit();
        return paginationInlineKeyboard.createButton(
                paginationInlineKeyboard.execute(itemsPagination.getStartButtonsIndex()),
                "Back",
                CallbackDataConstants.SETTINGS_MENU
        );
    }

    public InlineKeyboardMarkup displayRssFeedSettings() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        feedsInlineKeyboard.setFeedList(feedList);
        InlineKeyboardButton[] feedButtons = feedsInlineKeyboard.createFeedListButton();
        markup.addRow(feedButtons);
        createFeedSettingsButtons(markup);

        return markup;
    }

    public void createFeedSettingsButtons(InlineKeyboardMarkup inlineMarkup) {
        int currentFeedIndex = feedsInlineKeyboard.getCurrentFeedIndex();
        RssFeed selectedFeed = feedList.get(currentFeedIndex);

        inlineMarkup
                .addRow(new InlineKeyboardButton("⚙️Monitoring: " + selectedFeed.isMonitoring())
                        .callbackData(CallbackDataConstants.SETTINGS_FEED_POSTING))
                .addRow(new InlineKeyboardButton("⚙️Monitoring interval: " + selectedFeed.getInterval() + " min")
                        .callbackData(CallbackDataConstants.SETTINGS_FEED_INTERVAL))
                .addRow(new InlineKeyboardButton("Back")
                        .callbackData(CallbackDataConstants.SETTINGS_MENU));
    }

    public InlineKeyboardMarkup createIntervalFeedMenu() {
        InlineKeyboardButton[][] buttons = new InlineKeyboardButton[9][4];
        int interval = 5;
        int maxIntervalInHours = 24 * 60;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 4; j++) {
                String time;
                int displayInterval;

                if (interval < 60) {
                    interval += 5;
                    time = "min";
                    displayInterval = interval;
                } else if (interval < maxIntervalInHours) {
                    interval += 60;
                    time = "h";
                    displayInterval = interval / 60;
                } else {
                    interval += maxIntervalInHours;
                    time = "d";
                    displayInterval = interval / maxIntervalInHours;
                }
                buttons[i][j] = new InlineKeyboardButton("" + displayInterval + " " + time)
                        .callbackData(CallbackDataConstants.INTERVAL + interval);
            }
        }
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        markup.addRow(new InlineKeyboardButton("Back").callbackData(CallbackDataConstants.FEED_SETTINGS));
        return markup;
    }
}
