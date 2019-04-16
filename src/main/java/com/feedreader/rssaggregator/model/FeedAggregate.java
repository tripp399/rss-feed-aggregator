package com.feedreader.rssaggregator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FeedAggregate<T> {

    private final List<T> aggregatedList;

    public FeedAggregate() {
        aggregatedList = new CopyOnWriteArrayList<>();
//        aggregatedList = new ArrayList<>();
    }

    public List<T> getAggregatedList() {
        return aggregatedList;
    }

//    public synchronized void addFeedMessage(T message) {
//        aggregatedList.add(message);
//    }
    public void addFeedMessage(T message) {
        aggregatedList.add(message);
    }

    public T getLastMessage() {
        return aggregatedList.get(aggregatedList.size() - 1);
    }
}
