package com.feedreader.rssaggregator.tasks;

import com.feedreader.rssaggregator.util.Container;

import java.util.concurrent.BlockingQueue;

public class MyBlockingQueue<T> implements Container<T> {
    BlockingQueue<T> queue;

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
}
