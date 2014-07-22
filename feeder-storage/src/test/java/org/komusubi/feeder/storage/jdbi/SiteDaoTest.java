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
package org.komusubi.feeder.storage.jdbi;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.komusubi.feeder.model.WebSite;
import org.komusubi.feeder.utils.Types.AggregateType;
import org.komusubi.feeder.utils.Types.ScrapeType;

/**
 * @author jun.ozeki
 */
public class SiteDaoTest {

    private static final String EXPECTED_DOM_WEATHER_URL = "http://www.jal.co.jp/cms/other/ja/weather_info_dom.html";
    private static final String EXPECTED_DOM_INFO_URL = "http://rss.jal.co.jp/f4746/index.rdf";
    private static ExternalStorageResource storage;
    private SiteDao target;

    @BeforeClass
    public static void beforeClass() {
        storage = new ExternalStorageResource("jdbc:h2:mem:feeder", "user", "");
        storage.before();
        FeedDao feedDao = storage.open(FeedDao.class);
        feedDao.createTable();
        ChannelDao channelDao = storage.open(ChannelDao.class);
        channelDao.createTable();
        CategoryDao categoryDao = storage.open(CategoryDao.class);
        categoryDao.createTable();
        SiteDao siteDao = storage.open(SiteDao.class);
        siteDao.createTable();
        storage.execute("insert into feeds values (1, 'SCRAPE')");
        storage.execute("insert into feeds values (2, 'RSS')");
        storage.execute("insert into channels values (0, 'jal')");
        storage.execute("insert into channels values (1, '5971')");
        storage.execute("insert into channels values (2, '5931')");
        storage.execute("insert into channels values (3, 'jmb')");
        storage.execute("insert into categories values (1, 'WEATHER')");
        storage.execute("insert into categories values (2, 'INFORMATION')");
        storage.execute("insert into categories values (3, 'TOUR')");
        storage.execute("insert into categories values (4, 'INVESTER RELATIONS')");
        storage.execute("insert into categories values (5, 'PRESS RELEASE')");
        storage.execute("insert into categories values (6, 'FLIGHT STATUS')");
        storage.execute("insert into sites values (null, '運行の見通し(国内線)', 1, 1, 1, '" + EXPECTED_DOM_WEATHER_URL + "')");
        storage.execute("insert into sites values (null, '運行の見通し(国際線)', 1, 2, 1, 'http://www.jal.co.jp/cms/other/ja/weather_info_int.html')");
        storage.execute("insert into sites values (null, 'JALからのお知らせ', 2, 0, 2, 'http://rss.jal.co.jp/f4728/index.rdf')");
        storage.execute("insert into sites values (null, '国内線のお知らせ', 2, 1, 2, '" + EXPECTED_DOM_INFO_URL + "')");
        storage.execute("insert into sites values (null, '国際線のお知らせ', 2, 2, 2, 'http://rss.jal.co.jp/f4747/index.rdf')");
        storage.execute("insert into sites values (null, 'JALマイレージバンクのお知らせ', 2, 3, 2, 'http://rss.jal.co.jp/f4749/index.rdf')");
    }

    @AfterClass
    public static void afterClass() {
        storage.execute("drop table sites");
        storage.execute("drop table categories");
        storage.execute("drop table channels");
        storage.execute("drop table feeds");
        storage.after();
    } 

    @Before
    public void before() {
        target = storage.open(SiteDao.class); 
    }

    @Test
    public void findByPrimaryKey() {
        // setup
        
        // exercise
        WebSite site = target.findById(new Integer(1));

        // verify
        assertThat(site.url().toExternalForm(), is(EXPECTED_DOM_WEATHER_URL));
    }

    @Test
    public void findByFeedAndChannel() {
        // setup
        // exercise
        WebSite site = target.findByFeedAndChannel(AggregateType.FEEDER, ScrapeType.JAL5971);

        // verify 
        assertThat(site.url().toExternalForm(), is(EXPECTED_DOM_INFO_URL));
    }
}
