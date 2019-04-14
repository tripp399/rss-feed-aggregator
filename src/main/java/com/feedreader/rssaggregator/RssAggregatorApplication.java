package com.feedreader.rssaggregator;

import com.feedreader.rssaggregator.model.FeedAggregate;
import com.feedreader.rssaggregator.model.FeedsStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.*;

@SpringBootApplication
public class RssAggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(RssAggregatorApplication.class, args);


        List<String> opmlList = new ArrayList<String>();
		opmlList.add("http://paulirish.github.io/frontend-feeds/frontend-feeds.opml");
		opmlList.add("https://raw.githubusercontent.com/yasuharu519/opml/master/main.opml");
		opmlList.add("https://github.com/cudeso/OPML-Security-Feeds/blob/master/feedly.opml");

		OPMLAggregator op = new OPMLAggregator();
		HashSet<FeedsStore> allFeedsList = op.aggregateOPML(opmlList);
		//allFeedsList contains all the xmlUrls to be displayed

        List<String> feeds = new ArrayList<>();

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
