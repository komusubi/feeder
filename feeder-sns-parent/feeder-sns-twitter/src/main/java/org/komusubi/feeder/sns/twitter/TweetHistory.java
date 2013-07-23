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
import org.komusubi.feeder.sns.History;

import twitter4j.Twitter;

/**
 * 
 * @author jun.ozeki
 */
public class TweetHistory implements History<Topic> {

    private Twitter twitter;
    private int index;

    public TweetHistory(Twitter twitter) {
        this.twitter = twitter;
        this.index = 0;
    }

    public void count(int count) {
        
    }

    /**
     * @see org.komusubi.feeder.sns.History#indexOf(int)
     */
    @Override
    public Page<Topic> indexOf(int index) {
        if (index < 1)
            throw new IllegalArgumentException("index must be a natural number.");
        HistoryPage<Topic> page = new HistoryPage<Topic>(twitter, index);
        return page;
    }

    /**
     * @see org.komusubi.feeder.sns.History#next()
     */
    @Override
    public Page<Topic> next() {
        return indexOf(++index);
    }

    /**
     * @see org.komusubi.feeder.sns.History#previous()
     */
    @Override
    public Page<Topic> previous() {
        return indexOf(--index);
    }
    
    

}
