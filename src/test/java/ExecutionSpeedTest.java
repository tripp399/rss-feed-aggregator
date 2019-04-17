import com.feedreader.rssaggregator.model.FeedAggregate;
import com.feedreader.rssaggregator.model.FeedMessage;
import com.feedreader.rssaggregator.tasks.BlockingQueueFeedAggregator;
import com.feedreader.rssaggregator.tasks.FeedScanner;
import com.feedreader.rssaggregator.tasks.SimpleFeedAggregator;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

@FixMethodOrder(MethodSorters.JVM)
public class ExecutionSpeedTest {

    private List<String> feeds;
    private SimpleFeedAggregator simpleFeedAggregator;

    @Before
    public void init() {
        simpleFeedAggregator = new SimpleFeedAggregator();
        feeds = new ArrayList<>();
        try {
            File file = new ClassPathResource("allFeeds.txt").getFile();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                feeds.add(scanner.nextLine());
            }
            System.out.println(feeds.size());
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void blockingQueueAggregatorSpeedTest() throws InterruptedException {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(50);
        BlockingQueue<FeedMessage> queue = new LinkedBlockingQueue<>();
        FeedScanner scanner = new FeedScanner(queue);
        for(String url: feeds){
            scanner.addSource(url);
        }
        BlockingQueueFeedAggregator aggregator = new BlockingQueueFeedAggregator(queue);
        exec.scheduleWithFixedDelay(aggregator, 0, 1, TimeUnit.SECONDS);
        exec.scheduleAtFixedRate(scanner, 0, 300, TimeUnit.SECONDS);
        Long start = System.currentTimeMillis();
        while(!aggregator.isDone());
        Long end = System.currentTimeMillis();
        System.out.println("[BLOCKING QUEUE] Got "+aggregator.getByPubDate().size()+" elements from "+feeds.size()+" feeds in "+(end - start)/1000+" seconds");
    }

    public void threadPoolAggregatorSpeedTest(){
        long start = System.currentTimeMillis();
        FeedAggregate feedAggregate
                = simpleFeedAggregator.aggregateUsingThreadPools(feeds,100);
        long end = System.currentTimeMillis();
        System.out.println("[THREAD POOL] Got "+feedAggregate.getAggregatedList().size()+" elements from "+feeds.size()+" feeds in "+(end-start)/1000+ " seconds");
    }

    public void directMappingAggregateSpeedTest(){
        long start = System.currentTimeMillis();
        FeedAggregate feedAggregate = simpleFeedAggregator.aggregateUsingDirectMapping(feeds);
        long end = System.currentTimeMillis();
        System.out.println("[DIRECT MAPPING] Got "+feedAggregate.getAggregatedList().size()+" elements from "+feeds.size()+" feeds in "+(end-start)/1000+ " seconds");
    }
}
