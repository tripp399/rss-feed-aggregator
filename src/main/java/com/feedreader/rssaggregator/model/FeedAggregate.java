package com.feedreader.rssaggregator.model;

import com.feedreader.rssaggregator.util.Container;

import java.util.Set;
import java.util.TreeSet;

public class FeedAggregate<T extends Comparable<? super T>> implements Container<T>{

    private final Set<T> aggregatedList;

//    public FeedAggregate() {
//        aggregatedList = new ConcurrentSkipListSet<>();
//    }

    public FeedAggregate() {
        aggregatedList = new TreeSet<>();
    }

    public Set<T> getAggregatedList() {
        return aggregatedList;
    }

    @Override
    public synchronized void add(T a) {
        aggregatedList.add(a);
    }
}
