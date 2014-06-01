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

import java.io.Serializable;
import java.util.Arrays;

import org.komusubi.feeder.bind.BitlyUrlShortening;
import org.komusubi.feeder.model.Site;
import org.komusubi.feeder.model.Tag;
import org.komusubi.feeder.model.Tags;
import org.komusubi.feeder.model.Url;
import org.komusubi.feeder.spi.UrlShortening;
import org.komusubi.feeder.utils.ResourceBundleMessage;
import org.komusubi.feeder.utils.Types.ScrapeType;

/**
 * @author jun.ozeki
 */
public class RssSite implements Site, Serializable {
    private static final long serialVersionUID = 1L;
    private static final ResourceBundleMessage RESOURCE = new ResourceBundleMessage(RssSite.class);
    private Url url;
    private Tags tags;
    private UrlShortening urlShorten;
                    
    /**
     * create new instance.
     * @param resourceKey
     */
    public RssSite(String resourceKey) {
        this(resourceKey, new BitlyUrlShortening());
    }

    /**
     * create new instance.
     * @param resourceKey
     * @param scrapeType
     */
    public RssSite(String resourceKey, ScrapeType scrapeType) {
        this(resourceKey, new BitlyUrlShortening(scrapeType));
    }

    /**
     * create new instance.
     * @param resourceKey
     * @param urlShorten
     */
    public RssSite(String resourceKey, UrlShortening urlShorten) {
        this(new Url(RESOURCE.getString(resourceKey), urlShorten), urlShorten);
    }

    /**
     * create new instance.
     * @param url
     * @param urlShorten
     * @param tags
     */
    public RssSite(Url url, UrlShortening urlShorten, Tag... tags) {
        this.url = url;
        this.urlShorten = urlShorten;
        this.tags = new Tags();
        this.tags.addAll(Arrays.asList(tags));
    }

    /**
     * @see org.komusubi.feeder.model.Site#url()
     */
    @Override
    public Url url() {
        return url;
    }

    /**
     * @see org.komusubi.feeder.model.Site#tags()
     */
    @Override
    public Tags tags() {
        return tags;
    }

    public UrlShortening urlShortening() {
        return urlShorten;
    }
}
