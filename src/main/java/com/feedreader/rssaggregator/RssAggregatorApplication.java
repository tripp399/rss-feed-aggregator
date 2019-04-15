package com.feedreader.rssaggregator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.feedreader.rssaggregator.model.FeedAggregate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
public class RssAggregatorApplication {

  private static FeedAggregate feedAggregate;

  // public static boolean threadPool=false;

  public static void main(String[] args) {
    SpringApplication.run(RssAggregatorApplication.class, args);
    // HashSet<FeedsStore> feedsList = new HashSet<FeedsStore>();
    System.out.println("Starting...");
    long start = System.currentTimeMillis();

    List<String> feeds = new ArrayList<>();
    try {
      File file = new ClassPathResource("feeds.txt").getFile();
      Scanner scanner = new Scanner(file);

      while (scanner.hasNextLine()) {
        feeds.add(scanner.nextLine());
      }
      scanner.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    long finish = System.currentTimeMillis();
    System.out.println(finish - start);

    // feeds.add("http://rss.nytimes.com/services/xml/rss/nyt/World.xml");
    // feeds.add("https://thedatafarm.com/blog/feed/");

    feedAggregate = aggregate(feeds.subList(0, 50));

    long aggregatedAt = System.currentTimeMillis();
  }

  public static FeedAggregate aggregate(List<String> feeds) {
    // ApplicationContext context = ApplicationContextProvider.getApplicationContext();
    // FeedAggregate feedAggregate = (FeedAggregate) context.getBean("feedAggregate");
    
    FeedAggregate feedAggregate = new FeedAggregate();

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

  public static FeedAggregate getAggregate() {
    return feedAggregate;
  }

}
