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
package org.komusubi.feeder.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author jun.ozeki
 */
public class UrlTest {

    @Test
    public void delegationMethods() throws Exception {
        Url shorten = new Url("http://bit.ly/1do2OxD");
        assertThat(shorten.getHost(), equalTo("bit.ly"));
        assertThat(shorten.getAuthority(), equalTo("bit.ly"));
        assertThat(shorten.getPort(), equalTo(-1));
        assertThat(shorten.getDefaultPort(), equalTo(80));
        assertThat(shorten.getPath(), equalTo("/1do2OxD"));
        assertThat(shorten.getFile(), equalTo("/1do2OxD"));
        assertThat(shorten.getProtocol(), equalTo("http"));
    }

    // TODO issue #19
    @Ignore
    @Test
    public void shorten() {
        Url shorten = new Url("https://githug.com/");
        Url once = shorten.shorten();
        assertThat(shorten.toExternalForm(), equalTo(once.toExternalForm()));
        Url twice = shorten.shorten(); 
        assertThat(shorten.toExternalForm(), equalTo(twice.toExternalForm()));
    }
}
