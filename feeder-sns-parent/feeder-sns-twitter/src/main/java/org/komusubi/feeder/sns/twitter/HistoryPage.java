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
package org.komusubi.feeder.sns.twitter;

import org.komusubi.feeder.model.Page;
import org.komusubi.feeder.model.Topic;
import org.komusubi.feeder.model.Topics;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * @author jun.ozeki
 */
public class HistoryPage<T extends Topic> implements Page<Topic> {

    private static final long serialVersionUID = 1L;
    private Twitter twitter;
    private Paging current;

    /**
     * create new instance.
     * @param timeline
     */
    public HistoryPage(Twitter twitter, int currentPage) {
        this.twitter = twitter;
        this.current = new Paging(currentPage);
    }

    /**
     * set count of topic per a page.
     * @param count
     * @return
     */
    public HistoryPage<? extends Topic> count(int count) {
        current.count(count);
        return this;
    }

    protected Topics<? extends Topic> topics(int index) {
        ResponseList<Status> timeline;
        int original = current.getPage();
        try {
            current.setPage(index);
            timeline = twitter.getHomeTimeline(current);
            return ListConverter.convert(timeline);
        } catch (TwitterException e) {
            current.setPage(original);
            throw new Twitter4jException(e);
        }
    }

    /**
     * @see org.komusubi.feeder.model.Page#topics()
     */
    @Override
    public Topics<? extends Topic> topics() {
        return topics(current.getPage());
    }

    /**
     * 
     * @author jun.ozeki
     */
    public static class ListConverter {

        public static Topics<? extends Topic> convert(ResponseList<Status> statuses) {
            TweetTopics topics = new TweetTopics();
            for (Status s: statuses) {
                topics.add(new TweetTopic(s));
            }
            return topics;
        }
    }
}
