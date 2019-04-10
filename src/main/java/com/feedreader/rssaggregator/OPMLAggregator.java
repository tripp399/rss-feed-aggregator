package com.feedreader.rssaggregator;

import java.util.*;

import com.feedreader.rssaggregator.model.FeedsStore;

public class OPMLAggregator {
	
	public HashSet<FeedsStore> aggregateOPML(List<String> opmlList){
		HashSet<FeedsStore> feedsList = new HashSet<FeedsStore>();
		List<Thread> threads = new ArrayList<>();

		opmlList.forEach(opml -> {
			OPMLParser parser = new OPMLParser(opml,feedsList);
			Thread thread = new Thread(parser);
			threads.add(thread);
			thread.start();
		});

		threads.forEach(thread -> {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		return feedsList;
		
	}

}
