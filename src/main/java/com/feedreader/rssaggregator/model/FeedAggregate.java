package com.feedreader.rssaggregator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FeedAggregate {

    private final List<FeedMessage> aggregatedList;

    public FeedAggregate() {
        aggregatedList = new CopyOnWriteArrayList<>();
    }

    public List<FeedMessage> getAggregatedList() {
        return aggregatedList;
    }

    public void addFeedMessage(FeedMessage message) {
        aggregatedList.add(message);
    }

    public FeedMessage getLastMessage() {
        return aggregatedList.get(aggregatedList.size() - 1);
    }
}
