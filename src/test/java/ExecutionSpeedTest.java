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
import java.util.concurrent.atomic.AtomicBoolean;

@FixMethodOrder(MethodSorters.JVM)
public class ExecutionSpeedTest {

    private List<String> feeds;
    private SimpleFeedAggregator simpleFeedAggregator;
    private boolean started = false;

    @Before
    public void init() {
        simpleFeedAggregator = new SimpleFeedAggregator();
        feeds = new ArrayList<>();
        try {
            File file = new ClassPathResource("feeds.txt").getFile();
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
        singleItem(350);
    }

    @Test
    public void threadPoolAggregatorSpeedTest(){
        poolTest(350);
    }


    @Test
    public void directMappingAggregateSpeedTest(){
            directMappingTest(350);
    }

    @Ignore
    @Test
	public void serialSpeedTest() throws MalformedURLException {
		long start=System.currentTimeMillis();
		FeedAggregate feedAggregate = new FeedAggregate<>();
		for(String feed:feeds.subList(0, 100)) {
			SyndFeedParser feedParser = new SyndFeedParser(feed, feedAggregate, false);
			feedParser.run();
		}
		long end = System.currentTimeMillis();
		System.out.println("[SINGLE THREAD EXECUTION] Got "+feedAggregate.getAggregatedList().size()+" elements from "+feeds.size()+" feeds in "+(end-start)/1000+ " seconds");
	}


    @Test
    public void blockingQueueAggregatorWithBatchAdditionSpeedTest2() throws InterruptedException {
            batch(350);
    }

    private void batch(int i) {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);
        BlockingQueue<FeedMessage> queue = new LinkedBlockingQueue<>();
        FeedScanner scanner = new FeedScanner(queue, true);
        for(String url: feeds.subList(0, i)){
            scanner.addSource(url);
        }
        BlockingQueueFeedAggregator aggregator = new BlockingQueueFeedAggregator(queue);
        exec.scheduleWithFixedDelay(aggregator, 0, 1, TimeUnit.SECONDS);
        exec.scheduleWithFixedDelay(scanner, 0, 1, TimeUnit.SECONDS);
        Long start = System.currentTimeMillis();
        while(!aggregator.isDone());
        Long end = System.currentTimeMillis();
        System.out.println("[BLOCKING QUEUE WITH BATCH] Got "+aggregator.getByPubDate().size()+" elements from "+i+" feeds in "+(end - start)/1000+" seconds");
    }

    private void directMappingTest(int i) {
        AtomicBoolean state = new AtomicBoolean(false);
        Runnable r = () -> {
            FeedAggregate feedAggregate = simpleFeedAggregator.aggregateUsingDirectMapping(feeds.subList(0, i));
            state.set(true);
            System.out.printf("[DIRECT MAPPING] Got "+feedAggregate.getAggregatedList().size()+" elements from "+i+" feeds in ");
        };

        Thread t = new Thread(r);
        t.start();

        try {
            long start = System.currentTimeMillis();
            t.join(120000);
            long end = System.currentTimeMillis();
            if(state.get()){
                System.out.println((end-start)/1000+" secs");
            }else{
                System.out.println("Not done in 120 seconds");
            }
        } catch (InterruptedException e) {
        }
    }

    private void singleItem(int i) {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);
        BlockingQueue<FeedMessage> queue = new LinkedBlockingQueue<>();
        FeedScanner scanner = new FeedScanner(queue, false);
        for(String url: feeds.subList(0, i)){
            scanner.addSource(url);
        }
        BlockingQueueFeedAggregator aggregator = new BlockingQueueFeedAggregator(queue);
        exec.scheduleWithFixedDelay(aggregator, 0, 1, TimeUnit.SECONDS);
        exec.scheduleAtFixedRate(scanner, 0, 3000, TimeUnit.SECONDS);
        Long start = System.currentTimeMillis();
        while(!aggregator.isDone());
        Long end = System.currentTimeMillis();
        System.out.println("[BLOCKING QUEUE] Got "+aggregator.getByPubDate().size()+" elements from "+i+" feeds in "+(end - start)/1000+" seconds");
    }

    private void poolTest(int i) {
        long start = System.currentTimeMillis();
        FeedAggregate feedAggregate
                = simpleFeedAggregator.aggregateUsingThreadPools(feeds.subList(0, i),10);
        long end = System.currentTimeMillis();
        System.out.println("[THREAD POOL] Got "+feedAggregate.getAggregatedList().size()+" elements from "+i+" feeds in "+(end-start)/1000+ " seconds");
    }

}
