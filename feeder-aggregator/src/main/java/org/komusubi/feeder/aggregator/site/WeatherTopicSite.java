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
package org.komusubi.feeder.aggregator.site;

import java.util.Arrays;

import org.komusubi.feeder.bind.BitlyUrlShortening;
import org.komusubi.feeder.model.Site;
import org.komusubi.feeder.model.Tag;
import org.komusubi.feeder.model.Tags;
import org.komusubi.feeder.model.Url;
import org.komusubi.feeder.utils.ResourceBundleMessage;
import org.komusubi.feeder.utils.Types.ScrapeType;

/**
 * @author jun.ozeki
 */
public class WeatherTopicSite implements Site {

    private static final ResourceBundleMessage BUNDLE_MESSAGE = new ResourceBundleMessage(WeatherTopicSite.class);
    private static final String URL_RESOURCE_KEY = "site.url";
    private Url siteUrl;
    private Tags tags;
    private ScrapeType scrapeType;

    /**
     * create new instance.
     */
    public WeatherTopicSite() {
        this(URL_RESOURCE_KEY, ScrapeType.KOMUSUBI, new Tag[0]);
    }

    /**
     * create new instance.
     * @param resourceKey
     * @param scrapeType
     * @param tags
     */
    public WeatherTopicSite(String resourceKey, ScrapeType scrapeType, Tag... tags) {
        this.siteUrl = new Url(BUNDLE_MESSAGE.getString(resourceKey), new BitlyUrlShortening(scrapeType));
        this.scrapeType = scrapeType;
        this.tags = new Tags();
        this.tags.addAll(Arrays.asList(tags));
    }

    /**
     * create new instance.
     * @param resourceKey
     * @param tags
     */
    public WeatherTopicSite(String resourceKey, Tag... tags) {
        this(resourceKey, ScrapeType.KOMUSUBI, tags);
    }

    /**
     * create new instance.
     * @param url
     */
    public WeatherTopicSite(Url url) {
        this(url, new Tag[0]);
    }

    /**
     * create new instance.
     * @param url
     * @param tags
     */
    public WeatherTopicSite(Url url, Tag... tags) {
        this.siteUrl = url;
        this.tags = new Tags();
        this.tags.addAll(Arrays.asList(tags));
    }
    
    /**
     * create new instance.
     * @param tags
     */
    public WeatherTopicSite(Tag... tags) {
       this(URL_RESOURCE_KEY, tags); 
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WeatherTopicSite other = (WeatherTopicSite) obj;
        if (siteUrl == null) {
            if (other.siteUrl != null)
                return false;
        } else if (!siteUrl.equals(other.siteUrl))
            return false;
        if (tags == null) {
            if (other.tags != null)
                return false;
        } else if (!tags.equals(other.tags))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((siteUrl == null) ? 0 : siteUrl.hashCode());
        result = prime * result + ((tags == null) ? 0 : tags.hashCode());
        return result;
    }

    /**
     * @see org.komusubi.feeder.model.Site#tag()
     */
    @Override
    public Tags tags() {
        return tags;
    }

    /**
     * @see org.komusubi.feeder.model.Site#url()
     */
    @Override
    public Url url() {
        return siteUrl;
    }

    public ScrapeType scrapeType() {
        return scrapeType;
    }
}
