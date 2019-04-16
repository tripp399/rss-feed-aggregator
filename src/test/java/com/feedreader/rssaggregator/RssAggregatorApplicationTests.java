package com.feedreader.rssaggregator;

import com.feedreader.rssaggregator.model.FeedAggregate;
import com.feedreader.rssaggregator.model.FeedMessage;
import com.rometools.rome.feed.synd.SyndEntry;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

import static com.feedreader.rssaggregator.RssAggregatorApplication.aggregate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RssAggregatorApplicationTests {

  private List<String> feeds;
  private Random random;


  @Before
  public void init() {
    random = new Random();
    feeds = new ArrayList<>();
    try {
      File file = new ClassPathResource("feeds.txt").getFile();
      Scanner scanner = new Scanner(file);
      while (scanner.hasNextLine()) {
        feeds.add(scanner.nextLine());
      }
      scanner.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void contextLoads() {
  }

  @Test
  public void feedAggregateIsNotEmpty() throws MalformedURLException {

    int length = 20;
    int index = random.nextInt(300);
    System.out.println("starting");
    long start = System.currentTimeMillis();
//    FeedAggregate feedAggregate = aggregate(feeds.subList(index, index + length), 20);
    FeedAggregate feedAggregate = aggregate(feeds, 15);
    long finish = System.currentTimeMillis();
    System.out.println(finish - start);
    System.out.println(feedAggregate.getAggregatedList().size());

    Assert.notEmpty(feedAggregate.getAggregatedList(), "List is Empty");
  }

  @Test
  public void feedMessageLinkIsNotEmpty() throws MalformedURLException {

    int length = 20;
    int index = random.nextInt(300);
    System.out.println("starting");
    long start = System.currentTimeMillis();
//    FeedAggregate feedAggregate = aggregate(feeds.subList(index, index + length), 20);
    FeedAggregate feedAggregate = aggregate(feeds, 20);
    long finish = System.currentTimeMillis();
    System.out.println(finish - start);
    System.out.println(feedAggregate.getAggregatedList().size());

//    for (SyndEntry entry: (List<SyndEntry>) feedAggregate.getAggregatedList()) {
//      Assert.isTrue(null != entry.getLink() || ! entry.getLink().isEmpty(),
//              "link is null or empty");
//    }

    for (FeedMessage message : (List<FeedMessage>) feedAggregate.getAggregatedList()) {
      Assert.isTrue(null != message.getLink() || !message.getLink().isEmpty(),
              "link is null or empty");
    }
  }

  @Test
  public void feedAggregateContainsAllMessages() throws MalformedURLException {
    int sum = 0;
    do {
      int length = 1;
      int index = random.nextInt(350);
      int[] size = new int[4];

      for (int i = index, j = 0; j < 3; i = i + length, j++) {
        size[j] = aggregate(feeds.subList(i, i + length), 20).getAggregatedList().size();
        sum += size[j];
      }
      size[3] = aggregate(feeds.subList(index, index + 3 * length), 20).getAggregatedList().size();

      Assert.isTrue(sum == size[3], "Aggregate size not consistent");
    } while(sum == 0);
  }

  @Test
  public void feedAggregateSizeIsConsistent() throws MalformedURLException {
      int size1, size2;
      do {
        int index = random.nextInt(350);
        int length = 1;
        System.out.println("index: " + index);
        size1 = aggregate(feeds.subList(index, index + length), 20).getAggregatedList().size();
        size2 = aggregate(feeds.subList(index, index + length)).getAggregatedList().size();
        System.out.println("size1: " + size1 + " , size2: " + size2);

        Assert.isTrue(size1 == size2, "aggregated size is not consistent");
      } while (size1 == 0);

  }

  @Ignore
  @Test
  public void feedAggregateIsOrderedByDate() throws MalformedURLException {
    int length = 10;
    int index = random.nextInt(350);

    List<SyndEntry> feedMessages = aggregate(feeds.subList(index, index + length),20).getAggregatedList();

    for (int i = 0; i < feedMessages.size() - 1; i++) {
      Date d1 = feedMessages.get(i).getPublishedDate();
      Date d2 = feedMessages.get(i+1).getPublishedDate();
      Assert.isTrue( d1.compareTo(d2) <= 0 , "Messages out of order");
    }
  }

  @Ignore
  @Test
  public void threadPoolScalabilityMeasurement() throws MalformedURLException {

    Map<Integer, Long> executionTimeMap = new HashMap<>();
    List<Long> aggregationTimes = new ArrayList<>();

    for (int i = 0, j = 0; j < 350; i++, j = j + 50) {
      final long start = System.currentTimeMillis();
      System.out.println("Aggregating" + j);
      int size = aggregate(feeds.subList(0, j), 20).getAggregatedList().size();
      long finish = System.currentTimeMillis();

      executionTimeMap.put(size, (finish - start));
//      System.out.println(size + ", " + (finish - start));
    }


  }

}
