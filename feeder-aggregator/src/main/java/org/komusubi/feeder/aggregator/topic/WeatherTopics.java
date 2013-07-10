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
package org.komusubi.feeder.aggregator.topic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.komusubi.feeder.aggregator.scraper.AbstractWeatherScraper;
import org.komusubi.feeder.aggregator.scraper.HtmlScraper;
import org.komusubi.feeder.aggregator.scraper.WeatherAnnouncementScraper;
import org.komusubi.feeder.aggregator.scraper.WeatherTitleScraper;
import org.komusubi.feeder.aggregator.scraper.WeatherTopicScraper;
import org.komusubi.feeder.aggregator.site.WeatherTopicSite;
import org.komusubi.feeder.model.Message;
import org.komusubi.feeder.model.Message.Script;
import org.komusubi.feeder.model.Tag;
import org.komusubi.feeder.model.Tags;
import org.komusubi.feeder.model.Topic;
import org.komusubi.feeder.model.Topics;

/**
 * @author jun.ozeki
 */
public class WeatherTopics extends Topics<Topic> {

    private static final long serialVersionUID = 1L;

    /**
     * weather topic announcement.
     * @author jun.ozeki
     */
    public static class Announcement implements Script, Serializable {

        private static final long serialVersionUID = 1L;
        private String information;

        /**
         * create new instance.
         * @param information
         */
        public Announcement(String information) {
            this.information = information;
        }

        /**
         * @see org.komusubi.feeder.model.Message.Script#line()
         */
        @Override
        public String line() {
            return information;
        }
        
        @Override
        public int codePointCount() {
            if (information == null)
                return 0;
            return information.codePointCount(0, information.length());
        }

        @Override
        public String codePointSubstring(int begin, int end) {
            // FIXME code point substring
            if (information == null)
                return null;
            return information.substring(begin, end);
        }

        @Override
        public String codePointSubstring(int begin) {
            // FIXME code point substring
            if (information == null)
                return null;
            return codePointSubstring(begin, information.length());
        }
    }

    /**
     * weather topic title.
     * @author jun.ozeki
     */
    public static class Title implements Script, Serializable {

        private static final long serialVersionUID = 1L;
        private String title;

        /**
         * create new instance.
         */
        public Title(String title) {
            this.title = title;
        }

       /**
         * @see org.komusubi.feeder.model.Message.Script#line()
         */
        @Override
        public String line() {
            return title;
        }

        /**
         * @see org.komusubi.feeder.model.Message.Script#codePointLength()
         */
        @Override
        public int codePointCount() {
            if (title == null)
                return 0;
            return title.codePointCount(0, title.length());
        }

        @Override
        public String codePointSubstring(int begin, int end) {
            if (title == null)
                return null;
            return title.substring(begin, end);
        }

        @Override
        public String codePointSubstring(int begin) {
            if (title == null)
                return null;
            return codePointSubstring(begin, title.length());
        }
       
    }
    
    private WeatherAnnouncementScraper announceScraper;
    private WeatherTitleScraper titleScraper;
    private WeatherTopicScraper topicScraper;
    private Provider<Message> messageProvider;
    
    /**
     * create new instance.
     */
    public WeatherTopics(Provider<Message> messageProvider) {
        this(new WeatherTopicSite(), new HtmlScraper(), messageProvider);
    }

    /**
     * create new instance.
     * @param site
     */
    public WeatherTopics(WeatherTopicSite site, Provider<Message> messageProvider) {
        this(site, new HtmlScraper(), messageProvider);
    }
    
    /**
     * create new instance.
     * @param scraper
     */
    public WeatherTopics(HtmlScraper scraper, Provider<Message> messageProvider) {
        this(new WeatherTopicSite(), scraper, messageProvider);
    }
    
    /**
     * crewate new instance.
     * @param site
     * @param scraper
     */
    public WeatherTopics(WeatherTopicSite site, HtmlScraper scraper, Provider<Message> messageProvider) {
        this(new WeatherTopicScraper(site, scraper), 
             new WeatherTitleScraper(site, scraper), 
             new WeatherAnnouncementScraper(site, scraper),
             messageProvider);
    }
    
    /**
     * create new instance.
     * @param topicScraper
     * @param titleScraper
     * @param announceScraper
     */
    @Inject
    public WeatherTopics(WeatherTopicScraper topicScraper, WeatherTitleScraper titleScraper, 
                    WeatherAnnouncementScraper announceScraper, Provider<Message> messageProvider) {
        if (topicScraper == null || titleScraper == null || announceScraper == null || messageProvider == null)
            throw new IllegalArgumentException("argments MUST not be null: ");
        this.topicScraper = topicScraper;
        this.titleScraper = titleScraper;
        this.announceScraper = announceScraper;
        this.messageProvider = messageProvider;
    }
    
    /**
     * @see org.komusubi.feeder.model.Message#text()
     */
    @Override
    public Message message() {
        Message message = messageProvider.get();
        for (Title title: titleScraper) {
            message.append(title)
                    .append("\n");
        }
        message.addAll(titleScraper.scrape());
        for (Topic topic: topicScraper) {
            message.addAll(topic.message());
        }
        message.addAll(announceScraper.scrape());
        
        // check duprecate scraper
        List<AbstractWeatherScraper> scrapers = new ArrayList<AbstractWeatherScraper>();
        for (AbstractWeatherScraper scraper: new AbstractWeatherScraper[]{announceScraper, titleScraper, topicScraper}) {
            if (scrapers.contains(scraper)) {
                continue;
            } else {
                // append tag label
                Tags tags = scraper.site().tags();
                for (Iterator<Tag> it = tags.iterator(); it.hasNext(); ) { 
                    message.append(it.next().label());
                    if (it.hasNext())
                        message.append(" ");
                }
                scrapers.add(scraper);
            }
        }
        return message;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("WeatherTopics [announceScraper=").append(announceScraper).append(", titleScraper=")
                        .append(titleScraper).append(", topicScraper=").append(topicScraper).append("]");
        return builder.toString();
    }
}
