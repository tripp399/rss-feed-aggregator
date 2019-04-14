package com.feedreader.rssaggregator;

import com.feedreader.rssaggregator.model.FeedAggregate;
import com.feedreader.rssaggregator.model.FeedsStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class RssAggregatorApplication {
	
	//public static boolean threadPool=false;

	public static void main(String[] args) {
		SpringApplication.run(RssAggregatorApplication.class, args);
		HashSet<FeedsStore> feedsList = new HashSet<FeedsStore>();

		List<String> opmlList = Arrays.asList(Constants.URL1,Constants.URL2,Constants.URL3,Constants.URL4,Constants.URL5);

		OPMLAggregator op = new OPMLAggregator();
		HashSet<FeedsStore> allFeedsList = op.aggregateOPML(opmlList);
		//	System.out.println("Finished with all threads in "+(end-start)+"ms");
		//	System.out.println("Complete");

		//allFeedsList contains all the xmlUrls to be displayed

		List<String> feeds = new ArrayList<>();
		for(FeedsStore feedStore: allFeedsList) {
			feeds.add(feedStore.getXmlUrl());
		}

		FeedAggregate feedAggregate = aggregate(feeds);
	}

	public static FeedAggregate aggregate(List<String> feeds) {
		ApplicationContext context = ApplicationContextProvider.getApplicationContext();
		FeedAggregate feedAggregate = (FeedAggregate) context.getBean("feedAggregate");
		int n = 100;
		boolean threadPool=true;
		if(threadPool) {
			ExecutorService  exec= Executors.newFixedThreadPool(n);
			for(String feed:feeds) {
				Runnable thread = new RSSFeedParser(feed, feedAggregate);
				exec.execute(thread);
			}
			exec.shutdown();
			while(!exec.isTerminated()) {

			}

		}
		else {
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
		}

		return feedAggregate;
	}
}
