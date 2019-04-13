package com.feedreader.rssaggregator;

import org.junit.Assert;
import org.junit.Test;

import com.feedreader.rssaggregator.OPMLAggregator;
import com.feedreader.rssaggregator.model.FeedsStore;

import java.util.*;

public class OPMLParserTest {
	@Test
	public void opmltest() {
		List<String> opmlList = new ArrayList<String>();
		opmlList.add("http://paulirish.github.io/frontend-feeds/frontend-feeds.opml");
		opmlList.add("https://raw.githubusercontent.com/yasuharu519/opml/master/main.opml");
		opmlList.add("https://github.com/cudeso/OPML-Security-Feeds/blob/master/feedly.opml");


		OPMLAggregator op = new OPMLAggregator();
		HashSet<FeedsStore> li = op.aggregateOPML(opmlList);
		System.out.println(li.size());
		for(FeedsStore f :li) {
			System.out.println(f.getXmlUrl());
		}
		Assert.assertTrue(!li.isEmpty());
	}
}
