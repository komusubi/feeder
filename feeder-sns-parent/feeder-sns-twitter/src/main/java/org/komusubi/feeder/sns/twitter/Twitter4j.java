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

import org.komusubi.feeder.model.Message;
import org.komusubi.feeder.model.Message.Script;
import org.komusubi.feeder.model.Messages;
import org.komusubi.feeder.model.ScriptLine;
import org.komusubi.feeder.model.Topic;
import org.komusubi.feeder.model.Topics;
import org.komusubi.feeder.sns.History;
import org.komusubi.feeder.sns.SocialNetwork;
import org.komusubi.feeder.sns.twitter.TweetMessage.TweetScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * @author jun.ozeki
 */
public class Twitter4j implements SocialNetwork {

    private static final Logger logger = LoggerFactory.getLogger(Twitter4j.class);
    private Twitter twitter;
    private boolean outputConsole = Boolean.getBoolean("tweet.console");

    /**
     * create new instance.
     */
    public Twitter4j() {
        this(TwitterFactory.getSingleton());
    }

    /**
     * 
     * @param prefix in property key
     */
    public Twitter4j(String prefix) {
        this(new TwitterFactory(prefix).getInstance());
    }
    /**
     * create new instance for unit test
     * @param twitter
     */
    // package scope 
    Twitter4j(Twitter twitter) {
        this.twitter = twitter;
    }

    /**
     * @see org.komusubi.feeder.sns.SocialNetwork#post(org.komusubi.feeder.model.Message)
     */
    @Override
    public void post(Message message) {
        tweet(message);
    }

    /**
     * @see org.komusubi.feeder.sns.SocialNetwork#post(org.komusubi.feeder.model.Messages)
     */
    @Override
    public void post(Messages<? extends Message> messages) {
        for (Message m: messages)
            tweet(m);
    }

    /**
     * @see org.komusubi.feeder.sns.SocialNetwork#post(Topic topic)
     */
    @Override
    public void post(Topic topic) {
        post(topic.messages());
    }

    /**
     * @see org.komusubi.feeder.sns.SocialNetwork#post(org.komusubi.feeder.model.Topics)
     */
    @Override
    public void post(Topics<? extends Topic> topics) {
        for (Topic t: topics) 
            post(t);
    }

    /**
     * tweet.
     * @param message
     */
    public void tweet(Message message) {
        Script current = new ScriptLine("");
        try {
            Status result = null;
            for (Script script: message) {
                current = script; // mark current script for when exception occurred.
                if (outputConsole) {
                    System.out.printf("tweet(length:%d): %s%n",
                                        TweetScript.lengthAfterTweeted(script.trimedLine()), script.trimedLine());
                } else {
                    StatusUpdate status = new StatusUpdate(script.trimedLine());
                    if (result != null) {
                        status.inReplyToStatusId(result.getId());
                    }
                    logger.info("tweet(length:{}): {}", TweetScript.lengthAfterTweeted(status.getStatus()),
                                                        status.getStatus());
                    result = twitter.updateStatus(status);
                }
            }
        } catch (TwitterException e) {
            throw new Twitter4jException(String.format("tweet(length:%d): %s",  
                                            TweetScript.lengthAfterTweeted(current.trimedLine()),
                                            current.trimedLine()));
        }
    }

    /**
     * @see org.komusubi.feeder.sns.SocialNetwork#history()
     */
    @Override
    public History history() {
        return new TweetHistory(twitter);
    }

}
