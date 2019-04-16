package com.feedreader.rssaggregator.model;

import com.feedreader.rssaggregator.util.Container;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class FeedAggregate<T extends Comparable<? super T>> implements Container<T>{

    private final List<T> aggregatedList;

    public FeedAggregate() {
        aggregatedList = new CopyOnWriteArrayList<>();
    }

    public List<T> getAggregatedList() {
        return aggregatedList;
    }

    @Override
    public void add(T a) {
        aggregatedList.add(a);
    }

    public void sort(){
        Collections.sort(this.aggregatedList, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return o1.compareTo(o2);
            }
        });
    }
}
