package com.feedreader.rssaggregator.tasks;

import com.feedreader.rssaggregator.model.FeedMessage;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class FeedScannerTest {

    @Test
    public void initialize() throws InterruptedException {
        BlockingQueue<FeedMessage> queue = new LinkedBlockingQueue<>();
        FeedScanner scanner = new FeedScanner(queue);
        String url = "http://feeds.feedburner.com/TellDontAsk";
        scanner.addSource(url);
        assertTrue(scanner.removeSource(url));
    }

    @Test
    public void testRepeatedExecution() throws InterruptedException {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        BlockingQueue<FeedMessage> queue = new LinkedBlockingQueue<>();
        FeedScanner scanner = new FeedScanner(queue);
        scanner.addSource("http://feeds.feedburner.com/TellDontAsk");
        exec.scheduleAtFixedRate(scanner, 1, 10, TimeUnit.SECONDS);

        int first = 0;
        while(true){
            FeedMessage message = queue.poll(5, TimeUnit.SECONDS);
            if(message == null) break;
            first++;
            System.out.println(message.toString());
        }

        int second = 0;
        while(true){
            FeedMessage message = queue.poll(5, TimeUnit.SECONDS);
            if(message == null) break;
            second++;
            System.out.println(message.toString());
        }

        assertEquals(first, second);
    }

    @Test
    public void removeSource() {
        String url = "http://feeds.feedburner.com/TellDontAsk";
        BlockingQueue<FeedMessage> queue = new LinkedBlockingQueue<>();
        FeedScanner scanner = new FeedScanner(queue);
        assertFalse(scanner.removeSource(url));
    }

    @Test
    public void startScannerWithInvalidURL() throws InterruptedException {
        String url = "invalidurl";
        BlockingQueue<FeedMessage> queue = new LinkedBlockingQueue<>();
        FeedScanner scanner = new FeedScanner(queue);
        scanner.addSource(url);
        scanner.startScanner();
        int messages = 0;
        while(true){
            FeedMessage message = queue.poll(5, TimeUnit.SECONDS);
            if(message == null) break;
            System.out.println(message.toString());
        }
        assertEquals(0, messages);
    }
}