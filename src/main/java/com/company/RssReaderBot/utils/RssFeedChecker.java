package com.company.RssReaderBot.utils;

import com.company.RssReaderBot.config.BotConfig;
import com.company.RssReaderBot.db.entities.RssFeed;
import com.company.RssReaderBot.db.repositories.UserSettingsRepository;
import com.company.RssReaderBot.models.ItemModel;
import com.company.RssReaderBot.utils.parser.RssParser;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class RssFeedChecker implements Runnable {

    @Getter @Setter
    private int interval;

    @Getter @Setter
    private boolean posting = true;

    private final String rssUrl;

    @Getter
    List<ItemModel> newItemsList;

    @Getter
    private Thread thread;

    private ItemModel previousItem;

    private final RssParser parser;

    private final Message message;

    private final UserSettingsRepository userSettingsRepository;

    private final BotConfig botConfig;

    public RssFeedChecker(BotConfig botConfig, Message message,
                          UserSettingsRepository userSettingsRepository, RssFeed feed) {
        this.botConfig = botConfig;
        this.message = message;
        this.userSettingsRepository = userSettingsRepository;
        this.rssUrl = feed.getUrl();
        this.interval = feed.getInterval();
        parser = new RssParser();
    }

    public void startChecking() {
        thread = new Thread(this);
        setPosting(true);
        thread.start();
    }

    public void stopChecking() {
        setPosting(false);
        thread.interrupt();
    }

    public void toggleChecking(boolean condition) {
        if (condition) {
            startChecking();
        } else {
            stopChecking();
        }
    }

    @Override
    public void run() {
        while (posting) {
            try {
                parser.parseRss(rssUrl);
                List<ItemModel> currentItemList = parser.getAllElementsList();
                processNewItems(currentItemList);
                previousItem = currentItemList.get(0);

                Thread.sleep((long) interval * 60 * 1000); // in minutes
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void processNewItems(List<ItemModel> itemsList) {
        if (itemsList == null || itemsList.isEmpty()) {
            return;
        }
        if (previousItem != null) {
            for (ItemModel item : itemsList) {
                String previousItemTitle = previousItem.getTitle();
                if (item.getTitle().equals(previousItemTitle)) {
                    return;
                } else {
                    long chatId = message.chat().id();
                    botConfig.getTelegramBot().execute(new SendMessage(chatId, ElementDisplayFormatter
                            .displayElement(chatId, userSettingsRepository, item)).parseMode(ParseMode.HTML));
                }
            }
        }
    }

    @Override
    public String toString() {
        return "RssFeedChecker{" +
                "thread=" + thread +
                ", posting=" + posting +
                ", interval=" + interval +
                ", rssUrl='" + rssUrl + '\'' +
                '}';
    }
}
