package com.feedreader.rssaggregator.model;


public class FeedsStore {

	final String title;
	final String htmlUrl;
	final String xmlUrl;
	final String text;
	final String type;

	public FeedsStore(String title,String htmlUrl,String xmlUrl,String text,String type) {
		this.htmlUrl=htmlUrl;
		this.title=title;
		this.xmlUrl=xmlUrl;
		this.text=text;
		this.type=type;
	}

	public String getTitle() {
		return title;
	}

	public String getHtmlUrl() {
		return htmlUrl;
	}

	public String getXmlUrl() {
		return xmlUrl;
	}

	public String getText() {
		return text;
	}

	public String getType() {
		return type;
	}


}
