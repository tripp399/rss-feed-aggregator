package com.feedreader.rssaggregator.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FeedMessageTest {
    FeedMessage message;

    @Before
    public void init(){
        message = new FeedMessage();
    }

    @Test
    public void setTitle() {
        message.setTitle("abcd");
        Assert.assertEquals("abcd", message.getTitle());
    }

    @Test
    public void setDescription() {
        message.setDescription("abcd");
        Assert.assertEquals("abcd", message.getDescription());
    }

    @Test
    public void setLink() {
        message.setLink("abcd.com");
        Assert.assertEquals("abcd.com", message.getLink());
    }

    @Test
    public void getAuthor() {
        message.setAuthor("abcd");
        Assert.assertEquals("abcd", message.getAuthor());
    }

    @Test
    public void setAuthor() {
        message.setAuthor("abcd");
        Assert.assertEquals("abcd", message.getAuthor());
    }

    @Test
    public void setGuid() {
        message.setGuid("1234");
        Assert.assertEquals("1234", message
        .getGuid());
    }

    @Test
    public void isSentinel() {
        message.setSentinel(true);
        Assert.assertTrue(message.isSentinel());
    }
}