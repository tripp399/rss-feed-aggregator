package com.feedreader.rssaggregator.tasks;

import com.feedreader.rssaggregator.model.FeedMessage;

import java.util.*;
import java.util.concurrent.*;

public class BlockingQueueFeedAggregator implements Runnable{
    private static int counter = 0;

    private final BlockingQueue<FeedMessage> itemsToProcess;
    private final ExecutorService executorService = Executors.newCachedThreadPool(); // show variation using cached thread pool
    private final ConcurrentSkipListSet<FeedMessage> byPubDate = new ConcurrentSkipListSet<>();
    private volatile boolean done = false;

    public BlockingQueueFeedAggregator(BlockingQueue<FeedMessage> itemsToProcess){
        this.itemsToProcess = itemsToProcess;
    }

    public void processQueue(){
        while(true){
            try {
                FeedMessage message = this.itemsToProcess.take();
                if(message.isSentinel()){
                    done = true;
                }else{
                    done = false;
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
            boolean status = byPubDate.add(this.message);
//            System.out.println("skip list status:"+status);
        }
    }

    public boolean isDone() {
        return done;
    }

    public static int getCounter() {
        return counter;
    }
}
