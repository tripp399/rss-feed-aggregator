package com.feedreader.rssaggregator.controller;

import com.feedreader.rssaggregator.ApplicationContextProvider;
import com.feedreader.rssaggregator.model.FeedMessage;
import com.feedreader.rssaggregator.tasks.BlockingQueueFeedAggregator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.TreeSet;

@RestController
public class FeedController {
    @RequestMapping("/feeds/v1")
    public TreeSet<FeedMessage> getFeeds(){
        BlockingQueueFeedAggregator aggregator = (BlockingQueueFeedAggregator)ApplicationContextProvider.getApplicationContext().getBean("feedAggregator");
        TreeSet<FeedMessage> msg = aggregator.getByPubDate();
        System.out.println("Feeds size:"+msg.size());
        return msg;
    }

}