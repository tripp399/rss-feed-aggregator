package com.feedreader.rssaggregator;

import com.feedreader.rssaggregator.tasks.FeedAggregator;
import com.feedreader.rssaggregator.tasks.FeedScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class RssAggregatorApplication {

    public static void main(String[] args){
        // Start sping application
        SpringApplication.run(RssAggregatorApplication.class, args);

        FeedScanner scanner = (FeedScanner)ApplicationContextProvider.getApplicationContext().getBean("feedScanner");

        List<String> feeds = new ArrayList<>();
        try {
            File file = new ClassPathResource("feeds.txt").getFile();
            Scanner scan = new Scanner(file);

            while (scan.hasNextLine()) {
                feeds.add(scan.nextLine());
            }
            scan.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        for(int i = 0; i < feeds.size(); i++){
            scanner.addSource(feeds.get(i));
        }

        FeedAggregator aggregator = (FeedAggregator)ApplicationContextProvider.getApplicationContext().getBean("feedAggregator");

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        scheduledExecutorService.scheduleAtFixedRate(scanner, 10, 300, TimeUnit.SECONDS);

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        executorService.submit(aggregator);
    }

//  private static FeedAggregate feedAggregate;
//
//  public static void main(String[] args) {
//    SpringApplication.run(RssAggregatorApplication.class, args);
//
//
//    List<String> feeds = new ArrayList<>();
//    try {
//      File file = new ClassPathResource("feeds.txt").getFile();
//      Scanner scanner = new Scanner(file);
//
//      while (scanner.hasNextLine()) {
//        feeds.add(scanner.nextLine());
//      }
//      scanner.close();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//    System.out.println("Starting...");
//    long start = System.currentTimeMillis();
//    feedAggregate = aggregate(feeds, 20);
//    long finish = System.currentTimeMillis();
//    System.out.println(finish - start);
//    System.out.println("size: " + feedAggregate.getAggregatedList().size());
//
//  }

//    public static FeedAggregate aggregate(List<String> feeds, int poolSize) {
//        FeedAggregate feedAggregate = new FeedAggregate<>();
//
//        ExecutorService exec = Executors.newFixedThreadPool(poolSize);
//        List<Future<SyndFeed>> futures = new ArrayList<>();
//        for (String feed : feeds) {
////            Runnable thread = new RSSFeedParser(feed, feedAggregate);
////            exec.submit(thread);
//            Callable<SyndFeed> thread = null;
////            try {
//////                thread = new QueueSyndFeedParser(feed, feedAggregate);
////            } catch (MalformedURLException e) {
////                e.printStackTrace();
////            }
//            futures.add(exec.submit(thread));
//        }
//
////        futures.forEach(future -> {
////            try {
////                if (null != future.get()) {
////                    feedAggregate.getAggregatedList()
////                            .addAll(future.get().getEntries());
////                }
////            } catch (InterruptedException | ExecutionException e) {
////                e.printStackTrace();
////            }
////        });
//        exec.shutdown();
//        while (!exec.isTerminated()) {
//
//        }
//
//        return feedAggregate;
//    }
//
//    public static FeedAggregate aggregate(List<String> feeds) {
//
//        FeedAggregate feedAggregate = new FeedAggregate();
//        List<Thread> threads = new ArrayList<>();
//
//        feeds.forEach(feed -> {
//            RSSFeedParser parser = new RSSFeedParser(feed, feedAggregate);
//            Thread thread = new Thread(parser);
//            threads.add(thread);
//            thread.start();
//        });
//
//        threads.forEach(thread -> {
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
//        return feedAggregate;
//    }
//
//    public static FeedAggregate getAggregate() {
//        return feedAggregate;
//    }

}
