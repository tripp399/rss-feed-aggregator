package com.feedreader.rssaggregator.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import com.feedreader.rssaggregator.model.FeedsStore;

public class OPMLAggregator {

	public HashSet<String> aggregateOPML(List<String> opmlList){
		HashSet<String> feedsList = new HashSet<>();
		List<Thread> threads = new ArrayList<>();

		opmlList.forEach(opml -> {
			OPMLParser parser = new OPMLParser(opml,feedsList);
			Thread thread = new Thread(parser);
			threads.add(thread);
			thread.start();
		});

		threads.forEach(thread -> {
			try {
				thread.join(25000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    });

		return feedsList;

  }

}
