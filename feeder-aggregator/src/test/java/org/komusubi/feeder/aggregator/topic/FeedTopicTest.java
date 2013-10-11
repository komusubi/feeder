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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.komusubi.feeder.aggregator.rss.FeedReader;
import org.komusubi.feeder.aggregator.rss.FeedReader.EntryScript;
import org.komusubi.feeder.bind.FeederMessagesProvider;
import org.komusubi.feeder.model.Tags;
import org.mockito.Mock;

/**
 * @author jun.ozeki
 */
public class FeedTopicTest {

    @Before
    public void before() {
        initMocks(this);
    }

    @Mock private FeedReader mockReader;
    @Mock private EntryScript mockScript;
    
    @Test
    public void scriptInEachMessage() {
        // setup
        String text = "entry script entity.";
        when(mockScript.trimedLine()).thenReturn(text);
        when(mockReader.retrieve()).thenReturn(Arrays.asList(mockScript));
        when(mockReader.tags()).thenReturn(new Tags());
        // exercise
        FeedTopic target = new FeedTopic(mockReader, new FeederMessagesProvider());
        assertThat(target.messages().size(), is(1));
        assertThat(target.messages().get(0).size(), is(1));
        assertThat(target.messages().get(0).get(0).trimedLine(), is(text));
        // verify
        verify(mockScript, times(1)).trimedLine();
        verify(mockReader, times(3)).retrieve();
    }

}
