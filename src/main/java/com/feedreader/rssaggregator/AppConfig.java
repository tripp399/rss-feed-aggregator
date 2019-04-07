package com.feedreader.rssaggregator;

import com.feedreader.rssaggregator.model.FeedAggregate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public FeedAggregate feedAggregate() {
        return new FeedAggregate();
    }
}
