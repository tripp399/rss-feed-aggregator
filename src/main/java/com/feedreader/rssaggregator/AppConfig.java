package com.feedreader.rssaggregator;

import com.feedreader.rssaggregator.model.FeedMessage;
import com.feedreader.rssaggregator.tasks.BlockingQueueFeedAggregator;
import com.feedreader.rssaggregator.tasks.FeedScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
@ComponentScan(basePackageClasses = BlockingQueueFeedAggregator.class)
@ComponentScan(basePackageClasses = FeedScanner.class)
public class AppConfig {

    @Bean
    public BlockingQueue<FeedMessage> feedMessageQueue(){
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public BlockingQueueFeedAggregator feedAggregator(){
        return new BlockingQueueFeedAggregator(feedMessageQueue());
    }

    @Bean
    public FeedScanner feedScanner(){
        return new FeedScanner(feedMessageQueue(), false);
    }
}
