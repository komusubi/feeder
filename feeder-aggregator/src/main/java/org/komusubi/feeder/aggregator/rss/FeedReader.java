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
package org.komusubi.feeder.aggregator.rss;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.komusubi.feeder.aggregator.AggregatorException;
import org.komusubi.feeder.aggregator.rss.FeedReader.EntryScript;
import org.komusubi.feeder.aggregator.site.RssSite;
import org.komusubi.feeder.bind.BitlyUrlShortening;
import org.komusubi.feeder.model.AbstractScript;
import org.komusubi.feeder.model.Message.Script;
import org.komusubi.feeder.model.Tags;
import org.komusubi.feeder.spi.UrlShortening;
import org.komusubi.feeder.utils.Types.ScrapeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.AbstractFeedFetcher;
import com.sun.syndication.fetcher.impl.DiskFeedInfoCache;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HttpClientFeedFetcher;
import com.sun.syndication.fetcher.impl.SyndFeedInfo;
import com.sun.syndication.io.FeedException;

/**
 * @author jun.ozeki
 */
public class FeedReader implements Iterable<EntryScript> {
    private static final Logger logger = LoggerFactory.getLogger(FeedReader.class);

    /**
     * 
     * @author jun.ozeki
     */
    public static class EntryScript extends AbstractScript {

        private static final long serialVersionUID = 1L;
        private StringBuilder builder;
        private UrlShortening urlShorten;

        /**
         * @param entry
         */
        public EntryScript(SyndEntry entry) {
            this(entry, new BitlyUrlShortening());
        }

        /**
         * create new instance.
         * @param entry
         * @param urlShorten
         */
        public EntryScript(SyndEntry entry, UrlShortening urlShorten) {
            this.urlShorten = urlShorten;
            this.builder = line(entry); // initialize configure "line"
        }
        
        /**
         * create new instance.
         * @param entry
         * @param scrapeType
         */
        public EntryScript(SyndEntry entry, ScrapeType scrapeType) {
            this(entry, new BitlyUrlShortening(scrapeType));
        }

        private StringBuilder line(SyndEntry entry) {
            builder = new StringBuilder();
            builder.append(entry.getTitle());
            if (StringUtils.isNotBlank(entry.getDescription().getValue())) {
                if (!builder.toString().endsWith("\n"))
                    builder.append("\n");
                builder.append(entry.getDescription().getValue());
            }
            if (StringUtils.isNotBlank(entry.getLink())) {
                if (!builder.toString().endsWith("\n"))
                    builder.append("\n");

                URL url = urlShorten.shorten(entry.getLink());
                builder.append(url.toExternalForm());
            }
            return builder;
        }

        /**
         * @see org.komusubi.feeder.model.Message.Script#line()
         */
        @Override
        public String line() {
            if (builder.length() > 0)
                return builder.toString();
            return "";
        }

        /**
         * @see org.komusubi.feeder.model.Message.Script#codePointCount()
         */
        @Override
        public int codePointCount() {
            String line = line();
            return line.codePointCount(0, line.length());
        }

        /**
         * @see org.komusubi.feeder.model.Message.Script#append(java.lang.String)
         */
        @Override
        public Script append(String str) {
            builder.append(str);
            return this;
        }


        /**
         * @see org.komusubi.feeder.model.Message.Script#codePointSubstring(int, int)
         */
        @Override
        public String codePointSubstring(int begin, int end) {
            throw new UnsupportedOperationException("not implemented");
        }

        /**
         * @see org.komusubi.feeder.model.Message.Script#codePointSubstring(int)
         */
        @Override
        public String codePointSubstring(int begin) {
            throw new UnsupportedOperationException("not implemented");
        }
        
        @Override
        public String toString() {
            StringBuilder builder2 = new StringBuilder();
            builder2.append("EntryScript [builder=").append(builder).append("]");
            return builder2.toString();
        }
    }

    private RssSite site;
    private FeedFetcherCache feedInfoCache;
    private static final String CACHEABLE_PROPERTY = "feeder.history";
    // cacheable default is true
    private boolean cacheable = System.getProperty(CACHEABLE_PROPERTY) == null ? true : Boolean.getBoolean(CACHEABLE_PROPERTY);
    private UrlShortening urlShorten;

    /**
     * create new instance.
     */
    public FeedReader(RssSite site) {
        this(site, site.urlShortening());
    }
    
    /**
     * carete new instance.
     * @param site
     * @param urlShorten
     */
    public FeedReader(RssSite site, UrlShortening urlShorten) {
        if (site == null)
            throw new IllegalArgumentException("site must NOT be null");
        if (urlShorten == null)
            throw new IllegalArgumentException("urlShorten must NOT be null");
        this.site = site;
        this.urlShorten = urlShorten;
        if (cacheable)
            this.feedInfoCache = new DiskFeedInfoCache(System.getProperty("java.io.tmpdir"));
    }

    public List<EntryScript> retrieve() {
        SyndFeedInfo feedInfo = null;
        if (cacheable)
            feedInfo = this.feedInfoCache.getFeedInfo(this.site.url().toURL());
        long lastModified = 0L;
        if (feedInfo != null && feedInfo.getSyndFeed().getEntries().size() > 0) {
            // get first entry feed publish date because it was wrong in lastModified date in http header.
            // TODO this implementation for specific url. fix near future.
            SyndEntry entry = (SyndEntry) feedInfo.getSyndFeed().getEntries().get(0);
            logger.debug("last modified url:{}, {}", site.url().toExternalForm(), entry.getPublishedDate());
            lastModified = entry.getPublishedDate().getTime();
        }
        return retrieve(lastModified);
    }

    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<EntryScript> retrieve(long lastModified) {

        List<EntryScript> scripts = new ArrayList<>();
        AbstractFeedFetcher fetcher = new HttpClientFeedFetcher(this.feedInfoCache);
        try {
            SyndFeed feed = fetcher.retrieveFeed(site.url().toURL());
            if (feed == null)
                return scripts;
            
            // target site does not work lastModified in HTTP header,
            // compare to each entry#getUpdateDate()
            for (Iterator<SyndEntry> it = (Iterator<SyndEntry>) feed.getEntries().iterator(); it.hasNext(); ) {
                SyndEntry e = it.next();
                if (lastModified < e.getPublishedDate().getTime())
                    scripts.add(new EntryScript(e, urlShorten));
                else
                    logger.info("read already entry: {}, {}", e.getPublishedDate(), e.getTitle());
            }
            // reverse order
            Collections.reverse(scripts);
        } catch (IllegalArgumentException | IOException | FeedException | FetcherException e) {
            throw new AggregatorException(e);
        }
        return scripts;
    }


    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<EntryScript> iterator() {
        return retrieve().iterator();
    }
    
    public Tags tags() {
        return this.site.tags();
    }
}
