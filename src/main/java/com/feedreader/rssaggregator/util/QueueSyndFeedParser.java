package com.feedreader.rssaggregator.util;
import com.feedreader.rssaggregator.model.FeedMessage;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class QueueSyndFeedParser implements Runnable, Callable<SyndFeed> {

    private final URL url;
    private final BlockingQueue<FeedMessage> queue;

    public QueueSyndFeedParser(String url, BlockingQueue<FeedMessage> queue) throws MalformedURLException {
        this.url = new URL(url);
        this.queue = queue;
    }

    private SyndFeed parse() {
        SyndFeed feed = null;
        try {
            feed = new SyndFeedInput().build(new XmlReader(url));

            feed.getEntries().forEach(entry -> {
                FeedMessage fm = new FeedMessage(entry.getTitle(),
                        entry.getDescription().getValue(),
                        entry.getLink(),
                        entry.getAuthor(),
                        entry.getPublishedDate());

                try {
                    this.queue.put(fm);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        } catch (FeedException|IOException e) {
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
