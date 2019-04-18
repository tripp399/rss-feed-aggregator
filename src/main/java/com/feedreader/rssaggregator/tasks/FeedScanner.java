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

/**
 * FeedScanner which spawns a set of threads for each feed uri
 * This acts a producer in our system. It also supports addition for new urls
 */
public class FeedScanner implements Runnable{
    private Set<String> urls = new ConcurrentSkipListSet<>();
    int num = Runtime.getRuntime().availableProcessors();
//    private ExecutorService executorService = Executors.newFixedThreadPool(num + 3);
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private BlockingQueue<FeedMessage> queue;
    private final boolean batch;
    private AtomicInteger currentParsedCount = new AtomicInteger(0);

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
        for(String url: this.urls){
            executorService.submit(new ScanSource(url, this.queue, this.batch, this.currentParsedCount));
        }

//        for(Future<?> f: futures){
//            try {
//                f.get();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//
// }

        // Busy waiting
        while(currentParsedCount.get() != this.urls.size()){}

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
        private AtomicInteger currentParsedCount;

        /**
         * Constructor
         * @param url The url to process
         * @param queue The queue to put the processed item
         * @param batch Whether to use batch processing
         * @param currentParsedCount Atomic integer to indicate if all urls are done parsing
         */
        public ScanSource(String url, BlockingQueue<FeedMessage> queue, boolean batch, AtomicInteger currentParsedCount){
            this.url = url;
            this.queue = queue;
            this.batch = batch;
            this.currentParsedCount = currentParsedCount;
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
            currentParsedCount.incrementAndGet();
        }
    }

    @Override
    public void run() {
        startScanner();
    }
}
