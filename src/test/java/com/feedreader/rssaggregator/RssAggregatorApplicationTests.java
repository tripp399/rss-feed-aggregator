package com.feedreader.rssaggregator;

import com.feedreader.rssaggregator.model.FeedAggregate;
import com.feedreader.rssaggregator.model.FeedMessage;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
// import org.junit.Assert;

import java.io.File;
import java.io.IOException;
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
  public void feedAggregateIsNotEmpty() {
    int length = 20;
    int index = random.nextInt(550);
    FeedAggregate feedAggregate = aggregate(feeds.subList(index, index + length));

    Assert.notEmpty(feedAggregate.getAggregatedList(), "List is Empty");
  }

  @Test
  public void FeedAggregateContainsAllMessages() {
    int length = 5;
    int index = random.nextInt(550);
    int[] size = new int[4];
    int sum = 0;

    for (int i = index, j = 0; j < 3; i = i + length, j++) {
      size[j] = aggregate(feeds.subList(i, i + length)).getAggregatedList().size();
      sum += size[j];
    }
    size[3] = aggregate(feeds.subList(index, index + 3 * length)).getAggregatedList().size();

    Assert.isTrue(sum == size[3], "Aggregate size not consistent");
  }

  @Test
  public void feedAggregateSizeIsConsistent() {
    int length = random.nextInt(20);

    for (int i = 0; i < 3; i++) {
      int index = random.nextInt(550);
//      System.out.println("index: " + index);
      int size1 = aggregate(feeds.subList(index, index + length)).getAggregatedList().size();
      int size2 = aggregate(feeds.subList(index, index + length)).getAggregatedList().size();

      Assert.isTrue(size1 == size2, "aggregated size is not consistent");
    }
  }

  @Test
  public void feedAggregateIsOrderedByDate() {
    int length = 10;
    int index = random.nextInt(600);

    List<FeedMessage> feedMessages = aggregate(feeds.subList(index, index + length)).getAggregatedList();

    for (int i = 0; i < feedMessages.size() - 1; i++) {
      Date d1 = feedMessages.get(i).getPubDate();
      Date d2 = feedMessages.get(i+1).getPubDate();
      Assert.isTrue( d1.compareTo(d2) <= 0 , "Messages out of order");
    }
  }

}
