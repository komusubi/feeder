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

import java.util.Date;
import java.util.Iterator;

import javax.inject.Provider;

import org.komusubi.feeder.aggregator.rss.FeedReader;
import org.komusubi.feeder.aggregator.site.RssSite;
import org.komusubi.feeder.bind.FeederMessagesProvider;
import org.komusubi.feeder.model.Message;
import org.komusubi.feeder.model.Message.Script;
import org.komusubi.feeder.model.Messages;
import org.komusubi.feeder.model.Tag;
import org.komusubi.feeder.model.Tags;
import org.komusubi.feeder.model.Topic;

/**
 * @author jun.ozeki
 */
public class FeedTopic implements Topic {

    private static final long serialVersionUID = 1L;
    private Date created;
    private Message message;
    private FeedReader reader;
    private Tags tags;
    private Provider<Messages<Message>> provider;
    
    /**
     * create new instance.
     */
    public FeedTopic(RssSite site) {
        this(site, new FeederMessagesProvider());
    }

    /**
     * create new instance.
     */
    public FeedTopic(RssSite site, Provider<Messages<Message>> provider) {
        this(new FeedReader(site), provider);
    }

    /**
     * create new instance. 
     */
    public FeedTopic(FeedReader reader, Provider<Messages<Message>> provider) {
        this.reader = reader;
        this.provider = provider;
        created = new Date();
        this.tags = reader.tags();
    }

    /**
     * @see org.komusubi.feeder.model.Topic#createdAt()
     */
    @Override
    public Date createdAt() {
        return created;
    }

    /**
     * @see org.komusubi.feeder.model.Topic#message()
     */
    @Override
    @Deprecated
    public Message message() {

        for (Script script: reader.retrieve()) {
            boolean tagging = false;
            for (Iterator<Tag> it = tags.iterator(); it.hasNext(); ) {
                Tag tag = it.next();
                if (!tagging && !script.line().endsWith("\n")) {
                    script.append("\n");
                    tagging = true;
                }
                script.append(tag.label());
                if (it.hasNext())
                    script.append(" ");
            }
            message.add(script);
        }
        return message;
    }

    /**
     * @see org.komusubi.feeder.model.Topic#messages()
     */
    @Override
    public Messages<Message> messages() {
        Messages<Message> messages = provider.get();

        for (Script script: reader.retrieve()) {
            Message m = messages.newInstance();
            boolean tagging = false;
            for (Iterator<Tag> it = tags.iterator(); it.hasNext(); ) {
                Tag tag = it.next();
                if (!tagging && !script.line().endsWith("\n")) {
                    script.append("\n");
                    tagging = true;
                }
                script.append(tag.label());
                if (it.hasNext())
                    script.append(" ");
            }
            m.add(script);
            messages.add(m);
        }
        return messages;
    }

    /**
     * @param tags
     */
    public FeedTopic addTag(Tag... tags) {
        for (Tag t: tags)
            this.tags.add(t);
        return this;
    }

}
