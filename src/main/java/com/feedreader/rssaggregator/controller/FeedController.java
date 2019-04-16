package com.feedreader.rssaggregator.controller;
import com.feedreader.rssaggregator.ApplicationContextProvider;
import com.feedreader.rssaggregator.model.FeedMessage;
import com.feedreader.rssaggregator.tasks.FeedAggregator;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class FeedController {
    @RequestMapping("/feeds/v1")
    public List<FeedMessage> getFeeds(){
        FeedAggregator aggregator = (FeedAggregator)ApplicationContextProvider.getApplicationContext().getBean("feedAggregator");
        List<FeedMessage> msg = aggregator.getByPubDate();
        System.out.println("Feeds size:"+msg.size());
        return msg;
    }

}
