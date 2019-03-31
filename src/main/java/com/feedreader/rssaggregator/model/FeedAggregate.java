package com.feedreader.rssaggregator.model;

import java.util.ArrayList;
import java.util.List;

public class FeedAggregate {

    private final List<FeedMessage> aggregatedList;

    public FeedAggregate() {
        aggregatedList = new ArrayList<>();
    }

    public List<FeedMessage> getAggregatedList() {
        return aggregatedList;
    }

    public synchronized void addFeedMessage(FeedMessage message) {
        aggregatedList.add(message);
    }

    public FeedMessage getLastMessage() {
        return aggregatedList.get(aggregatedList.size() - 1);
    }
}
