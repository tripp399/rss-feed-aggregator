package com.feedreader.rssaggregator;

import com.feedreader.rssaggregator.tasks.BlockingQueueFeedAggregator;
import com.feedreader.rssaggregator.tasks.FeedScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
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
        // Start spring application
        SpringApplication.run(RssAggregatorApplication.class, args);

        // Initialize scanner
        FeedScanner scanner = (FeedScanner)ApplicationContextProvider.getApplicationContext().getBean("feedScanner");

        // Initialize a set of feeds
        List<String> feeds = new ArrayList<>();
        try {
            InputStream file = new ClassPathResource("feeds.txt").getInputStream();
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

        // Initialize aggregator
        BlockingQueueFeedAggregator aggregator = (BlockingQueueFeedAggregator)ApplicationContextProvider.getApplicationContext().getBean("feedAggregator");

        // Start aggregator task
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        // Start periodic scanner task
        // It will be executed on a period of 5 minutes
        scheduledExecutorService.scheduleAtFixedRate(scanner, 0, 300, TimeUnit.SECONDS);

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(aggregator);
    }

}
