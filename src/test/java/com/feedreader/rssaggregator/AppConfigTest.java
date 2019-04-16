package com.feedreader.rssaggregator;

import com.feedreader.rssaggregator.tasks.FeedAggregator;
import com.feedreader.rssaggregator.tasks.FeedScanner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppConfigTest {

    @Test
    public void getFeedMessageQueue() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        assertNotNull(context.getBean("feedMessageQueue", BlockingQueue.class));
    }

    @Test
    public void getFeedScanner() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        assertNotNull(context.getBean("feedScanner", FeedScanner.class));
    }

    @Test
    public void getFeedAggregator() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        assertNotNull(context.getBean("feedAggregator", FeedAggregator.class));
    }

    @Test
    public void testBlockingQueueInScannerAndAggregator(){
//        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//        FeedScanner scanner = context.getBean("feedScanner")
        FeedScanner scanner = (FeedScanner)ApplicationContextProvider.getApplicationContext()
                .getBean("feedScanner");

        FeedAggregator aggregator = (FeedAggregator)ApplicationContextProvider.getApplicationContext().getBean("feedAggregator");

        assertEquals(scanner.getQueue(), aggregator.getItemsToProcess());
    }
}