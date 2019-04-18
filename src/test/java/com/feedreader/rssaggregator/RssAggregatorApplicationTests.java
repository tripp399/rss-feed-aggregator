package com.feedreader.rssaggregator;

import com.feedreader.rssaggregator.tasks.SimpleFeedAggregator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


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

}
