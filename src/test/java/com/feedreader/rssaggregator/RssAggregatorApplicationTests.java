package com.feedreader.rssaggregator;

import com.feedreader.rssaggregator.tasks.SimpleFeedAggregator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RssAggregatorApplicationTests {

  private List<String> feeds = new ArrayList<>();
  private SimpleFeedAggregator simpleFeedAggregator;

  @Before
  public void init() {
    simpleFeedAggregator = new SimpleFeedAggregator();
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

  @Ignore
  @Test
  public void threadPoolScalabilityMeasurement() throws MalformedURLException {

    Map<Integer, Long> executionTimeMap = new HashMap<>();
    List<Long> aggregationTimes = new ArrayList<>();
    for (int i = 0, j = 0; j < 350; i++, j = j + 50) {
      final long start = System.currentTimeMillis();
      System.out.println("Aggregating" + j);
      int size = simpleFeedAggregator.aggregateUsingThreadPools(feeds.subList(0, j), 20).getAggregatedList().size();
      long finish = System.currentTimeMillis();

      executionTimeMap.put(size, (finish - start));
      System.out.println(size + ", " + (finish - start));
    }
  }

}
