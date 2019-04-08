package com.feedreader.rssaggregator.tasks;

import com.feedreader.rssaggregator.model.FeedMessage;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;

public class RSSFeedParser implements Runnable {

    // Variables in a FeedMessage to be parsed
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String LANGUAGE = "language";
    private static final String COPYRIGHT = "copyright";
    private static final String LINK = "link";
    private static final String AUTHOR = "author";
    private static final String ITEM = "item";
    private static final String PUB_DATE = "pubDate";
    private static final String GUID = "guid";

    private final URL url;
    private final BlockingQueue<FeedMessage> feedMessages;

    public RSSFeedParser(String feedUrl, BlockingQueue<FeedMessage> feedMessages) {
        this.feedMessages = feedMessages;
        try {
            this.url = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void readFeed() {
        try {
            boolean isFeedHeader = true;
            String description = "";
            String title = "";
            String link = "";
            String language = "";
            String copyright = "";
            String author = "";
            String pubdate = "";
            String guid = "";
            String pubDate = "";

            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = read();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            while (eventReader.hasNext()) {
                try{
                    XMLEvent event = eventReader.nextEvent();
                    if (event.isStartElement()) {
                        String localPart = event.asStartElement().getName().getLocalPart();

                        switch (localPart) {
                            case ITEM:
                                if (isFeedHeader) {
                                    isFeedHeader = false;
                                }
                                break;
                            case TITLE:
                                title = getCharacterData(eventReader);
                                break;
                            case DESCRIPTION:
                                description = getCharacterData(eventReader);
                                break;
                            case LINK:
                                link = getCharacterData(eventReader);
                                break;
                            case GUID:
                                guid = getCharacterData(eventReader);
                                break;
                            case LANGUAGE:
                                language = getCharacterData(eventReader);
                                break;
                            case AUTHOR:
                                author = getCharacterData(eventReader);
                                break;
                            case PUB_DATE:
                                pubdate = getCharacterData(eventReader);
                                break;
                            case COPYRIGHT:
                                copyright = getCharacterData(eventReader);
                                break;
                        }
                    } else if (event.isEndElement()) {
                        if (event.asEndElement().getName().getLocalPart().equals(ITEM)) {
                            FeedMessage message = new FeedMessage();
                            message.setAuthor(author);
                            message.setDescription(description);
                            message.setGuid(guid);
                            message.setLink(link);
                            message.setTitle(title);
                            message.setPubDate(pubdate);
                            System.out.println("Added a new message");
                            this.feedMessages.offer(message);
                        }
                    }
                }catch (Exception e){
                    continue;
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        System.out.println("-------PARSING "+this.url.getHost()+" COMPLETE!");
    }

    private String getCharacterData(XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        XMLEvent event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }

    private InputStream read() {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        readFeed();
    }
}