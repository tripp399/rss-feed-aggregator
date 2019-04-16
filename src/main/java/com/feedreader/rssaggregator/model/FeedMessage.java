package com.feedreader.rssaggregator.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class FeedMessage implements Comparable{

    private static final DateFormat PUBDATE_FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

    String title;
    String description;
    String link;
    String author;
    String guid;
    Date pubDate;

    public FeedMessage() {
    }

    public FeedMessage(String title, String description, String link, String author, Date pubDate) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.author = author;
        this.pubDate = pubDate;
    }

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
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    @Override
    public String toString() {
        return "FeedMessage [title=" + title + ", description=" + description
                + ", link=" + link + ", author=" + author + ", guid=" + guid
                + "]";
    }

    @Override
    public int compareTo(Object o) {
        FeedMessage message = (FeedMessage)o;
        if (this.equals(message))
            return 0;
        else
            return this.getPubDate().compareTo(message.getPubDate());
    }

    @Override
    public boolean equals(Object obj) {
        FeedMessage message = (FeedMessage)obj;
        if (this.getTitle().equals(message.getTitle())
                && this.getPubDate().equals(message.getPubDate())) {
            return true;
        }
        else return false;
    }
}