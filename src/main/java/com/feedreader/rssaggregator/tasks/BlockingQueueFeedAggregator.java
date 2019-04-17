package com.feedreader.rssaggregator.tasks;

import com.feedreader.rssaggregator.model.FeedMessage;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlockingQueueFeedAggregator implements Runnable{
    private final BlockingQueue<FeedMessage> itemsToProcess;
    private final ExecutorService executorService = Executors.newCachedThreadPool(); // show variation using cached thread pool
    private final ConcurrentSkipListSet<FeedMessage> byPubDate = new ConcurrentSkipListSet<>(new Comparator<FeedMessage>() {
        @Override
        public int compare(FeedMessage o1, FeedMessage o2) {
            return o1.compareTo(o2);
        }
    });
    private volatile boolean done = false;

    private static int counter = 0;

    public BlockingQueueFeedAggregator(BlockingQueue<FeedMessage> itemsToProcess){
        this.itemsToProcess = itemsToProcess;
    }

    public void processQueue(){
        while(true){
            try {
                FeedMessage message = this.itemsToProcess.take();
                if(message.isSentinel()){
                    done = true;
                }
                counter++;
                executorService.submit(new WorkItem(message, byPubDate));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Set<FeedMessage> getByPubDate(){
        return this.byPubDate;
    }

    @Override
    public void run() {
        processQueue();
    }

    public BlockingQueue<FeedMessage> getItemsToProcess() {
        return itemsToProcess;
    }

    class WorkItem implements Runnable{
        private FeedMessage message;
        private ConcurrentSkipListSet<FeedMessage> byPubDate;

        public WorkItem(FeedMessage message, ConcurrentSkipListSet<FeedMessage> byPubDate){
            this.message = message;
            this.byPubDate = byPubDate;
        }

        @Override
        public void run() {
            byPubDate.add(this.message);
        }
    }

    public boolean isDone() {
        return done;
    }
}
