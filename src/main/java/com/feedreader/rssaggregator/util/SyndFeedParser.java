package com.feedreader.rssaggregator.util;

import com.feedreader.rssaggregator.model.FeedMessage;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * SyndFeedParser it uses the ROME library for XML Parsing
 */
public class SyndFeedParser implements Runnable, Callable<SyndFeed> {
    private final URL url;
    private final Container<FeedMessage> container;
    private final boolean batch;

    /**
     * Constructor
     * @param url The url to parse
     * @param container The container on which to put the results
     * @param batch Whether to perform batch processing
     * @throws MalformedURLException
     */
    public SyndFeedParser(String url, Container<FeedMessage> container, boolean batch) throws MalformedURLException {
        this.url = new URL(url);
        this.container = container;
        this.batch = batch;
    }

    /**
     * Parse the items from a feed one by one, and put the results in a container
     * @return SyndFeed callable object for future
     */
    private SyndFeed parse() {
        SyndFeed feed = null;
        try {
            feed = new SyndFeedInput().build(new XmlReader(url));

//            System.out.println("[QueuedSyndParser] Now Parsing "+url+" | Entries:"+feed.getEntries().size());
            feed.getEntries().forEach(entry -> {
                if(entry.getPublishedDate() == null){
                    entry.setPublishedDate(new Date());
                }
                FeedMessage fm = new FeedMessage(entry.getTitle(),
                        entry.getDescription().getValue(),
                        entry.getLink(),
                        entry.getAuthor(),
                        entry.getPublishedDate(),
                        entry.getUri());
                this.container.add(fm);
            });
//            System.out.println("\t[QueuedSyndParser] Done Parsing "+url+" | Entries:"+feed.getEntries().size());

        } catch (FeedException|IOException|NullPointerException e) {
//            System.out.println("[QueuedSyndParser] Error in feed "+url);
        } catch(Exception e){
//            System.out.println("[QueuedSyndParser] Unexpected Error in feed "+url);
//            e.printStackTrace();
        }
        return feed;
    }

    /**
     * Parse the XML file and bulk add the elements in the container
     * @return SyndFeed object which can be called
     */
    private SyndFeed parseBatch(){
        SyndFeed feed = null;
        try {
            Set<FeedMessage> messages = new HashSet<>();
            feed = new SyndFeedInput().build(new XmlReader(url));

            feed.getEntries().forEach(entry -> {
                if(entry.getPublishedDate() == null){
                    entry.setPublishedDate(new Date());
                }
                FeedMessage fm = new FeedMessage(entry.getTitle(),
                        entry.getDescription().getValue(),
                        entry.getLink(),
                        entry.getAuthor(),
                        entry.getPublishedDate(),
                        entry.getUri());

                messages.add(fm);
            });
            this.container.addAll(messages);
            System.out.println("[QueuedSyndParser] Done Parsing "+url+" | Entries:"+feed.getEntries().size());

        } catch (FeedException|IOException|NullPointerException e) {
            System.out.println("[QueuedSyndParser] Error in feed "+url);
        } catch(Exception e){
            System.out.println("[QueuedSyndParser] Unexpected Error in feed "+url);
//            e.printStackTrace();
        }
        return feed;
    }

    @Override
    public void run() {
        if(this.batch){
            parseBatch();
        }else{
            parse();
        }
    }

    @Override
    public SyndFeed call() {
        return parse();
    }
}
