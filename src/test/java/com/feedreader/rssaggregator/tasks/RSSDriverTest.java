package com.feedreader.rssaggregator.tasks;

import com.feedreader.rssaggregator.model.FeedMessage;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;

public class RSSDriverTest {

    @Test
    public void run() {
        List<String> feeds = new ArrayList<>();
        feeds.add("http://billmaher.hbo.libsynpro.com/rss");
//        feeds.add("http://feeds.bbci.co.uk/news/rss.xml");
//        feeds.add("http://rss.nytimes.com/services/xml/rss/nyt/World.xml");

        BlockingQueue<FeedMessage> sortedFeedMessageQueue = new LinkedBlockingQueue<>();

        RSSDriver driver = new RSSDriver(sortedFeedMessageQueue, feeds);
        driver.run();
    }
}