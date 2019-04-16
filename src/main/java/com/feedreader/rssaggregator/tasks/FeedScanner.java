package com.feedreader.rssaggregator.tasks;

import com.feedreader.rssaggregator.model.FeedMessage;
import com.feedreader.rssaggregator.util.SyndFeedParser;

import java.net.MalformedURLException;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FeedScanner {
    private Set<String> urls = new ConcurrentSkipListSet<>();
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private BlockingQueue<FeedMessage> queue;

    public FeedScanner(BlockingQueue<FeedMessage> queue){
        this.queue = queue;
    }

    public void addSource(String url) {
        urls.add(url);
    }

    public boolean removeSource(String url) {
        return urls.remove(url);
    }

    public void startScanner(){
        for(String url: this.urls){
            executorService.submit(new ScanSource(url, this.queue));
        }
    }

    class ScanSource implements Runnable{
        private final String url;
        private final BlockingQueue<FeedMessage> queue;

        public ScanSource(String url, BlockingQueue<FeedMessage> queue){
            this.url = url;
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                SyndFeedParser parser = new SyndFeedParser(url, queue);
                parser.call();
            } catch (MalformedURLException e) {
                System.out.println("[FeedScanner] URL "+this.url+"is malformed!");
                e.printStackTrace();
            }
        }
    }
}
