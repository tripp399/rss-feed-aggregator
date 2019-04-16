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
      File file = new ClassPathResource("validFeeds.txt").getFile();
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
    int index = random.nextInt(350);
    System.out.println("starting");
    long start = System.currentTimeMillis();
    FeedAggregate feedAggregate = aggregate(feeds.subList(index, index + length), 20);
    long finish = System.currentTimeMillis();
    System.out.println(finish - start);
    System.out.println(feedAggregate.getAggregatedList().size());

    for (FeedMessage message : feedAggregate.getAggregatedList()) {
      Assert.isTrue(null != message.getLink() || !message.getLink().isEmpty(),
              "link is null or empty");
    }

    Assert.notEmpty(feedAggregate.getAggregatedList(), "List is Empty");
  }

  @Test
  public void feedMessageLinkIsNotEmpty() {

    int length = 20;
    int index = random.nextInt(350);
    System.out.println("starting");
    long start = System.currentTimeMillis();
    FeedAggregate feedAggregate = aggregate(feeds.subList(index, index + length), 20);
    long finish = System.currentTimeMillis();
    System.out.println(finish - start);
    System.out.println(feedAggregate.getAggregatedList().size());

    for (FeedMessage message : feedAggregate.getAggregatedList()) {
      Assert.isTrue(null != message.getLink() || !message.getLink().isEmpty(),
              "link is null or empty");
    }

    Assert.notEmpty(feedAggregate.getAggregatedList(), "List is Empty");
  }

  @Test
  public void feedAggregateContainsAllMessages() {
    int sum = 0;
    do {
      int length = 1;
      int index = random.nextInt(350);
      int[] size = new int[4];


      for (int i = index, j = 0; j < 3; i = i + length, j++) {
        size[j] = aggregate(feeds.subList(i, i + length), 20).getAggregatedList().size();
        sum += size[j];
      }
      size[3] = aggregate(feeds.subList(index, index + 3 * length)).getAggregatedList().size();

      Assert.isTrue(sum == size[3], "Aggregate size not consistent");
    } while(sum == 0);
  }

  @Test
  public void feedAggregateSizeIsConsistent() {
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
  public void feedAggregateIsOrderedByDate() {
    int length = 10;
    int index = random.nextInt(350);

    List<FeedMessage> feedMessages = aggregate(feeds.subList(index, index + length),20).getAggregatedList();

    for (int i = 0; i < feedMessages.size() - 1; i++) {
      Date d1 = feedMessages.get(i).getPubDate();
      Date d2 = feedMessages.get(i+1).getPubDate();
      Assert.isTrue( d1.compareTo(d2) <= 0 , "Messages out of order");
    }
  }

  @Ignore
  @Test
  public void threadPoolScalabilityMeasurement() {

    List<Long> aggregationTimes = new ArrayList<>();
    final long start = System.currentTimeMillis();

    System.out.println("Aggregating");
    int size = aggregate(feeds.subList(0, 150), 20).getAggregatedList().size();
    long finish = System.currentTimeMillis();
    System.out.println(size + ", " + (finish - start));
    Assert.isTrue(true, "");
  }

//  @Ignore
//  @Test
//  public void persistListOfValidFeeds() {
//
//    OPMLAggregator agg = new OPMLAggregator();
//    System.out.println("Collecting feed urls");
//    HashSet<String> feeds = agg.aggregateOPML(Arrays.asList(Constants.URL1, Constants.URL2, Constants.URL3,
//            Constants.URL4, Constants.URL5, Constants.URL6));
//
//    List<Future<?>> futuresList = new ArrayList<>();
//    Set<String> validUrls = new HashSet<>();
//    ExecutorService exec = Executors.newFixedThreadPool(20);
//    System.out.println("Submitting tasks");
//    FeedAggregate feedAggregate = new FeedAggregate();
//    for (String feed : feeds) {
//      Callable<String> thread = new RSSFeedParser(feed, feedAggregate);
//      futuresList.add(exec.submit(thread));
//    }
//
//    System.out.println("Checking for timeout");
//    futuresList.forEach(future -> {
//      try {
//        String result = (String) future.get(2, TimeUnit.SECONDS);
//        validUrls.add(result);
//      } catch (TimeoutException e) {
//        future.cancel(true);
//        e.printStackTrace();
//      } catch (InterruptedException e) {
//      } catch (ExecutionException e) {
//      }
//    });
//
//    String filePath =
//            "C:\\Users\\pulki\\Workspace\\IdeaProjects\\CPPRoject\\src\\main\\resources\\validFeeds.txt";
//
//    System.out.println("writing to file");
//    validUrls.forEach(s -> {
//      try {
//        FileWriter w = new FileWriter(filePath);
//
//        PrintWriter writer = new PrintWriter(w);
//
//        for (String validUrl : validUrls) {
//          writer.println(validUrl);
//        }
//        writer.close();
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
//    });
//
//    exec.shutdown();
//    while (!exec.isTerminated()) {
//
//    }
//  }
}
