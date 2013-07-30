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
package org.komusubi.feeder.sns.twitter.strategy;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import org.komusubi.common.util.Resolver;
import org.komusubi.feeder.model.Message;
import org.komusubi.feeder.model.Page;
import org.komusubi.feeder.model.Topic;
import org.komusubi.feeder.sns.GateKeeper;
import org.komusubi.feeder.sns.twitter.Twitter4j;
import org.komusubi.feeder.utils.ResolverUtils.DateResolver;

/**
 * @author jun.ozeki
 */
public class SleepStrategy implements GateKeeper {

    /**
     * 
     * @author jun.ozeki
     */
    public static class PageCache {
        private static final long CACHE_DURATION = 60 * 60 * 1000;
        private Date date;
        private Page page;
        private Resolver<Date> resolver;
        private long cacheDuration;
        private Twitter4j twitter4j;

        /**
         * create new instance.
         */
        public PageCache() {
            this(new Twitter4j(), new DateResolver(), CACHE_DURATION);
        }

        /**
         * create new instance.
         * @param twitter4j
         */
        public PageCache(Twitter4j twitter4j) {
            this(twitter4j, new DateResolver(), CACHE_DURATION);
        }

        /**
         * create new instance.
         * @param date
         */
        @Inject
        public PageCache(Twitter4j twitter4j, Resolver<Date> resolver, @Named("cache duration") long duration) {
            this.twitter4j = twitter4j;
            this.resolver = resolver;
            this.cacheDuration = duration;
            init();
        }

        public Page page() {
            return page;
        }

        public Date date() {
            return date;
        }

        private void init() {
            this.date = resolver.resolve();
            page = twitter4j.history().next();
        }

        public boolean outdated() {
            Date current = resolver.resolve();
            if (current.getTime() - date.getTime() > cacheDuration) {
                return true;
            }
            return false;
        }

        /**
         * @return
         */
        public void refresh() {
            if (!outdated())
                return;
            init();
        }

        public boolean exists(Message message) {
            for (Topic t: page.topics()) {
                if (t.message().equals(message))
                    return true;
            }
            return false;
        }
    }

    private long milliSecond;
    private PageCache cache;

    /**
     * create new instance.
     */
    public SleepStrategy() {
        this(1);
    }

    /**
     * create new instance.
     * @param sleepSecond
     */
    public SleepStrategy(long sleepSecond) {
        this(sleepSecond, new PageCache());
    }

    /**
     * create new instance.
     * @param sleepSecond
     * @param cache
     */
    @Inject
    public SleepStrategy(@Named("tweet sleep interval") long sleepSecond, PageCache cache) {
        this.milliSecond = sleepSecond * 1000;
        this.cache = cache;
    }

    // PageCache is NOT static class, because access to twitter4j instance.
//    private void init(PageCache cache) {
//        if (cache == null)
//            this.cache = new PageCache();
//        else
//            this.cache = cache;
//    }

    /**
     * @see org.komusubi.feeder.sns.GateKeeper#available()
     */
    @Override
    public boolean available(Message message) {
        boolean result = false;
        cache.refresh();
        if (!cache.exists(message))
            result = true;
        try {
            Thread.sleep(milliSecond);
        } catch (InterruptedException ignore) {
            result = true;
        }
        return result;
    }

}
