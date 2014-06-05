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

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import javax.inject.Provider;

import org.komusubi.feeder.aggregator.scraper.AbstractWeatherScraper;
import org.komusubi.feeder.aggregator.scraper.HtmlScraper;
import org.komusubi.feeder.aggregator.scraper.WeatherAnnouncementScraper;
import org.komusubi.feeder.aggregator.scraper.WeatherAnnouncementScraper.Announcement;
import org.komusubi.feeder.aggregator.scraper.WeatherContentScraper;
import org.komusubi.feeder.aggregator.scraper.WeatherContentScraper.Content;
import org.komusubi.feeder.aggregator.scraper.WeatherTitleScraper;
import org.komusubi.feeder.aggregator.scraper.WeatherTitleScraper.Title;
import org.komusubi.feeder.aggregator.site.WeatherTopicSite;
import org.komusubi.feeder.bind.FeederMessagesProvider;
import org.komusubi.feeder.model.Message;
import org.komusubi.feeder.model.Messages;
import org.komusubi.feeder.model.Tag;
import org.komusubi.feeder.model.Tags;
import org.komusubi.feeder.model.Topic;

/**
 * @author jun.ozeki
 */
public class WeatherTopic implements Topic {

    private static final long serialVersionUID = 1L;
    private Date created;
    private Messages<Message> messages;
    private WeatherAnnouncementScraper announceScraper;
    private WeatherTitleScraper titleScraper;
    private WeatherContentScraper contentScraper;
    private Tags tags;

    /**
     * create new instance.
     */
    public WeatherTopic() {
        this(new WeatherTopicSite(), new HtmlScraper(), new FeederMessagesProvider());
    }
    
    /**
     * create new instance.
     * @param site
     * @param provider
     */
    public WeatherTopic(WeatherTopicSite site, Provider<Messages<Message>> provider) {
        this(site, new HtmlScraper(), provider);
    }

    /**
     * create new instance.
     * @param scraper
     * @param provider
     */
    public WeatherTopic(HtmlScraper scraper, Provider<Messages<Message>> provider) {
        this(new WeatherTopicSite(), scraper, provider);
    }

    /**
     * create new instance.
     * @param site
     * @param scraper
     * @param provider
     */
    public WeatherTopic(WeatherTopicSite site, HtmlScraper scraper, Provider<Messages<Message>> provider) {
        this(new WeatherContentScraper(site, scraper), 
             new WeatherTitleScraper(site, scraper), 
             new WeatherAnnouncementScraper(site, scraper),
             provider);
    }

    /**
     * create new instance.
     * @param topicScraper
     * @param titleScraper
     * @param announceScraper
     * @param provider
     */
    public WeatherTopic(WeatherContentScraper topicScraper, 
                        WeatherTitleScraper titleScraper,
                        WeatherAnnouncementScraper announceScraper, Provider<Messages<Message>> provider) {
        this.contentScraper = topicScraper;
        this.titleScraper = titleScraper;
        this.announceScraper = announceScraper;
        this.created = new Date();
        this.messages = provider.get();
        this.tags = new Tags();
    }

    // exclude any scraper, does NOT need to compare.
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WeatherTopic other = (WeatherTopic) obj;
        if (created == null) {
            if (other.created != null)
                return false;
        } else if (!created.equals(other.created))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((created == null) ? 0 : created.hashCode());
        return result;
    }

    
    public WeatherTopic addTag(Tag... tagArray) {
        for (Tag t: tagArray)
            tags.add(t);
        return this;
    }

    /**
     * @see org.komusubi.feeder.model.Topic#message()
     */
    @Override
    @Deprecated
    public Message message() {
        return messages().get(0);
    }
  
    /**
     * @see org.komusubi.feeder.model.Topic#messages()
     */
    @Override
    public Messages<Message> messages() {
        Message message = messages.newInstance();

        message.setTopic(this);

        for (Announcement announcement: announceScraper.scrape()) {
            message.append(announcement)
                    .append("\n");
        }
        for (Title title: titleScraper) {
            message.append(title)
                    .append("\n");
        }
        for (Content content: contentScraper) {
            message.append(content)
                   .append("\n"); 
        }
        
        for (AbstractWeatherScraper scraper: Arrays.asList(announceScraper, titleScraper, contentScraper)) {
            Tags scraperTags = scraper.site().tags();
            for (Tag t: scraperTags) 
                tags.add(t);
        }
        for (Iterator<Tag> it = tags.iterator(); it.hasNext(); ) {
            Tag tag = it.next();
            message.append(tag.label());
            if (it.hasNext())
                message.append(" ");
        }
        messages.add(message);
        return messages;
    }

    /**
     * @see org.komusubi.feeder.model.Topic#createdAt()
     */
    @Override
    public Date createdAt() {
        return created;
    }

    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WeatherTopic [created=").append(created)
				.append(", messages=").append(messages)
				.append(", announceScraper=").append(announceScraper)
				.append(", titleScraper=").append(titleScraper)
				.append(", contentScraper=").append(contentScraper)
				.append(", tags=").append(tags).append("]");
		return builder.toString();
	}

}
