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

import java.util.Date;

import org.komusubi.feeder.model.FeederMessage;
import org.komusubi.feeder.model.Message;
import org.komusubi.feeder.model.Topic;

import twitter4j.Status;

/**
 * @author jun.ozeki
 */
public class TweetTopic implements Topic {

    private static final long serialVersionUID = 1L;
    private Status status;
    private Message message;

    /**
     * create new instance.
     * @param status
     */
    public TweetTopic(Status status) {
        this.status = status;
        this.message = new FeederMessage().append(status.getText());
    }

    // package
    TweetTopic(String str) {
        this.message = new FeederMessage().append(str);
    }

    /**
     * @see org.komusubi.feeder.model.Topic#message()
     */
    public Message message() {
        return message;
    }

    /**
     * @see org.komusubi.feeder.model.Topic#createdAt()
     */
    @Override
    public Date createdAt() {
        return status.getCreatedAt();
    }
}
