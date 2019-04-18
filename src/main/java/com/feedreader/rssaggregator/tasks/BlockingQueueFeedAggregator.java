package com.feedreader.rssaggregator.tasks;

import com.feedreader.rssaggregator.model.FeedMessage;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BlockingQueueFeedAggregator - This class consumes the queue passed in the constructor
 * It acts as a consumer in our system.
 */
public class BlockingQueueFeedAggregator implements Runnable{
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final BlockingQueue<FeedMessage> itemsToProcess;
    private final ConcurrentSkipListSet<FeedMessage> byPubDate = new ConcurrentSkipListSet<>();
    private volatile boolean done = false;
    private static int counter = 0;

    /**
     * Class constructor
     * @param itemsToProcess The blocking queue shared by producer on which producers
     *                       put messages
     */
    public BlockingQueueFeedAggregator(BlockingQueue<FeedMessage> itemsToProcess){
        this.itemsToProcess = itemsToProcess;
    }

    /**
     * This method to extract an element from queue and processes each item in a different thread by passing it to executor service.
     */
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

    /**
     * Getter for obtaining the set of feed messages which are sorted by published date
     * @return Set of feed messages sorted by published date
     */
    public Set<FeedMessage> getByPubDate(){
        return this.byPubDate;
    }

    /**
     * Getter for obtained the blocking which this consumer is processing
     * @return A reference to the blocking queue to process
     */
    public BlockingQueue<FeedMessage> getItemsToProcess() {
        return itemsToProcess;
    }

    /**
     * Getter for obtaining whether a set of feeds has been processed
     * @return boolean indicating whether the sentinel message has been received from producers manager
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Getter indication how many messages are processed
     * @return integer a counter indicating how many messages have been processed
     */
    public static int getCounter() {
        return counter;
    }

    /**
     * An inner class which encapsulates the logic to process each feed item
     */
    class WorkItem implements Runnable{
        private FeedMessage message;
        private ConcurrentSkipListSet<FeedMessage> byPubDate;

        /**
         * Class constructor
         * @param message The message to be processed
         * @param byPubDate The data structure on which the message is put
         */
        public WorkItem(FeedMessage message, ConcurrentSkipListSet<FeedMessage> byPubDate){
            this.message = message;
            this.byPubDate = byPubDate;
        }

        @Override
        public void run() {
            byPubDate.add(this.message);
        }
    }


    @Override
    public void run() {
        processQueue();
    }
}
