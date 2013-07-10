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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author jun.ozeki
 */
public class TweetMessageTest {

    @Rule public ExpectedException exception = ExpectedException.none();
    
    private TweetMessage target;

    @Before
    public void before() {
        target = new TweetMessage();
    }

    @Test
    public void null文字の追加時に例外が発生する事() {
        exception.expect(Twitter4jException.class);
        exception.expectMessage("line must NOT be null");
        // exercise
        target.append((String) null);
    }
    
    @Test
    public void 文字列追加時に分割して追加される事() {
        // setup
        String line = "あいうえおかきくけこさしすせそたちつてと"
                    + "なにぬねのはひふへほまみむめもやいゆえよ"
                    + "らりるれろアイウエオカキクケコサシスセソ"
                    + "タチツテトナニヌネノハヒフヘホマミムメモ"
                    + "あいうえおかきくけこさしすせそたちつてと"
                    + "なにぬねのはひふへほまみむめもやいゆえよ"
                    + "らりるれろアイウエオカキクケコサシスセソ"
                    + "タチツテトナニヌネノハヒフヘホマミムメモ"
                    + "あいうえおかきくけこさしすせそたちつてと";
        String expected1 = 
                      "あいうえおかきくけこさしすせそたちつてと"
                    + "なにぬねのはひふへほまみむめもやいゆえよ"
                    + "らりるれろアイウエオカキクケコサシスセソ"
                    + "タチツテトナニヌネノハヒフヘホマミムメモ"
                    + "あいうえおかきくけこさしすせそたちつてと"
                    + "なにぬねのはひふへほまみむめもやいゆえよ"
                    + "らりるれろアイウエオカキクケコサシスセソ";
        String expected2 = 
                      "タチツテトナニヌネノハヒフヘホマミムメモ"
                    + "あいうえおかきくけこさしすせそたちつてと";
        // exercise
        target.append(line);
        // verify
        assertThat(target.size(), is(2));
        assertThat(target.get(0).line(), is(expected1));
        assertThat(target.get(1).line(), is(expected2));
    }
}
