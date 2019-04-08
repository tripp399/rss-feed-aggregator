package com.feedreader.rssaggregator.tasks;

import com.feedreader.rssaggregator.model.FeedMessage;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class RSSFeedAggregator implements  Runnable{

    private BlockingQueue<FeedMessage> feedMessagesQueue;
    private BlockingQueue<FeedMessage> sortedFeedMessageQueue;
    private TreeSet<FeedMessage> sortedFeedMessagesSet;

    public RSSFeedAggregator(BlockingQueue<FeedMessage> feedMessagesQueue, BlockingQueue<FeedMessage> sortedFeedMessageQueue){
        this.feedMessagesQueue = feedMessagesQueue;
        this.sortedFeedMessageQueue = sortedFeedMessageQueue;
        Comparator<FeedMessage> pubDateComparator = new Comparator<FeedMessage>() {
            @Override
            public int compare(FeedMessage o1, FeedMessage o2) {
                return o1.getPubDate().compareTo(o2.getPubDate());
            }
        };
        this.sortedFeedMessagesSet = new TreeSet<>(pubDateComparator);
    }

    @Override
    public void run() {
        while(true){
            FeedMessage message = null;
            try {
                message = this.feedMessagesQueue.poll(10000, TimeUnit.MILLISECONDS);
                if(message != null){
//                    System.out.println("Received a new message");
                    this.sortedFeedMessagesSet.add(message);
                }else{
                    System.out.println("Sorted feed message set size:"+this.sortedFeedMessagesSet.size());
                    System.out.println("No message received for 15secs! Sending records on output pipe!");
                    while(!this.sortedFeedMessagesSet.isEmpty()){
                        FeedMessage minPubDateMessage = this.sortedFeedMessagesSet.pollFirst();
                        if(minPubDateMessage != null){
                            this.sortedFeedMessageQueue.offer(minPubDateMessage);
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
