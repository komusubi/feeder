/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.komusubi.feeder.web;

import java.io.File;
import java.io.PrintStream;

import org.komusubi.feeder.aggregator.scraper.HtmlScraper;
import org.komusubi.feeder.aggregator.scraper.WeatherAnnouncementScraper;
import org.komusubi.feeder.aggregator.scraper.WeatherContentScraper;
import org.komusubi.feeder.aggregator.scraper.WeatherTitleScraper;
import org.komusubi.feeder.aggregator.site.RssSite;
import org.komusubi.feeder.aggregator.site.WeatherTopicSite;
import org.komusubi.feeder.aggregator.topic.FeedTopic;
import org.komusubi.feeder.aggregator.topic.WeatherTopic;
import org.komusubi.feeder.bind.BitlyUrlShortening;
import org.komusubi.feeder.model.Topic;
import org.komusubi.feeder.model.Topics;
import org.komusubi.feeder.sns.Speaker;
import org.komusubi.feeder.sns.spi.PageCache;
import org.komusubi.feeder.sns.twitter.HashTag;
import org.komusubi.feeder.sns.twitter.TweetMessage.TimestampFragment;
import org.komusubi.feeder.sns.twitter.Twitter4j;
import org.komusubi.feeder.sns.twitter.provider.TweetMessagesProvider;
import org.komusubi.feeder.sns.twitter.strategy.SleepStrategy;
import org.komusubi.feeder.sns.twitter.strategy.SleepStrategy.FilePageCache;
import org.komusubi.feeder.sns.twitter.strategy.SleepStrategy.PartialMatchPageCache;
import org.komusubi.feeder.spi.UrlShortening;
import org.komusubi.feeder.utils.Types.AggregateType;
import org.komusubi.feeder.utils.Types.ScrapeType;

/**
 * @author jun.ozeki
 */
public final class StandAlone {
//    private static final Logger logger = LoggerFactory.getLogger(StandAlone.class);

    // hide default constructor.
    private StandAlone() {
        
    }

    protected ScrapeType parseScrapeType(String[] args) {
        ScrapeType type = null;
        for (int i = 0; i < args.length; i++) {
            try {
                type = ScrapeType.valueOf(args[i].toUpperCase());
            } catch (IllegalArgumentException e) {
                // ignore
            }
        }
        return type;
    }

    private AggregateType parseAggregateType(String[] args) {
        AggregateType type = null;
        for (int i = 0; i < args.length; i++) {
            try {
                type = AggregateType.valueOf(args[i].toUpperCase());
            } catch (IllegalArgumentException e) {
                // ignore
            }
        }
        return type;
    }

    private static void usage(PrintStream stream) {
        stream.printf("arguments must be \"scraper\" or \"feeder\" and \"jal5971\" or \"jal5931\"");
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            usage(System.err);
            return;
        } 
        StandAlone standAlone = new StandAlone();
        ScrapeType scrapeType = standAlone.parseScrapeType(args); 
        AggregateType aggregateType = standAlone.parseAggregateType(args); 
        if (scrapeType == null || aggregateType == null) {
            usage(System.err);
            return;
        }

        Topics<? extends Topic> topics;
        PageCache pageCache;
        String tmpDirname = System.getProperty("java.io.tmpdir");
        String suffix = scrapeType.name().toLowerCase();
        if (AggregateType.SCRAPER.equals(aggregateType)) {
            topics = standAlone.aggregateScraper(scrapeType);
            File storeFile = new File(tmpDirname + "/scraper-" + suffix + ".txt");
            pageCache = new PartialMatchPageCache(storeFile);
        } else if(AggregateType.FEEDER.equals(aggregateType)) {
            topics = standAlone.aggregateFeeder(scrapeType);
            File storeFile = new File(tmpDirname + "/feeder-" + suffix + ".txt");
            pageCache = new FilePageCache(storeFile);
        } else {
            throw new IllegalArgumentException("arguments must be \"scraper\" or \"feeder\"");
        }
        // topic 
        Speaker speaker = new Speaker(new Twitter4j(scrapeType), new SleepStrategy(pageCache));
        speaker.talk(topics);
    }

    private Topics<? extends Topic> aggregateScraper(ScrapeType scrapeType) {

        WeatherTopic weather = null;
        UrlShortening urlShorten = new BitlyUrlShortening(scrapeType);
        HtmlScraper scraper = new HtmlScraper(true, urlShorten);
        if (ScrapeType.JAL5971.equals(scrapeType)) {
            WeatherTopicSite domestic = new WeatherTopicSite("domestic", urlShorten);
            weather = new WeatherTopic(new WeatherContentScraper(domestic, scraper),
                                                     new WeatherTitleScraper(domestic, scraper),
                                                     new WeatherAnnouncementScraper(domestic, scraper),
                                                     new TweetMessagesProvider(new TimestampFragment("HHmm")));
        } else if (ScrapeType.JAL5931.equals(scrapeType)) {
            WeatherTopicSite international = new WeatherTopicSite("international", urlShorten);
            weather = new WeatherTopic(new WeatherContentScraper(international, scraper),
                                                        new WeatherTitleScraper(international, scraper),
                                                        new WeatherAnnouncementScraper(international, scraper),
                                                        new TweetMessagesProvider(new TimestampFragment("HHmm")));
        } else {
            throw new IllegalArgumentException("unknown ScrapType");
        }

        Topics<WeatherTopic> topics = new Topics<>();
        weather.addTag(new HashTag("jal"));
        topics.add(weather);
        
        return topics;
    }
    
    private Topics<? extends Topic> aggregateFeeder(ScrapeType scrapeType) {
        
        String resourceKey = null;
        if (ScrapeType.JAL5971.equals(scrapeType)) {
            resourceKey = "jal.domestic";
        } else if (ScrapeType.JAL5931.equals(scrapeType)) {
            resourceKey = "jal.international";
        } else {
            throw new IllegalArgumentException("unknown ScrapeType");
        }

        BitlyUrlShortening urlShorten = new BitlyUrlShortening(scrapeType);
        RssSite site = new RssSite(resourceKey, urlShorten);

        HashTag jal = new HashTag("jal");
        FeedTopic jalInfo = new FeedTopic(new RssSite("jal.info", urlShorten), new TweetMessagesProvider());
        jalInfo.addTag(jal);

        FeedTopic feedTopic = new FeedTopic(site, new TweetMessagesProvider());
        feedTopic.addTag(jal);

        Topics<FeedTopic> topics = new Topics<>();
        topics.add(jalInfo);
        topics.add(feedTopic);

        return topics;
    }

}
