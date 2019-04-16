package com.feedreader.rssaggregator.util;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Iterator;

public class OPMLParser implements Runnable {

    private static final String TITLE = "title";
    private static final String HTMLURL = "htmlUrl";
    private static final String XMLURL = "xmlUrl";
    private static final String TEXT = "text";
    private static final String TYPE = "type";
    private final URL opmlUrl;
    HashSet<String> feedsList;

    public OPMLParser(String opmlUrl, HashSet<String> feedsList) {
        this.feedsList = feedsList;
        try {
            this.opmlUrl = new URL(opmlUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public HashSet<String> parseOPML() {
        try {
            String title = "";
            String htmlUrl = "";
            String xmlUrl = "";
            String text = "";
            String type = "";

            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = read();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                try {
                    if (event.isStartElement() && event.asStartElement().getName() != null) {
                        String eventName = event.asStartElement().getName().toString();
                        if (eventName.equals("outline")) {
                            Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                            while (attributes.hasNext()) {
                                Attribute attribute = attributes.next();
                                if (attribute.getName() != null) {
                                    String name = attribute.getName().toString();
                                    switch (name) {
                                        case XMLURL:
                                            xmlUrl = attribute.getValue();
                                            break;
                                        default:
                                            break;

                                    }
                                }
                            }
                        }
                    }
                } catch (ClassCastException ce) {
                    continue;
                }
                if (!xmlUrl.isEmpty()) {
                    try {
                        URL url = new URL(xmlUrl);
                        URLConnection openConnection = url.openConnection();
                        openConnection.addRequestProperty(
                                "User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0"
                        );
                        openConnection.getInputStream();

                        feedsList.add(xmlUrl);
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // throw new RuntimeException(e);
        }
        return feedsList;
    }

    private InputStream read() {
        try {
            return opmlUrl.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        parseOPML();
    }

}
