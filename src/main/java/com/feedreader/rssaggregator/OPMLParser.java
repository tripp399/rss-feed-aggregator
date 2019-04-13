package com.feedreader.rssaggregator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import com.feedreader.rssaggregator.model.FeedAggregate;
import com.feedreader.rssaggregator.model.FeedsStore;

public class OPMLParser implements Runnable{

	private static final String TITLE = "title";
	private static final String HTMLURL= "htmlUrl";
	private static final String XMLURL = "xmlUrl";
	private static final String TEXT = "text";
	private static final String TYPE = "type";
	private final URL opmlUrl;
	HashSet<FeedsStore> feedsList = new HashSet<FeedsStore>();
	


	public OPMLParser(String opmlUrl,HashSet<FeedsStore> feedsList ) {
		this.feedsList=feedsList;
		try {
			this.opmlUrl = new URL(opmlUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public HashSet<FeedsStore> parseOPML(){
		try {
			String title="";
			String htmlUrl ="";
			String xmlUrl="";
			String text="";
			String type="";

			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = read();
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				try {
					if(event.isStartElement() && event.asStartElement().getName()!=null) {
						String eventName = event.asStartElement().getName().toString();
						if(eventName.equals("outline")) {
							Iterator<Attribute> attributes = event.asStartElement().getAttributes();
							while(attributes.hasNext()){
								Attribute attribute = attributes.next();
								if(attribute.getName()!=null) {
									String name = attribute.getName().toString();
									switch(name) {
									case TITLE:
										title= attribute.getValue();
										break;
									case TEXT:
										text = attribute.getValue();
										break;
									case HTMLURL:
										htmlUrl=attribute.getValue();
										break;
									case XMLURL:
										xmlUrl=attribute.getValue();
										break;
									case TYPE:
										type= attribute.getValue();
										break;
									default:
										break;

									}
								}
							}
						}
					}
				}
				catch(ClassCastException ce) {
					continue;
				}
				if(!xmlUrl.isEmpty()) {
					FeedsStore feedStore = new FeedsStore(title, htmlUrl, xmlUrl, text, type);
					feedsList.add(feedStore);
				}
			}
		}
		catch (Exception e) {
			System.out.println(e);
			throw new RuntimeException(e);
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
