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
package org.komusubi.feeder.web;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import org.komusubi.feeder.aggregator.scraper.HtmlScraper;
import org.komusubi.feeder.aggregator.scraper.WeatherAnnouncementScraper;
import org.komusubi.feeder.aggregator.scraper.WeatherContentScraper;
import org.komusubi.feeder.aggregator.scraper.WeatherTitleScraper;
import org.komusubi.feeder.aggregator.site.WeatherTopicSite;
import org.komusubi.feeder.aggregator.topic.WeatherTopic;
import org.komusubi.feeder.model.Message;
import org.komusubi.feeder.model.Site;
import org.komusubi.feeder.sns.GateKeeper;
import org.komusubi.feeder.sns.SocialNetwork;
import org.komusubi.feeder.sns.Speaker;
import org.komusubi.feeder.sns.twitter.TweetMessage;
import org.komusubi.feeder.sns.twitter.TweetMessage.Fragment;
import org.komusubi.feeder.sns.twitter.TweetMessage.TimestampFragment;
import org.komusubi.feeder.sns.twitter.Twitter4j;
import org.komusubi.feeder.sns.twitter.strategy.SleepStrategy;
import org.komusubi.feeder.spi.PageCache;
import org.komusubi.feeder.storage.cache.PartialMatchPageCache;
import org.komusubi.feeder.web.scheduler.QuartzModule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * bootstrap.
 * @author jun.ozeki
 */
public class Bootstrap extends GuiceServletContextListener {

    /**
     * @see com.google.inject.servlet.GuiceServletContextListener#getInjector()
     */
    @Override
    protected Injector getInjector() {
        return buildInjector();
    }

    /**
     * build injector this module.
     * @return
     */
    public Injector buildInjector() {
        return Guice.createInjector(new WebModule(),
                                    new Jal5971Module(),
                                    new QuartzModule());
    }

    /**
     * servlet module.
     * @author jun.ozeki
     */
    public static class WebModule extends JerseyServletModule {
        
        /**
         * @see com.google.inject.servlet.ServletModule#configureServlets()
         */
        @Override
        protected void configureServlets() {
            Map<String, String> param = new HashMap<String, String>();
            param.put(PackagesResourceConfig.PROPERTY_PACKAGES, Jal5971Resource.class.getPackage().getName());
            serve("/*").with(GuiceContainer.class, param);
            bind(Jal5971Resource.class);
        }
    }
    
    /**
     * jal5971 module.
     * @author jun.ozeki
     */
    public static class Jal5971Module extends AbstractModule {

        @Override
        protected void configure() {
            bind(SocialNetwork.class).to(Twitter4j.class).in(Singleton.class);
            bind(Speaker.class);
            bind(Long.class).annotatedWith(Names.named("cache duration")).toInstance(60 * 10 * 1000L);
            bind(Long.class).annotatedWith(Names.named("tweet sleep interval")).toInstance(1L);
            bind(String.class).annotatedWith(Names.named("tweet store file"))
                .toInstance(System.getProperty("java.io.tmpdir") + "/feeder-store.txt");
            bind(PageCache.class).to(PartialMatchPageCache.class);
            bind(GateKeeper.class).to(SleepStrategy.class);
//            bind(new TypeLiteral<Resolver<Date>>(){}).to(DateResolver.class);
            bind(WeatherContentScraper.class);
            bind(WeatherAnnouncementScraper.class);
            bind(String.class).annotatedWith(Names.named("fragment format")).toInstance("HHmm");
            bind(WeatherTitleScraper.class);
            bind(Fragment.class).to(TimestampFragment.class);
            bind(Message.class).to(TweetMessage.class);
            bind(HtmlScraper.class);
            bind(Site.class).to(WeatherTopicSite.class);
            bind(WeatherTopic.class);
        }
    }
}
