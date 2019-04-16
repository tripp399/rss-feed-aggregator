package com.feedreader.rssaggregator;

import com.feedreader.rssaggregator.util.Constants;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.feedreader.rssaggregator.util.OPMLAggregator;

import java.util.*;

public class OPMLParserTest {

    @Ignore
    @Test
    public void opmltest() {

        List<String> opmlList = Arrays.asList(Constants.URL1, Constants.URL2);

        OPMLAggregator op = new OPMLAggregator();
        HashSet<String> li = op.aggregateOPML(opmlList);
        System.out.println(li.size());
        Assert.assertTrue(!li.isEmpty());
    }
}
