package com.feedreader.rssaggregator.tasks;

import com.feedreader.rssaggregator.model.FeedMessage;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RSSDriver implements Runnable{
    private BlockingQueue<FeedMessage> feedMessagesQueue;
    private BlockingQueue<FeedMessage> sortedFeedMessageQueue;
    private List<String> feedURLs;

    public RSSDriver(BlockingQueue<FeedMessage> sortedFeedMessageQueue, List<String> feedURLs) {
        this.feedMessagesQueue = new LinkedBlockingQueue<>();
        this.sortedFeedMessageQueue = sortedFeedMessageQueue;
        this.feedURLs = feedURLs;
    }

    @Override
    public void run() {
        for(String url: feedURLs){
            RSSFeedParser parser = new RSSFeedParser(url, feedMessagesQueue);
            new Thread(parser).start();
        }

        RSSFeedAggregator aggregator = new RSSFeedAggregator(feedMessagesQueue, sortedFeedMessageQueue);
        new Thread(aggregator).start();

        while(true){
            try {
                FeedMessage message = this.sortedFeedMessageQueue.take();
                System.out.println(message.getPubDate().toString() + ":" + message.getSource().toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
