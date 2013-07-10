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

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableHeader;
import org.htmlparser.util.NodeList;
import org.komusubi.feeder.aggregator.site.WeatherTopicSite;
import org.komusubi.feeder.aggregator.topic.WeatherTopic;
import org.komusubi.feeder.aggregator.topic.WeatherTopic.WeatherStatus;
import org.komusubi.feeder.model.Region;

/**
 * @author jun.ozeki
 */
public class WeatherTopicScraper extends AbstractWeatherScraper implements Iterable<WeatherTopic> {

    private static final String ATTR_VALUE = "weather_info_txtBox";
    private static final String ATTR_NAME = "class";
    
    /**
     * create new instance.
     */
    public WeatherTopicScraper() {
        
    }

    /**
     * create new instance.
     * @param site
     */
    public WeatherTopicScraper(WeatherTopicSite site) {
        this(site, new HtmlScraper());
    }

    /**
     * create new instance.
     * @param scraper
     */
    public WeatherTopicScraper(HtmlScraper scraper) {
        this(new WeatherTopicSite(), scraper);
    }

    /**
     * create new instance.
     * @param site
     * @param scraper
     */
    @Inject
    public WeatherTopicScraper(WeatherTopicSite site, HtmlScraper scraper) {
        super(site, scraper);
    }

    /**
     * scrape for Region.
     * @return
     */
    public List<Region> scrapeRegion() {
        NodeList nodes = scrape(Region.class);
        List<Region> regions = new ArrayList<Region>();
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.elementAt(i);
            regions.add(new Region(node.toPlainTextString()));
        }
        return regions;
    }

    /**
     * scrape for WeatherStatus.
     * @return
     */
    public List<WeatherStatus> scrapeWeatherStatus() {
        NodeList nodes = scrape(WeatherStatus.class);
        List<WeatherStatus> statuses = new ArrayList<WeatherStatus>();
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.elementAt(i);
            String[] values = node.toPlainTextString().split("\n");
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < values.length; j++) {
                builder.append(values[j]);
                if (values.length > j + 1)
                    builder.append(" ");
            }
            statuses.add(new WeatherStatus(builder.toString()));
        }
        return statuses;
    }

    /**
     * scrape html.
     * @return
     */
    public NodeList scrape(Class<?> clazz) {
        if (!clazz.isAssignableFrom(Region.class) && !clazz.isAssignableFrom(WeatherStatus.class))
            throw new IllegalArgumentException("argument MUST be Region.class or WeatherStatus.class");

        Class<?> filterTagClass = null;
        if (clazz.isAssignableFrom(Region.class)) {
            filterTagClass = TableHeader.class;
        } else if (clazz.isAssignableFrom(WeatherStatus.class)) {
            filterTagClass = TableColumn.class;
        }
        return scraper().scrapeMatchNodes(site().url(), filter(), filterTagClass);
    }

  
    /**
     * WeatherTopics filter for html scraper. 
     * @return
     */
    protected NodeFilter filter() {
        return new AndFilter(
                        new NodeClassFilter(Div.class),
                        new HasAttributeFilter(ATTR_NAME, ATTR_VALUE));
    }

    /**
     * topic count size.
     * @return
     */
    public int size() {
        int size = scrapeRegion().size();
        if (size != scrapeWeatherStatus().size())
            throw new IllegalStateException("not match size: Region:" + scrapeRegion().size()
                            + ", WeatherStatus:" + scrapeWeatherStatus().size());
        return size;
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<WeatherTopic> iterator() {
        List<WeatherTopic> list = new ArrayList<WeatherTopic>();
        for (int i = 0; i < size(); i++) {
            Region region = scrapeRegion().get(i);
            WeatherStatus status = scrapeWeatherStatus().get(i);
            list.add(new WeatherTopic(region, status));
        }
        return list.iterator();
    }

}
