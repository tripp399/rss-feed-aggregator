package com.feedreader.rssaggregator.model;

import com.feedreader.rssaggregator.util.Container;

import java.util.Set;
import java.util.TreeSet;

/**
 * FeedAggregate class to aggregate a set of results.
 * Used in thread pools and direct mapping simulation
 * @param <T>
 */
public class FeedAggregate<T extends Comparable<? super T>> implements Container<T>{
    private final Set<T> aggregatedList;

    /**
     * Constructor
     */
    public FeedAggregate() {
        aggregatedList = new TreeSet<>();
    }

    /**
     * Get the results using this getter method
     * @return Set of results
     */
    public Set<T> getAggregatedList() {
        return aggregatedList;
    }

    @Override
    public synchronized void add(T a) {
        aggregatedList.add(a);
    }

    @Override
    public synchronized void addAll(Set<T> a) {
        this.aggregatedList.addAll(a);
    }
}
