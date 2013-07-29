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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.komusubi.common.util.Resolver;
import org.komusubi.feeder.model.Page;
import org.komusubi.feeder.model.Topic;
import org.komusubi.feeder.model.Topics;
import org.komusubi.feeder.sns.History;
import org.komusubi.feeder.sns.twitter.TweetMessage;
import org.komusubi.feeder.sns.twitter.TweetTopic;
import org.komusubi.feeder.sns.twitter.TweetTopics;
import org.komusubi.feeder.sns.twitter.Twitter4j;
import org.komusubi.feeder.sns.twitter.strategy.SleepStrategy.PageCache;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import twitter4j.Status;

/**
 * @author jun.ozeki
 */
@RunWith(Enclosed.class)
public class SleepStrategyTest {

//    @Mock private SocialNetwork mockSocialNetwork;

    public static class PageCacheTest {
        @Mock private Twitter4j mockTwitter4j;
        @Mock private Resolver<Date> mockResolver;
        @Mock private History mockHistory;
        @Mock private Page mockPage;
        @Mock private Topics<? extends Topic> mockTopics;
        @Mock private TweetTopics tweetTopics;

        @Before
        public void before() {
            MockitoAnnotations.initMocks(this);
        }

        @Test
        public void overOutdated() throws Exception {
            // setup
            Date date = DateUtils.parseDate("2013/08/01 10:11:35", new String[] { "yyyy/MM/dd HH:mm:ss" });
            when(mockResolver.resolve()).thenReturn(date);
            when(mockTwitter4j.history()).thenReturn(mockHistory);
            when(mockHistory.next()).thenReturn(mockPage);
            when(mockPage.topics()).thenReturn(null);

            // exercise
            SleepStrategy parent = new SleepStrategy(mockTwitter4j, 1L);
            PageCache cache = parent.new PageCache(mockResolver, 36000L);

            // verify
            assertFalse(cache.outdated());
            verify(mockResolver, times(2)).resolve();
        }

        @Test
        public void upToDate() throws Exception {
           // setup 

           when(mockResolver.resolve()).thenAnswer(new Answer<Date>() {

               private int count = 0;
               @Override
               public Date answer(InvocationOnMock invocation) throws Throwable {
                   Date date;
                   if (count++ == 0)
                       date = DateUtils.parseDate("2013/07/31 19:18:20", new String[]{"yyyy/MM/dd HH:mm:ss"});
                   else 
                       date = DateUtils.parseDate("2013/07/31 20:18:21", new String[]{"yyyy/MM/dd HH:mm:ss"});
                   return date;
               }
               
           });
           when(mockTwitter4j.history()).thenReturn(mockHistory);
           when(mockHistory.next()).thenReturn(mockPage);
           when(mockPage.topics()).thenReturn(null);
           
           // exercise
           SleepStrategy parent = new SleepStrategy(mockTwitter4j, 1L);
           PageCache target = parent.new PageCache(mockResolver, 36000L);
           
           // verify
           assertTrue(target.outdated());
           verify(mockResolver, times(2)).resolve();
        }
        
        @Mock private Status mockStatus;

        @Test
        public void existsMessage() {
            // setup
            String msg = "sample test message";
            // new another instance does not work, jdk give a same object id.
//            when(mockStatus.getText()).thenReturn(msg.substring(0, msg.length())); 
            when(mockStatus.getText()).thenReturn(msg);

            Topics<? extends Topic> topics = new Topics<Topic>();
            TweetTopic topic = new TweetTopic(mockStatus);
            topics.add(topic);

            when(mockTwitter4j.history()).thenReturn(mockHistory);
            when(mockHistory.next()).thenReturn(mockPage);
            doReturn(topics).when(mockPage).topics();

            TweetMessage message = new TweetMessage();
//            message.append(msg.substring(0, msg.length()));
            message.append(msg);

            // exercise
            SleepStrategy parent = new SleepStrategy(mockTwitter4j, 1L);
            PageCache cache = parent.new PageCache();
           
            // verify
            assertTrue(cache.exists(message));
            verify(mockPage).topics();
        }
    }

}
