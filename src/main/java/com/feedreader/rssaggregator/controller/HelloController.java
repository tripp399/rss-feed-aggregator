package com.feedreader.rssaggregator.controller;

import com.feedreader.rssaggregator.ApplicationContextProvider;
import com.feedreader.rssaggregator.model.FeedAggregate;
import com.feedreader.rssaggregator.model.FeedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String sayHello() {
        return "Hello World!";
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping("/feeds")
    public List<FeedMessage> getFeedAggregate() {
        return ((FeedAggregate) ApplicationContextProvider.getApplicationContext().getBean("feedAggregate"))
                    .getAggregatedList();
    }
}