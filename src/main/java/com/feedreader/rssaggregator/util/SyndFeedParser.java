package com.feedreader.rssaggregator.util;

import com.feedreader.rssaggregator.model.FeedAggregate;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.ParsingFeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

public class SyndFeedParser implements Runnable, Callable<SyndFeed> {

    private final URL url;
    private FeedAggregate<SyndEntry> feedAggregate;

    public SyndFeedParser(String url, FeedAggregate<SyndEntry> feedAggregate) throws MalformedURLException {
//    public SyndFeedParser(String url) throws MalformedURLException {
        this.url = new URL(url);
        this.feedAggregate = feedAggregate;
    }

    private SyndFeed parse() {
        SyndFeed feed = null;
        try {
            feed = new SyndFeedInput().build(new XmlReader(url));

            feed.getEntries().forEach(syndEntry -> {
                feedAggregate.getAggregatedList().add(syndEntry);
            });

//            feedAggregate.getAggregatedList().addAll(feed.getEntries());
        } catch (FeedException e) {
//            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return feed;
    }

    @Override
    public void run() {
        parse();
    }

    @Override
    public SyndFeed call() {
        return parse();
    }
}
