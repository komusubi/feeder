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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.komusubi.feeder.aggregator.AggregatorException;
import org.komusubi.feeder.aggregator.rss.FeedReader.EntryScript;
import org.komusubi.feeder.aggregator.site.RssSite;
import org.komusubi.feeder.model.AbstractScript;
import org.komusubi.feeder.model.Message.Script;
import org.komusubi.feeder.model.Url;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.FeedException;

/**
 * @author jun.ozeki
 */
public class FeedReader implements Iterable<EntryScript> {

    /**
     * 
     * @author jun.ozeki
     */
    public static class EntryScript extends AbstractScript {

        private static final long serialVersionUID = 1L;
        private StringBuilder builder;

        /**
         * @param entry
         */
        public EntryScript(SyndEntry entry) {
            this.builder = line(entry); // initialize configure "line"
        }

        private StringBuilder line(SyndEntry entry) {
            builder = new StringBuilder();
            builder.append(entry.getTitle());
            if (StringUtils.isNotBlank(entry.getDescription().getValue())) {
                if (!builder.toString().endsWith("\n"))
                    builder.append("\n");
                builder.append(entry.getDescription().getValue());
            }
            Url url = new Url(entry.getLink()).shorten();
            if (!builder.toString().endsWith("\n"))
                builder.append("\n");
            builder.append(url.toExternalForm()); 

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

    /**
     * create new instance.
     */
    public FeedReader(RssSite site) {
        this.site = site;
        this.feedInfoCache = HashMapFeedInfoCache.getInstance();
    }

    public List<EntryScript> retrieve() {
        return retrieve(0L);
    }

    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<EntryScript> retrieve(long lastModified) {

        List<EntryScript> scripts = new ArrayList<>();
        HttpURLFeedFetcher fetcher = new HttpURLFeedFetcher(this.feedInfoCache);
        try {
            SyndFeed feed = fetcher.retrieveFeed(site.url().toURL());
            if (feed == null)
                return scripts;
            
            for (Iterator<SyndEntry> it = (Iterator<SyndEntry>) feed.getEntries().iterator(); it.hasNext(); ) {
                scripts.add(new EntryScript(it.next()));
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
        return retrieve(0L).iterator();
    }
    
}
