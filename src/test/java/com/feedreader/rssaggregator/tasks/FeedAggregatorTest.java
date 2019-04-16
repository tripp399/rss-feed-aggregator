package com.feedreader.rssaggregator.tasks;

import com.feedreader.rssaggregator.model.FeedMessage;
import org.junit.Test;

import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.*;

import static org.junit.Assert.assertTrue;

public class FeedAggregatorTest {

    @Test
    public void processQueue() throws InterruptedException {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);
        BlockingQueue<FeedMessage> queue = new LinkedBlockingQueue<>();
        FeedScanner scanner = new FeedScanner(queue);
        String url = "http://feeds.feedburner.com/TellDontAsk";
        scanner.addSource(url);
        exec.scheduleAtFixedRate(scanner, 1, 30, TimeUnit.SECONDS);
        FeedAggregator aggregator = new FeedAggregator(queue);
        exec.scheduleWithFixedDelay(aggregator, 1, 5, TimeUnit.SECONDS);
        Thread.sleep(3000);

        List<FeedMessage> messages = aggregator.getByPubDate();
        for(FeedMessage message: messages){
            System.out.println(message.getPubDate());
        }
        assertTrue(messages.size() > 0);
    }

    @Test
    public void multipleCallsWillResultInGreaterNumberOfFeeds() throws InterruptedException {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);
        BlockingQueue<FeedMessage> queue = new LinkedBlockingQueue<>();
        FeedScanner scanner = new FeedScanner(queue);
        String url = "http://feeds.feedburner.com/TellDontAsk";
        String url2 = "https://www.npr.org/rss/podcast.php?id=510289";
        scanner.addSource(url);
        scanner.addSource(url2);
        exec.scheduleAtFixedRate(scanner, 1, 1, TimeUnit.SECONDS);
        FeedAggregator aggregator = new FeedAggregator(queue);
        exec.scheduleWithFixedDelay(aggregator, 1, 1, TimeUnit.SECONDS);
        Thread.sleep(500);
        List<FeedMessage> messages = aggregator.getByPubDate();
        int prevSize = messages.size();
        System.out.println("-----FIRST CALL----");;
        System.out.println(messages.size());
        Thread.sleep(2000);
        System.out.println("---SECOND CALL---");
        List<FeedMessage> newerMessages = aggregator.getByPubDate();
        int thisSize = newerMessages.size();
        System.out.println(newerMessages.size());
        assertTrue(thisSize > prevSize);
    }

    @Test
    public void getByPubDate() throws InterruptedException {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);
        BlockingQueue<FeedMessage> queue = new LinkedBlockingQueue<>();
        FeedScanner scanner = new FeedScanner(queue);
        String url = "http://feeds.feedburner.com/TellDontAsk";
        scanner.addSource(url);
        exec.scheduleAtFixedRate(scanner, 1, 20, TimeUnit.SECONDS);
        FeedAggregator aggregator = new FeedAggregator(queue);
        exec.scheduleWithFixedDelay(aggregator, 1, 5, TimeUnit.SECONDS);
        Thread.sleep(3000);

        List<FeedMessage> messages = aggregator.getByPubDate();
        assertTrue(messages.size() > 0);
        FeedMessage prev = null;
        for(FeedMessage message: messages){
            if(prev != null){
                // New message is more recent than prev message
                assertTrue(message.getPubDate().compareTo(prev.getPubDate()) >= 0);
            }
            prev = message;
        }
    }
}