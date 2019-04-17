package com.feedreader.rssaggregator.controller;

import com.feedreader.rssaggregator.ApplicationContextProvider;
import com.feedreader.rssaggregator.model.FeedMessage;
import com.feedreader.rssaggregator.tasks.BlockingQueueFeedAggregator;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class FeedController {
    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping("/feeds/v1")
    public Set<FeedMessage> getFeeds(){
        BlockingQueueFeedAggregator aggregator = (BlockingQueueFeedAggregator)ApplicationContextProvider.getApplicationContext().getBean("feedAggregator");
        Set<FeedMessage> msg = aggregator.getByPubDate();
        System.out.println("Feeds size:"+msg.size());
        return msg;
    }

}
