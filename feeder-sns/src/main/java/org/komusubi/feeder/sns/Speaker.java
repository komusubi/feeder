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
package org.komusubi.feeder.sns;

import javax.inject.Inject;

import org.komusubi.feeder.model.Message;
import org.komusubi.feeder.model.Topic;
import org.komusubi.feeder.model.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jun.ozeki
 */
public class Speaker {
    private static final Logger logger = LoggerFactory.getLogger(Speaker.class);
    private SocialNetwork socialNetwork;
    private GateKeeper gatekeeper;
    
    /**
     * create new instance.
     * @param socialNetwork
     */
    @Inject
    public Speaker(SocialNetwork socialNetwork, GateKeeper gatekeeper) {
        this.socialNetwork = socialNetwork;
        this.gatekeeper = gatekeeper;
    }

    /**
     * talk to friends.
     * @param topics
     */
    public void talk(Topics<? extends Topic> topics) {
        for (Topic topic: topics) {
            talk(topic.message());
        }
    }

    /**
     * talked to friends.
     * @param topic
     */
    public void talk(Topic topic) {
        talk(topic.message());
    }

    /**
     * talk to friends.
     * @param message
     */
    public void talk(Message message) {
        if (gatekeeper.available(message)) {
            socialNetwork.post(message);
            gatekeeper.store(message);
        } else {
            logger.info("message duplicated, dose NOT post: {}", message.text());
        }
    }
}
