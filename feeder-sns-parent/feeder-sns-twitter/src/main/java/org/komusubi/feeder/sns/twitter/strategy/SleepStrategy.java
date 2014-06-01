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

import java.io.File;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.komusubi.common.util.Resolver;
import org.komusubi.feeder.model.Message;
import org.komusubi.feeder.model.Message.Script;
import org.komusubi.feeder.model.Page;
import org.komusubi.feeder.model.Topic;
import org.komusubi.feeder.sns.GateKeeper;
import org.komusubi.feeder.sns.twitter.Twitter4j;
import org.komusubi.feeder.spi.PageCache;
import org.komusubi.feeder.storage.cache.FilePageCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jun.ozeki
 */
public class SleepStrategy implements GateKeeper {

    /**
     * @author jun.ozeki
     */
    @Deprecated
    public static class TimelinePageCache implements PageCache {
        private static final Logger logger = LoggerFactory.getLogger(SleepStrategy.class);
        private static final long CACHE_DURATION = 60 * 60 * 1000;
        private static final Provider<Date> DATE_PROVIDER = new Provider<Date>() {
            @Override
            public Date get() {
                return new Date();
            }
        };
        private Date date;
        private Page page;
        private long cacheDuration;
        private Twitter4j twitter4j;
        private Provider<Date> provider;

        /**
         * create new instance.
         */
        public TimelinePageCache() {
            this(new Twitter4j(), DATE_PROVIDER, CACHE_DURATION);
        }

        /**
         * create new instance.
         * @param twitter4j
         */
        public TimelinePageCache(Twitter4j twitter4j) {
            this(twitter4j, DATE_PROVIDER, CACHE_DURATION);
        }

        /**
         * create new instance.
         * @param date
         */
        @Deprecated
        public TimelinePageCache(Twitter4j twitter4j, Resolver<Date> resolver, @Named("cache duration") long duration) {
            this.twitter4j = twitter4j;
            this.provider = DATE_PROVIDER;
            this.cacheDuration = duration;
            init();
        }

        @Inject
        public TimelinePageCache(Twitter4j twitter4j, Provider<Date> provider, @Named("cache duration") long duration) {
            this.twitter4j = twitter4j;
            this.provider = provider;
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
            this.date = provider.get();
            page = twitter4j.history().next();
        }

        public boolean outdated() {
            Date current = provider.get();
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
            for (Script script: message) {
                boolean found = false;
                for (Topic t: page.topics()) {
                    logger.info("script: {}", script.line());
                    logger.info("topic:  {}", t.message().text());
                    if (script.line().equals(t.message().text())) {
                        found = true;
                        break;
                    }
                }
                if (!found)
                    return false;
            }
            return true;
        }

        /**
         * @see org.komusubi.feeder.sns.twitter.strategy.SleepStrategy.PageCache#store(org.komusubi.feeder.model.Message)
         */
        @Override
        public void store(Message message) {
            // nothing to do
        }
    }

    private long milliSecond;
    private PageCache cache;
    private static final String STORAGE_PROPERTY = "message.storage";
    private boolean cacheable = System.getProperty(STORAGE_PROPERTY) == null ? true : Boolean.getBoolean(STORAGE_PROPERTY);

    /**
     * create new instance.
     */
    public SleepStrategy() {
        this(new FilePageCache(new File(System.getProperty("java.io.tmpdir") + SleepStrategy.class.getCanonicalName() + ".txt")));
    }

    /**
     * create new instance. 
     * @param pageCache
     */
    public SleepStrategy(PageCache pageCache) {
        this.cache = pageCache;
    }

    /**
     * create new instance.
     * @param sleepSecond
     */
    @Deprecated
    public SleepStrategy(long sleepSecond) {
        this(sleepSecond, new TimelinePageCache());
    }

    /**
     * create new instance.
     * @param sleepSecond
     * @param cache
     */
    @Inject
    @Deprecated
    public SleepStrategy(@Named("tweet sleep interval") long sleepSecond, PageCache cache) {
        this.milliSecond = sleepSecond * 1000;
        this.cache = cache;
    }

    /**
     * @see org.komusubi.feeder.sns.GateKeeper#available()
     */
    @Override
    public boolean available(Message message) {
        boolean result = false;
        cache.refresh();
        if (!cache.exists(message))
            result = true;
        return result;
    }

    /**
     * @see org.komusubi.feeder.sns.GateKeeper#store(org.komusubi.feeder.model.Message)
     */
    @Override
    public void store(Message message) {
        if (cacheable) 
            cache.store(message);
    }

}
