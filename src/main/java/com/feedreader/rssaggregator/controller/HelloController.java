package com.feedreader.rssaggregator.controller;

import com.feedreader.rssaggregator.ApplicationContextProvider;
import com.feedreader.rssaggregator.model.FeedAggregate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String sayHello() {
        return "Hello World!";
    }

    @RequestMapping("/feeds")
    public FeedAggregate getFeedAggregate() {
        return (FeedAggregate) ApplicationContextProvider.getApplicationContext().getBean("feedAggregate");
    }
}