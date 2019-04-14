package com.feedreader.rssaggregator.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
				thread.join(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    });
    
    writeToFile(
      "C:/Users/pulki/Academics/Spring-19/CP/Project/rss-feed-aggregator/src/main/resources/feeds.txt", 
      feedsList
    );

		return feedsList;
		
  }
  
  private boolean writeToFile(String filePath, HashSet<FeedsStore> feedsSet) {
    try {
      FileWriter w = new FileWriter(filePath);

      PrintWriter writer = new PrintWriter(w);

      for (FeedsStore feedStore : feedsSet) {
        writer.println(feedStore.getXmlUrl());
      }
      writer.close();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

}
