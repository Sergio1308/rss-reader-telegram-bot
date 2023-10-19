package com.company.RssReaderBot.models;

import com.company.RssReaderBot.utils.RssFeedChecker;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * RssFeedCheckerRegistry class is responsible for managing a collection of RssFeedChecker objects.
 * It provides methods to add, retrieve, and remove RssFeedChecker instances from the registry.
 */
@Component
public class RssFeedCheckerRegistry {

    @Getter
    private final Map<Integer, RssFeedChecker> feedCheckerMap;

    public RssFeedCheckerRegistry() {
        this.feedCheckerMap = new HashMap<>();
    }

    public void addRssFeedChecker(Integer feedId, RssFeedChecker feedChecker) {
        feedCheckerMap.put(feedId, feedChecker);
    }

    public RssFeedChecker getRssFeedChecker(Integer feedId) {
        return feedCheckerMap.get(feedId);
    }

    public void removeRssFeedChecker(Integer feedId) {
        feedCheckerMap.remove(feedId);
    }
}
