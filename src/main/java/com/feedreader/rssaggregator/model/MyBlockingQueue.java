package com.feedreader.rssaggregator.model;

import com.feedreader.rssaggregator.util.Container;

import java.util.Set;
import java.util.concurrent.BlockingQueue;

/**
 * Wrapper class around BlockingQueue. Required for Container Interface.
 * @param <T>
 */
public class MyBlockingQueue<T> implements Container<T> {
    BlockingQueue<T> queue;

    /**
     * Constructor
     * @param queue the queue to wrap
     */
    public MyBlockingQueue(BlockingQueue<T> queue){
        this.queue = queue;
    }

    @Override
    public void add(T a) {
        try {
            this.queue.put(a);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addAll(Set<T> a) {
        this.queue.addAll(a);
    }
}
