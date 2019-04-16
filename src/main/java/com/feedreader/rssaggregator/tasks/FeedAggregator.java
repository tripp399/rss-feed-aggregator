package com.feedreader.rssaggregator.tasks;
import com.feedreader.rssaggregator.model.FeedMessage;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FeedAggregator implements Runnable{
    private final BlockingQueue<FeedMessage> itemsToProcess;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2); // show variation using cached thread pool

    private final ConcurrentSkipListSet<FeedMessage> byPubDate = new ConcurrentSkipListSet<>(new Comparator<FeedMessage>() {
        @Override
        public int compare(FeedMessage o1, FeedMessage o2) {
            return o1.getPubDate().compareTo(o2.getPubDate());
        }
    });

    public FeedAggregator(BlockingQueue<FeedMessage> itemsToProcess){
        this.itemsToProcess = itemsToProcess;
    }

    public void processQueue(){
        while(true){
            try {
                FeedMessage message = this.itemsToProcess.take();
                byPubDate.add(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public TreeSet<FeedMessage> getByPubDate(){
        return new TreeSet<>(this.byPubDate);
    }

    @Override
    public void run() {
        processQueue();
    }
}
