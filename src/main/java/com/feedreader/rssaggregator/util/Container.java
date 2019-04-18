package com.feedreader.rssaggregator.util;

import java.util.Set;

/**
 * Container to code SyndFeedParser to an interface. To support multiple structures.
 * @param <T>
 */
public interface Container<T> {
    public void add(T a);
    public void addAll(Set<T> a);
}
