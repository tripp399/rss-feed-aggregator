package com.feedreader.rssaggregator;

import com.feedreader.rssaggregator.model.FeedAggregate;
import com.feedreader.rssaggregator.tasks.RSSFeedParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class RssAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(RssAggregatorApplication.class, args);

//		FeedAggregate feedAggregate = aggregate(feeds);
	}

//	public static FeedAggregate aggregate(List<String> feeds) {
//		FeedAggregate feedAggregate = new FeedAggregate();
//		List<Thread> threads = new ArrayList<>();
//
//		feeds.forEach(feed -> {
//			RSSFeedParser parser = new RSSFeedParser(feed, feedAggregate);
//			Thread thread = new Thread(parser);
//			threads.add(thread);
//			thread.start();
//		});
//
//		threads.forEach(thread -> {
//			try {
//				thread.join();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		});
//
//		return feedAggregate;
//	}
}
