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

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Provider;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.komusubi.feeder.aggregator.scraper.WeatherAnnouncementScraper;
import org.komusubi.feeder.aggregator.scraper.WeatherTitleScraper;
import org.komusubi.feeder.aggregator.scraper.WeatherTopicScraper;
import org.komusubi.feeder.aggregator.site.WeatherTopicSite;
import org.komusubi.feeder.aggregator.topic.WeatherTopic.WeatherStatus;
import org.komusubi.feeder.aggregator.topic.WeatherTopics.Announcement;
import org.komusubi.feeder.aggregator.topic.WeatherTopics.Title;
import org.komusubi.feeder.model.FeederMessage;
import org.komusubi.feeder.model.Message;
import org.komusubi.feeder.model.Region;
import org.komusubi.feeder.model.Tag;
import org.komusubi.feeder.model.Tags;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author jun.ozeki
 */
@RunWith(Theories.class)
public class WeatherTopicsTest {

    @Mock private WeatherTitleScraper titleScraper;
    @Mock private WeatherAnnouncementScraper announceScraper;
    @Mock private WeatherTopicScraper topicScraper;

    @DataPoints
    public static List<?>[][] messageDataPoint() {
        ArrayList<Title> titles = new ArrayList<Title>();
        titles.add(new Title("タイトルです。"));
        titles.add(new Title("ご確認下さい。"));
        ArrayList<Announcement> announces = new ArrayList<Announcement>();
        announces.add(new Announcement("この情報は当日のものです。"));
        ArrayList<WeatherTopic> topicList = new ArrayList<WeatherTopic>();
        topicList.add(new WeatherTopic(new Region("関東"), new WeatherStatus("平常通り運航します。")));
        
        return new List<?>[][] {
                        {titles, announces, topicList}
        };
    }
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @SuppressWarnings("unchecked")
    @Theory
    @Ignore
    public void Titleはリターンコードを追加する(List<?>[] args) {
        // setup
        Tags tags = new Tags();
        tags.add(new Tag() {

            private static final long serialVersionUID = 1L;

            @Override
            public String label() {
                return "#weather";
            }
            
        });
        when(titleScraper.iterator()).thenReturn(((List<Title>) args[0]).iterator());
        when(titleScraper.site()).thenReturn(new WeatherTopicSite(tags));
        when(announceScraper.scrape()).thenReturn((List<Announcement>) args[1]);
        when(announceScraper.site()).thenReturn(new WeatherTopicSite(tags));
        when(topicScraper.iterator()).thenReturn(((List<WeatherTopic>) args[2]).iterator());
        when(topicScraper.site()).thenReturn(new WeatherTopicSite(tags));
        WeatherTopics target = new WeatherTopics(topicScraper, titleScraper, announceScraper, new DummyProvider());
        String expected = "タイトルです。\nご確認下さい。\n関東: 平常通り運航します。\nこの情報は当日のものです。#weather";
        // exercise
//        Message actual = target.message();
        // verify
//        assertThat(actual.text(), is(expected));
    }
    
    private static class DummyProvider implements Provider<Message> {

        @Override
        public Message get() {
            return new FeederMessage();
        }
        
    }

}
