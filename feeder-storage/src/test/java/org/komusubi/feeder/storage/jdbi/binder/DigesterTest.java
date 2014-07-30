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
package org.komusubi.feeder.storage.jdbi.binder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class DigesterTest {
    
    @Test
    public void digestSha1() {
        // setup
        String value = "ダイジェスト対象文字列はこの本文です。";
        // byte[] expected = new byte[]{};
        // exercise
        byte[] actual = Digester.sha1(value);
        // verify
        // assertArrayEquals(actual, expected);
        assertThat(actual.length, is(20));
    }

    @Test
    public void hexString() {
        // setup
        byte[] bytes = "バイト配列".getBytes();
        String expected = "836f83438367947a97f1";
        // exercise
        String actual = Digester.hex(bytes);
        // verify
        assertThat(actual, is(expected));
    }

    @Test
    public void sha1ToHex() {
        // setup
        String value = "メッセージダイジェスト実施前テキスト";
        String expected = "b848a26687221a47291864ff82dad7d067f084";
        // exercise
        String actual = Digester.hex(Digester.sha1(value));
        // verify
        assertThat(actual, is(expected));
        assertThat(actual.length(), is(38));
    }
}

