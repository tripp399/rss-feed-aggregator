package com.feedreader.rssaggregator;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

import com.feedreader.rssaggregator.model.FeedAggregate;

import com.feedreader.rssaggregator.util.SyndFeedParser;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
public class RssAggregatorApplication {

  private static FeedAggregate feedAggregate;

  public static void main(String[] args) {
    SpringApplication.run(RssAggregatorApplication.class, args);
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

    feedAggregate = aggregate(feeds.subList(0, 50));
  }

    public static FeedAggregate aggregate(List<String> feeds, int poolSize) throws MalformedURLException {
        FeedAggregate feedAggregate = new FeedAggregate<>();

        ExecutorService exec = Executors.newFixedThreadPool(poolSize);
        List<Future<SyndFeed>> futures = new ArrayList<>();
        for (String feed : feeds) {
//            Runnable thread = new RSSFeedParser(feed, feedAggregate);
//            exec.submit(thread);
            Callable<SyndFeed> thread = new SyndFeedParser(feed, feedAggregate);
            futures.add(exec.submit(thread));
        }

//        futures.forEach(future -> {
//            try {
//                if (null != future.get()) {
//                    feedAggregate.getAggregatedList()
//                            .addAll(future.get().getEntries());
//                }
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        });
        exec.shutdown();
        while (!exec.isTerminated()) {

        }

        return feedAggregate;
    }

    public static FeedAggregate aggregate(List<String> feeds) {

        FeedAggregate feedAggregate = new FeedAggregate();
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

    public static FeedAggregate getAggregate() {
        return feedAggregate;
    }

}
