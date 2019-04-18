package com.feedreader.rssaggregator.tasks;

import com.feedreader.rssaggregator.model.FeedMessage;
import com.feedreader.rssaggregator.model.MyBlockingQueue;
import com.feedreader.rssaggregator.util.SyndFeedParser;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * FeedScanner which spawns a set of threads for each feed uri
 * This acts a producer in our system. It also supports addition for new urls
 */
public class FeedScanner implements Runnable{
    private Set<String> urls = new ConcurrentSkipListSet<>();
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private BlockingQueue<FeedMessage> queue;
    private final boolean batch;

    /**
     * Constructor
     * @param queue The blocking queue on which to put new items
     * @param batch Whether to process the items in each feed in a batch or item by item
     */
    public FeedScanner(BlockingQueue<FeedMessage> queue, boolean batch){
        this.queue = queue;
        this.batch = batch;
    }

    /**
     * Add a new url in the set of urls. This url will be parsed in the next iteration if the scheduler has not scheduled the FeedScanner. If new url is added when execution is in process it may parse it.
     * @param url The url to be parsed and elements extracted
     */
    public void addSource(String url) {
        // TODO: Check valid url either here or somewhere else
        urls.add(url);
    }

    /**
     * Remove the url from the set of urls to process
     * @param url The url to be removed from processing
     * @return boolean indication whether remove is successful or unsuccessful
     */
    public boolean removeSource(String url) {
        return urls.remove(url);
    }

    /**
     * A Mathod which is responsible for spawning a set of threads to be parsed
     */
    public void startScanner(){
        List<Future<?>> futures = new ArrayList<>();
        for(String url: this.urls){
            Future<?> f = executorService.submit(new ScanSource(url, this.queue, this.batch));
            futures.add(f);
        }

        for(Future<?> f: futures){
            try {
                f.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        // All feeds have been parsed now, send the sentinel message on queue to indicate end of this batch of processing
        // This is required as the consumer might be running in an infinite loop
        FeedMessage sentinel = new FeedMessage();
        sentinel.setSentinel(true);
        try {
            this.queue.put(sentinel);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter method to get the queue which the producer is putting new items on
     * @return The reference to the producer's queue
     */
    public BlockingQueue<FeedMessage> getQueue() {
        return queue;
    }

    /**
     * An inner class responsible for parsing a single url
     */
    class ScanSource implements Runnable{
        private final String url;
        private final BlockingQueue<FeedMessage> queue;
        private final boolean batch;

        /**
         * Constructor
         * @param url The url to process
         * @param queue The queue to put the processed item
         * @param batch Whether to use batch processing
         */
        public ScanSource(String url, BlockingQueue<FeedMessage> queue, boolean batch){
            this.url = url;
            this.queue = queue;
            this.batch = batch;
        }

        @Override
        public void run() {
            try {
                // Get a wrapper
                MyBlockingQueue<FeedMessage> myBlockingQueue = new MyBlockingQueue<>(this.queue);
                // Invoke the parser
                SyndFeedParser parser = new SyndFeedParser(url, myBlockingQueue, this.batch);
                parser.call();
            } catch (MalformedURLException e) {
                System.out.println("[FeedScanner] URL "+this.url+"is malformed!");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        startScanner();
    }
}
