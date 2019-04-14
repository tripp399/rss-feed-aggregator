package com.feedreader.rssaggregator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.feedreader.rssaggregator.model.FeedAggregate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class RssAggregatorApplication {

  // public static boolean threadPool=false;

  public static void main(String[] args) {
    SpringApplication.run(RssAggregatorApplication.class, args);
    // HashSet<FeedsStore> feedsList = new HashSet<FeedsStore>();
    System.out.println("Starting...");
    long start = System.currentTimeMillis();

    List<String> feeds = new ArrayList<>();
    try {
      Scanner scanner = new Scanner(
          new File("C:/Users/pulki/Academics/Spring-19/CP/Project/rss-feed-aggregator/src/main/resources/feeds.txt"));

      while (scanner.hasNextLine()) {
        feeds.add(scanner.nextLine());
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    long finish = System.currentTimeMillis();
    System.out.println(finish - start);

    // feeds.add("http://rss.nytimes.com/services/xml/rss/nyt/World.xml");
    // feeds.add("https://thedatafarm.com/blog/feed/");

    FeedAggregate feedAggregate = aggregate(feeds.subList(0, 50));

    System.out.println("size" + feedAggregate.getAggregatedList().size());
    long aggregatedAt = System.currentTimeMillis();
    System.out.println(aggregatedAt - finish);
    System.out.println("### aggregated");
  }

  public static FeedAggregate aggregate(List<String> feeds) {
    ApplicationContext context = ApplicationContextProvider.getApplicationContext();
    FeedAggregate feedAggregate = (FeedAggregate) context.getBean("feedAggregate");
    int n = 100;
    boolean threadPool = true;
    if (threadPool) {
      ExecutorService exec = Executors.newFixedThreadPool(n);
      for (String feed : feeds) {
        Runnable thread = new RSSFeedParser(feed, feedAggregate);
        exec.execute(thread);
      }
      exec.shutdown();
      while (!exec.isTerminated()) {

      }

    } else {
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
