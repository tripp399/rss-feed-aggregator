package com.feedreader.rssaggregator.tasks;

import com.feedreader.rssaggregator.model.FeedMessage;
import com.feedreader.rssaggregator.model.MyBlockingQueue;
import com.feedreader.rssaggregator.util.SyndFeedParser;

import java.net.MalformedURLException;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class FeedScanner implements Runnable{
    private Set<String> urls = new ConcurrentSkipListSet<>();
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private BlockingQueue<FeedMessage> queue;
    private AtomicInteger parsed = new AtomicInteger(0);

    public FeedScanner(BlockingQueue<FeedMessage> queue){
        this.queue = queue;
    }

    public void addSource(String url) {
        // TODO: Check valid url either here or somwhere else
        urls.add(url);
    }

    public boolean removeSource(String url) {
        return urls.remove(url);
    }

    public void startScanner(){
        int i = 0;
        for(String url: this.urls){
            executorService.submit(new ScanSource(url, this.queue, parsed));
            i++;
        }

        // Busy waiting
        while(this.parsed.get() != this.urls.size()){}

        FeedMessage sentinel = new FeedMessage();
        sentinel.setSentinel(true);
        try {
//            System.out.println("----SENTINEL SENT----");
            this.queue.put(sentinel);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        startScanner();
    }

    class ScanSource implements Runnable{
        private final String url;
        private final BlockingQueue<FeedMessage> queue;
        private final AtomicInteger parsed;

        public ScanSource(String url, BlockingQueue<FeedMessage> queue, AtomicInteger parsed){
            this.url = url;
            this.queue = queue;
            this.parsed = parsed;
        }

        @Override
        public void run() {
            try {
                MyBlockingQueue<FeedMessage> myBlockingQueue = new MyBlockingQueue<>(this.queue);
                SyndFeedParser parser = new SyndFeedParser(url, myBlockingQueue);
                parser.call();
            } catch (MalformedURLException e) {
                System.out.println("[FeedScanner] URL "+this.url+"is malformed!");
                e.printStackTrace();
            }
            this.parsed.incrementAndGet();
        }
    }

    public BlockingQueue<FeedMessage> getQueue() {
        return queue;
    }
}
