package com.feedreader.rssaggregator.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A class which represents the FeedItem in an RSS feed
 * After parsing the RSS Feed we will construct a set of FeedMessages
 */
public class FeedMessage implements Comparable{
    private static final DateFormat PUBDATE_FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    private String title;
    private String description;
    private String link;
    private String author;
    private String guid;
    private Date pubDate;
    private boolean isSentinel;


    /**
     * Constructor
     */
    public FeedMessage() {
        this.isSentinel = false;
    }

    /**
     * Constructor with all the elements to avoid using setters
     * @param title Title of RSS Item
     * @param description Description of RSS Item
     * @param link Link of the RSS Item
     * @param author Author of the RSS Item
     * @param pubDate Published date of the RSS Item
     * @param guid GUID which may or may not be present. Used to check the uniqueness of the RSS Feed item
     */
    public FeedMessage(String title, String description, String link, String author, Date pubDate, String guid) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.author = author;
        this.pubDate = pubDate;
        this.guid = guid;
        this.isSentinel = false;
    }

    /**
     * Getter for title
     * @return Title of the item of String type
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title
     * @param title Title of the item
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for description
     * @return String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for link
     * @return String
     */
    public String getLink() {
        return link;
    }

    /**
     * Setter for link
     * @param link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Getter for author
     * @return String
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Setter for author
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Getter for guid
     * @return String
     */
    public String getGuid() {
        return guid;
    }

    /**
     * Setter for guid
     * @param guid
     */
    public void setGuid(String guid) {
        this.guid = guid;
    }

    /**
     * Getter for pub date
     * @return Date object
     */
    public Date getPubDate() {
        return pubDate;
    }

    /**
     * Check whether the message is sentinel
     * @return
     */
    public boolean isSentinel() {
        return isSentinel;
    }

    /**
     * Indicate that this message is sentinel
     * @param sentinel
     */
    public void setSentinel(boolean sentinel) {
        isSentinel = sentinel;
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
        else{
            int compareStatus = this.getPubDate().compareTo(message.getPubDate());
            if(compareStatus == 0){
                return -1;
            }else{
                return compareStatus * (-1);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        FeedMessage message = (FeedMessage)obj;
        if(!this.guid.equals(message.getGuid())){
            return false;
        }else if(!this.link.equals(message.getLink())){
            return false;
        }else if(!this.title.equals(message.getTitle())){
            return false;
        }else{
            return true;
        }
    }
}