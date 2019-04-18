package com.feedreader.rssaggregator.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * OPMLAggregator, a class which takes in set of urls which have opml files and creates an aggregated file.
 */
public class OPMLAggregator {

	/**
	 * Method which accepts a set of urls which contain opml files and creates an aggregated list of it
	 * @param opmlList Set of urls
	 * @return A list of feed urls which are parsed from opml file
	 * @throws IOException
	 */
	public ConcurrentSkipListSet<String> aggregateOPML(List<String> opmlList) throws IOException{
		ConcurrentSkipListSet<String> feedsSet = new ConcurrentSkipListSet<>();
		List<Thread> threads = new ArrayList<>();

		opmlList.forEach(opml -> {
			OPMLParser parser = new OPMLParser(opml,feedsSet);
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
		FileWriter writer = new FileWriter(new File("full3.txt"));
		BufferedWriter bw = new BufferedWriter(writer);
		int i=0;
		for(String s : feedsSet) {
			bw.write(s);
			bw.newLine();
			bw.flush();
		}

		return feedsSet;

	}
}
