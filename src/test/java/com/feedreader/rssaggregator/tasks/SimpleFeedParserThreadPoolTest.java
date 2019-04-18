package com.feedreader.rssaggregator.tasks;

import com.feedreader.rssaggregator.model.FeedAggregate;
import com.feedreader.rssaggregator.model.FeedMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleFeedParserThreadPoolTest {

    private List<String> feeds;
    private Random random;
    private SimpleFeedAggregator simpleFeedAggregator;

    @Before
    public void init() {
        simpleFeedAggregator = new SimpleFeedAggregator();
        random = new Random();
        feeds = new ArrayList<>();
        try {
            File file = new ClassPathResource("feeds.txt").getFile();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                feeds.add(scanner.nextLine());
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void feedAggregateIsNotEmpty() throws MalformedURLException {

        int length = 20;
        int index = random.nextInt(300);
        System.out.println("starting");
        long start = System.currentTimeMillis();
//    FeedAggregate feedAggregate = aggregate(feeds.subList(index, index + length), 20);
        FeedAggregate feedAggregate
                = simpleFeedAggregator.aggregateUsingThreadPools(feeds,15);
        long finish = System.currentTimeMillis();
        System.out.println(finish - start);
        System.out.println(feedAggregate.getAggregatedList().size());

        Assert.notEmpty(feedAggregate.getAggregatedList(), "List is Empty");
    }

    @Test
    public void feedMessageLinkIsNotEmpty() throws MalformedURLException {

        int length = 20;
        int index = random.nextInt(300);
        System.out.println("starting");
        long start = System.currentTimeMillis();
        FeedAggregate feedAggregate
                = simpleFeedAggregator.aggregateUsingThreadPools(feeds.subList(index, index + length),15);
        long finish = System.currentTimeMillis();
        System.out.println(finish - start);
        System.out.println(feedAggregate.getAggregatedList().size());

        for (FeedMessage message : (Set<FeedMessage>) feedAggregate.getAggregatedList()) {
            Assert.isTrue(null != message.getLink() || !message.getLink().isEmpty(),
                    "link is null or empty");
        }
    }

    @Test
    public void feedAggregateSizeIsConsistent() throws MalformedURLException {
        int size1, size2;
        do {
            int index = random.nextInt(350);
            int length = 1;
            System.out.println("index: " + index);
            size1 = simpleFeedAggregator.aggregateUsingThreadPools(feeds.subList(index, index + length), 20).getAggregatedList().size();
            size2 = simpleFeedAggregator.aggregateUsingThreadPools(feeds.subList(index, index + length), 20).getAggregatedList().size();
            System.out.println("size1: " + size1 + " , size2: " + size2);

            Assert.isTrue(size1 == size2, "aggregated size is not consistent");
        } while (size1 == 0);

    }

    @Test
    public void feedAggregateIsOrderedByDate() throws MalformedURLException {
        int length = 10;
        int index = random.nextInt(350);

        Set<FeedMessage> feedMessages = simpleFeedAggregator.aggregateUsingThreadPools(feeds.subList(index, index + length),20).getAggregatedList();
        FeedMessage prev = null;
        for(FeedMessage message: feedMessages){
            if(prev != null){
                // New message is more recent than prev message
                assertTrue(message.getPubDate().compareTo(prev.getPubDate()) <= 0);
            }
            prev = message;
        }
    }
}
