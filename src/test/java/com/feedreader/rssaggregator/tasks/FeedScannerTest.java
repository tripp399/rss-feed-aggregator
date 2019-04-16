package com.feedreader.rssaggregator.tasks;

import com.feedreader.rssaggregator.model.FeedMessage;
import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;

public class FeedScannerTest {

    @Test
    public void initialize() throws InterruptedException {
        BlockingQueue<FeedMessage> queue = new LinkedBlockingQueue<>();
        FeedScanner scanner = new FeedScanner(queue);
        scanner.addSource("http://feeds.feedburner.com/TellDontAsk");
        scanner.startScanner();
        while(true){
            FeedMessage message = queue.take();
            System.out.println(message.toString());
        }
    }
}