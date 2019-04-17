package com.feedreader.rssaggregator;

import com.feedreader.rssaggregator.util.Constants;
import com.feedreader.rssaggregator.util.OPMLAggregator;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

public class OPMLParserTest {

    @Test
    public void opmltest() throws IOException {

        List<String> opmlList = Arrays.asList(Constants.URL1,Constants.URL2,Constants.URL3,Constants.URL4,Constants.URL5);

        OPMLAggregator op = new OPMLAggregator();
        ConcurrentSkipListSet<String> feedlist = op.aggregateOPML(opmlList);
        System.out.println(feedlist.size());
        Assert.assertTrue(!feedlist.isEmpty());
    }
}
