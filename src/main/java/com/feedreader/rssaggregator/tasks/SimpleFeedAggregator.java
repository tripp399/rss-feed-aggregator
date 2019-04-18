package com.feedreader.rssaggregator.tasks;

import com.feedreader.rssaggregator.model.FeedAggregate;
import com.feedreader.rssaggregator.util.SyndFeedParser;
import com.rometools.rome.feed.synd.SyndFeed;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * SimpleFeedAggregator: A class to test whether the blocking queue is better than using thread pools or direct mapping to threads
 */
public class SimpleFeedAggregator{

    /**
     * Parses a set of  feed using thread pools
     * @param feeds The set of feeds to parse
     * @param poolSize The number of threads on which parsing is to be conducted
     * @return FeedAggregate object which contains all the elements from all the feeds aggregated and sorted
     */
    public FeedAggregate aggregateUsingThreadPools(List<String> feeds, int poolSize) {
        FeedAggregate feedAggregate = new FeedAggregate<>();

        ExecutorService exec = Executors.newFixedThreadPool(poolSize);
        List<Future<SyndFeed>> futures = new ArrayList<>();
        for (String feed : feeds) {
            Callable<SyndFeed> callable = null;
            try {
                callable = new SyndFeedParser(feed, feedAggregate, false);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            futures.add(exec.submit(callable));
        }
        exec.shutdown();
        while (!exec.isTerminated()) {}
        return feedAggregate;
    }

    /**
     * Parses a set of feeds using direct mapping
     * @param feeds A set of feeds to parse
     * @return FeedAggregate: object which contains all the elements from all the feeds aggregated and sorted
     */
    public FeedAggregate aggregateUsingDirectMapping(List<String> feeds) {
        FeedAggregate feedAggregate = new FeedAggregate();
        List<Thread> threads = new ArrayList<>();

        feeds.forEach(feed -> {
            SyndFeedParser parser = null;
            try {
                parser = new SyndFeedParser(feed, feedAggregate, false);
                Thread thread = new Thread(parser);
                threads.add(thread);
                thread.start();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return feedAggregate;
    }
}
