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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.komusubi.feeder.bind.BitlyUrlShortening;
import org.komusubi.feeder.spi.UrlShortening;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author jun.ozeki
 */
public class UrlTest {

    @Mock private UrlShortening urlShortening;
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void delegationMethods() throws Exception {
        Url shorten = new Url("http://bit.ly/1do2OxD", new BitlyUrlShortening());
        assertThat(shorten.getHost(), equalTo("bit.ly"));
        assertThat(shorten.getAuthority(), equalTo("bit.ly"));
        assertThat(shorten.getPort(), equalTo(-1));
        assertThat(shorten.getDefaultPort(), equalTo(80));
        assertThat(shorten.getPath(), equalTo("/1do2OxD"));
        assertThat(shorten.getFile(), equalTo("/1do2OxD"));
        assertThat(shorten.getProtocol(), equalTo("http"));
    }

    @Test
    public void shorten() throws Exception {
        // setup
        String url = "https://github.com";
        URL in = new URL(url);
        URL out = new URL("https://bit.ly/dummy");
        when(urlShortening.shorten(in)).thenReturn(out);

        // exercise
        Url shorten = new Url(url, urlShortening);
        Url once = shorten.shorten();
        Url twice = shorten.shorten(); 
        
        // verify
        assertThat(shorten.toExternalForm(), equalTo(once.toExternalForm()));
        assertThat(shorten.toExternalForm(), equalTo(twice.toExternalForm()));
        assertThat(shorten.getOrigin().toExternalForm(), equalTo(url));
        // call Url#shorten twice but UrlShorten#shorten called only once.
        verify(urlShortening, times(1)).shorten(in);
    }
}
