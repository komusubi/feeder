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
package org.komusubi.feeder.aggregator.scraper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.htmlparser.NodeFilter;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.komusubi.feeder.aggregator.AggregatorException;
import org.komusubi.feeder.aggregator.site.WeatherTopicSite;
import org.komusubi.feeder.aggregator.topic.WeatherTopics.Announcement;

/**
 * @author jun.ozeki
 */
public class WeatherAnnouncementScraper extends AbstractWeatherScraper implements Iterable<Announcement> {

    private static final String ATTR_NAME = "class";
    private static final String ATTR_VALUE = "mgt10";

    /**
     * create new instance.
     * default constructor.
     */
    public WeatherAnnouncementScraper() {

    }

    /**
     * create new instance.
     * @param site
     */
    public WeatherAnnouncementScraper(WeatherTopicSite site) {
        this(site, new HtmlScraper());
    }
    
    /**
     * create new instance.
     * @param scraper
     */
    public WeatherAnnouncementScraper(HtmlScraper scraper) {
        this(new WeatherTopicSite(), scraper);
    }
    
    /**
     * @param site
     * @param scraper
     */
    @Inject
    public WeatherAnnouncementScraper(WeatherTopicSite site, HtmlScraper scraper) {
        super(site, scraper);
    }

    /**
     * scrape with filter.
     * @param filter
     * @return
     */
    public NodeList scrape(NodeFilter filter) {
        return scraper().scrape(site().url(), filter);
    }

    /**
     * scrape for Announcement.
     * @return
     */
    public List<Announcement> scrape() {
        NodeList nodes = scrape(filter());
        List<Announcement> announces = new ArrayList<Announcement>();
        try {
            for (NodeIterator it = nodes.elements(); it.hasMoreNodes();) {
                announces.add(new Announcement(it.nextNode().getText()));
            }
        } catch (ParserException e) {
            throw new AggregatorException(e);
        }
        return announces;
    }

    /**
     * Announcement filter for html scraper.
     * @return
     */
    protected NodeFilter filter() {
        return new AndFilter(
                        new NodeClassFilter(ParagraphTag.class),
                        new HasAttributeFilter(ATTR_NAME, ATTR_VALUE));
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Announcement> iterator() {
        return scrape().iterator();
    }
}
