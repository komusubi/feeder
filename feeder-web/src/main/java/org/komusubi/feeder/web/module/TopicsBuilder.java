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
package org.komusubi.feeder.web.module;

import org.komusubi.feeder.aggregator.rss.FeedReader;
import org.komusubi.feeder.aggregator.scraper.HtmlScraper;
import org.komusubi.feeder.aggregator.scraper.WeatherAnnouncementScraper;
import org.komusubi.feeder.aggregator.scraper.WeatherContentScraper;
import org.komusubi.feeder.aggregator.scraper.WeatherTitleScraper;
import org.komusubi.feeder.aggregator.topic.FeedTopic;
import org.komusubi.feeder.aggregator.topic.WeatherTopic;
import org.komusubi.feeder.model.Topic;
import org.komusubi.feeder.model.Topics;
import org.komusubi.feeder.model.WebSite;
import org.komusubi.feeder.sns.twitter.HashTag;
import org.komusubi.feeder.sns.twitter.TweetMessage.TimestampFragment;
import org.komusubi.feeder.sns.twitter.provider.TweetMessagesProvider;
import org.komusubi.feeder.storage.jdbi.SiteDao;
import org.komusubi.feeder.utils.Types.AggregateType;
import org.komusubi.feeder.utils.Types.ScrapeType;
import org.skife.jdbi.v2.DBI;

/**
 * @author jun.ozeki
 */
public class TopicsBuilder {

    private DBI dbi;
    private ScrapeType scrapeType;
    private AggregateType aggregateType;

    public TopicsBuilder(DBI dbi, AggregateType aggregateType, ScrapeType scrapeType) {
        this.dbi = dbi;
        this.aggregateType = aggregateType;
        this.scrapeType = scrapeType;
    }

    protected DBI dbi() {
        return dbi;
    }

    protected AggregateType aggregateType() {
        return aggregateType;
    }

    protected ScrapeType scrapeType() {
        return scrapeType;
    }

    public Module build() {
        FeederModule module = new FeederModule(this);
        return module;
    }

    public Topics<? extends Topic> topics() {
        Topics<? extends Topic> topics;
        switch (aggregateType) {
        case FEEDER:
            topics = buildFeederTopics(aggregateType, scrapeType);
            break;
        case SCRAPER:
            topics = buildScrapeTopics(aggregateType, scrapeType);
            break;
        default:
            throw new IllegalStateException("unknown AggregateType");
        }
        return topics;
    }

    /**
     * @param scrapeType
     * @return
     */
    private Topics<? extends Topic> buildFeederTopics(AggregateType aggregateType, ScrapeType scrapeType) {
        Topics<? extends Topic> topics = null;
        switch (scrapeType) {
        case JAL5931:
        case JAL5971:
            topics = buildRssTopics(aggregateType, scrapeType);
            break;
        case JAL:
        case JMB:
        case KOMUSUBI:
            topics = new Topics<>();
            break;
        default:
            throw new IllegalStateException("unknown ScrapeType");
        }
        return topics;
    }

    private Topics<? extends Topic> buildRssTopics(AggregateType aggregateType, ScrapeType scrapeType) {
        SiteDao siteDao = dbi.onDemand(SiteDao.class);
        try {
            // FIXME UrlShortening factory need !!!
            HashTag jal = new HashTag("jal");
            HashTag rss = new HashTag("rss");
            WebSite channelSite = siteDao.findByFeedAndChannel(aggregateType, scrapeType);
            FeedTopic channelTopic = new FeedTopic(new FeedReader(channelSite, null),
                                                    new TweetMessagesProvider());
            channelTopic.addTag(jal, rss);
            WebSite infoSite = siteDao.findByFeedAndChannel(aggregateType, ScrapeType.JAL);
            FeedTopic infoTopic = new FeedTopic(new FeedReader(infoSite, null),
                                                new TweetMessagesProvider());
            infoTopic.addTag(jal, rss);

            Topics<FeedTopic> topics = new Topics<>();
            topics.add(channelTopic);
            topics.add(infoTopic);
            return topics;
        } finally {
            siteDao.close();
        }
    }

    private Topics<? extends Topic> buildScrapeTopics(AggregateType aggregateType, ScrapeType scrapeType) {
        Topics<? extends Topic> topics = null;
        switch (scrapeType) {
        case JAL5971:
        case JAL5931:
            topics = buildWeatherTopics(aggregateType, scrapeType);
            break;
        case JMB:
        case JAL:
        case KOMUSUBI:
            topics = new Topics<>();
            break;
        default:
            throw new IllegalStateException("unknown ScrapeType");
        }
        return topics;
    }

    private Topics<? extends Topic> buildWeatherTopics(AggregateType aggregateType, ScrapeType scrapeType) {
        SiteDao siteDao = dbi.onDemand(SiteDao.class);
        try {
            WebSite site = siteDao.findByFeedAndChannel(aggregateType, scrapeType);
            // FIXME UrlShortening factory need !!
            HtmlScraper scraper = new HtmlScraper(true, null);
            WeatherTopic topic = new WeatherTopic(new WeatherContentScraper(site, scraper),
                                                     new WeatherTitleScraper(site, scraper),
                                                     new WeatherAnnouncementScraper(site, scraper),
                                                     new TweetMessagesProvider(new TimestampFragment("HHmm")));
            topic.addTag(new HashTag("jal"));

            Topics<WeatherTopic> topics = new Topics<>();
            topics.add(topic);
            return topics;
        } finally {
            siteDao.close();
        }
    }

}
