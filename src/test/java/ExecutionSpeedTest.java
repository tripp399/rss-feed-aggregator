import com.feedreader.rssaggregator.model.FeedAggregate;
import com.feedreader.rssaggregator.model.FeedMessage;
import com.feedreader.rssaggregator.tasks.BlockingQueueFeedAggregator;
import com.feedreader.rssaggregator.tasks.FeedScanner;
import com.feedreader.rssaggregator.tasks.SimpleFeedAggregator;
import com.feedreader.rssaggregator.util.SyndFeedParser;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
            File file = new ClassPathResource("full3.txt").getFile();
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
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);
        BlockingQueue<FeedMessage> queue = new LinkedBlockingQueue<>();
        FeedScanner scanner = new FeedScanner(queue, false);
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

    @Test
    public void threadPoolAggregatorSpeedTest(){
        long start = System.currentTimeMillis();
        FeedAggregate feedAggregate
                = simpleFeedAggregator.aggregateUsingThreadPools(feeds,10);
        long end = System.currentTimeMillis();
        System.out.println("[THREAD POOL] Got "+feedAggregate.getAggregatedList().size()+" elements from "+feeds.size()+" feeds in "+(end-start)/1000+ " seconds");
    }

    @Ignore
    @Test
    public void directMappingAggregateSpeedTest(){
        Runnable r = () ->{
            FeedAggregate feedAggregate = simpleFeedAggregator.aggregateUsingDirectMapping(feeds);
            System.out.printf("[DIRECT MAPPING] Got "+feedAggregate.getAggregatedList().size()+" elements from "+feeds.size()+" feeds in ");
        };

        Thread t = new Thread(r);
        t.start();
        boolean state = false;

        try {
            long start = System.currentTimeMillis();
            t.join(120000);
            long end = System.currentTimeMillis();
            state = true;
            System.out.printf(" %d %s", (end-start)/1000, "secs");
        } catch (InterruptedException e) {
            Assert.assertFalse(state);
        }
    }

    @Test
	public void serialSpeedTest() throws MalformedURLException {
		long start=System.currentTimeMillis();
		FeedAggregate feedAggregate = new FeedAggregate<>();
		for(String feed:feeds) {
			SyndFeedParser feedParser = new SyndFeedParser(feed, feedAggregate, false);
			feedParser.run();
		}
		long end = System.currentTimeMillis();
		System.out.println("[SINGLE THREAD EXECUTION] Got "+feedAggregate.getAggregatedList().size()+" elements from "+feeds.size()+" feeds in "+(end-start)/1000+ " seconds");
	}
}
