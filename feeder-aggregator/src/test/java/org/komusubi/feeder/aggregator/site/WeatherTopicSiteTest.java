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
package org.komusubi.feeder.aggregator.site;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.Test;
import org.komusubi.feeder.aggregator.site.WeatherTopicSite;

/**
 * @author jun.ozeki
 */
public class WeatherTopicSiteTest {

    private static final String WEATHER_TOPIC_URL = "http://www.jal.co.jp/cms/other/ja/weather_info_dom.html";

    @Test
    public void URL取得() throws Exception {
        WeatherTopicSite site = new WeatherTopicSite();
        assertEquals(new URL(WEATHER_TOPIC_URL), site.url());
    }
}
