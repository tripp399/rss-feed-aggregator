package com.feedreader.rssaggregator;

import com.feedreader.rssaggregator.model.FeedAggregate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class RssAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(RssAggregatorApplication.class, args);

		List<String> feeds = new ArrayList<>();
		feeds.add("http://billmaher.hbo.libsynpro.com/rss");
		feeds.add("http://feeds.bbci.co.uk/news/rss.xml");
		feeds.add("http://rss.nytimes.com/services/xml/rss/nyt/World.xml");

		FeedAggregate feedAggregate = aggregate(feeds);
	}

	public static FeedAggregate aggregate(List<String> feeds) {
		FeedAggregate feedAggregate = new FeedAggregate();
		List<Thread> threads = new ArrayList<>();

		for (String feed : feeds) {
			RSSFeedParser parser = new RSSFeedParser(feed, feedAggregate);
			Thread thread = new Thread(parser);
			threads.add(thread);
			thread.start();
		}

		for (Thread thread: threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return feedAggregate;
	}
}
