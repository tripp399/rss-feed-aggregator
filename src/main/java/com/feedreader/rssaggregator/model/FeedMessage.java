package com.feedreader.rssaggregator.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FeedMessage {
    private static final DateFormat PUBDATE_FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

    String title;
    String description;
    String author;
    String guid;
    Date pubDate;
    URL link;
    URL source;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link.toString();
    }

    public void setLink(String link) throws MalformedURLException {
        this.link = new URL(link);
        this.source = new URL(this.link.getProtocol() + "://" + this.link.getHost());
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) throws ParseException {
        this.pubDate = PUBDATE_FORMATTER.parse(pubDate);
    }

    public URL getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "FeedMessage [title=" + title + ", description=" + description
                + ", link=" + link + ", author=" + author + ", guid=" + guid
                + "]";
    }

}