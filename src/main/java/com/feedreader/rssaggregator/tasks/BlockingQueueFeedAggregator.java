package com.feedreader.rssaggregator.tasks;

import com.feedreader.rssaggregator.model.FeedMessage;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlockingQueueFeedAggregator implements Runnable{
    private final BlockingQueue<FeedMessage> itemsToProcess;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2); // show variation using cached thread pool

    private final TreeSet<FeedMessage> byPubDate = new TreeSet<>(new Comparator<FeedMessage>() {
        @Override
        public int compare(FeedMessage o1, FeedMessage o2) {
            return o1.compareTo(o2);
        }
    });

//    private final List<FeedMessage> feedmessageList = new ArrayList<>();
    private static int counter = 0;

    public BlockingQueueFeedAggregator(BlockingQueue<FeedMessage> itemsToProcess){
        this.itemsToProcess = itemsToProcess;
    }

    public void processQueue(){
        while(true){
            try {
                FeedMessage message = this.itemsToProcess.take();
                byPubDate.add(message);
//                feedmessageList.add(message);
//                System.out.println("Added in set!"+counter);
                counter++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public TreeSet<FeedMessage> getByPubDate(){
//        List<FeedMessage> copy = new ArrayList<>(this.feedmessageList);
//        Collections.sort(copy, new Comparator<FeedMessage>() {
//            @Override
//            public int compare(FeedMessage o1, FeedMessage o2) {
//                return o1.getPubDate().compareTo(o2.getPubDate());
//            }
//        });
        return new TreeSet<>(this.byPubDate);
    }

    @Override
    public void run() {
        processQueue();
    }

    public BlockingQueue<FeedMessage> getItemsToProcess() {
        return itemsToProcess;
    }
}
