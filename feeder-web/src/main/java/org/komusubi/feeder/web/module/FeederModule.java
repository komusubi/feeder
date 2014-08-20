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
package org.komusubi.feeder.web.module;

import org.komusubi.feeder.sns.Speaker;
import org.komusubi.feeder.sns.twitter.Twitter4j;
import org.komusubi.feeder.sns.twitter.strategy.SleepStrategy;
import org.komusubi.feeder.storage.cache.StoragePageCache;
import org.komusubi.feeder.storage.jdbi.MessageDao;

/**
 * @author jun.ozeki
 */
public class FeederModule implements Module {

    private TopicsBuilder builder;

    public FeederModule(TopicsBuilder topicsBuilder) {
        this.builder = topicsBuilder;
    }

    /**
     * @see org.komusubi.feeder.web.module.Module#run()
     */
    @Override
    public void run() {
        MessageDao messageDao = builder.dbi().onDemand(MessageDao.class);
        Speaker speaker = new Speaker(new Twitter4j(builder.scrapeType(), messageDao), 
                                        new SleepStrategy(new StoragePageCache(messageDao)));
        speaker.talk(builder.topics());
        // TODO close is necessary when use with onDemand method ?
        messageDao.close();
    }

}
