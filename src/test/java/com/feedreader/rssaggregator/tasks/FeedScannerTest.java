package com.feedreader.rssaggregator.tasks;

import com.feedreader.rssaggregator.model.FeedMessage;
import org.junit.Test;

import java.util.concurrent.*;

import static org.junit.Assert.*;

public class FeedScannerTest {

    @Test
    public void initialize() throws InterruptedException {
        BlockingQueue<FeedMessage> queue = new LinkedBlockingQueue<>();
        FeedScanner scanner = new FeedScanner(queue, false);
        String url = "http://feeds.feedburner.com/TellDontAsk";
        scanner.addSource(url);
        assertTrue(scanner.removeSource(url));
    }

    @Test
    public void testRepeatedExecution() throws InterruptedException {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        BlockingQueue<FeedMessage> queue = new LinkedBlockingQueue<>();
        FeedScanner scanner = new FeedScanner(queue, false);
        scanner.addSource("http://www.sqlservercentral.com/Forums/RssFeed148-0-0-1.aspx");
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
        FeedScanner scanner = new FeedScanner(queue, false);
        assertFalse(scanner.removeSource(url));
    }

    @Test
    public void startScannerWithInvalidURL() throws InterruptedException {
        String url = "invalidurl";
        BlockingQueue<FeedMessage> queue = new LinkedBlockingQueue<>();
        FeedScanner scanner = new FeedScanner(queue, false);
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