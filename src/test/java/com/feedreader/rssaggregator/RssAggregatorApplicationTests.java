package com.feedreader.rssaggregator;

import com.feedreader.rssaggregator.model.FeedAggregate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.feedreader.rssaggregator.RssAggregatorApplication.aggregate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RssAggregatorApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void feedsAggregate() {
        List<String> feeds = new ArrayList<>();
        feeds.add("http://billmaher.hbo.libsynpro.com/rss");
        feeds.add("http://feeds.bbci.co.uk/news/rss.xml");
        feeds.add("http://rss.nytimes.com/services/xml/rss/nyt/World.xml");

        FeedAggregate feedAggregate = aggregate(feeds);

        Assert.notEmpty(feedAggregate.getAggregatedList(), "List is empty");
    }

}
