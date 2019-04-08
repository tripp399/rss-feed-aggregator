package com.feedreader.rssaggregator;

import com.feedreader.rssaggregator.model.FeedAggregate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class RssAggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(RssAggregatorApplication.class, args);

        List<String> feeds = new ArrayList<>();
        feeds.add("http://podcasts.joerogan.net/feed");
        feeds.add("http://feeds.bbci.co.uk/news/rss.xml");
        feeds.add("http://rss.nytimes.com/services/xml/rss/nyt/World.xml");

        FeedAggregate feedAggregate = aggregate(feeds);
    }

    public static FeedAggregate aggregate(List<String> feeds) {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        FeedAggregate feedAggregate = (FeedAggregate) context.getBean("feedAggregate");
        List<Thread> threads = new ArrayList<>();

        feeds.forEach(feed -> {
            RSSFeedParser parser = new RSSFeedParser(feed, feedAggregate);
            Thread thread = new Thread(parser);
            threads.add(thread);
            thread.start();
        });

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        return feedAggregate;
    }
}
