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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.komusubi.feeder.bind.BitlyUrlShortening;
import org.komusubi.feeder.bind.FeederMessage;
import org.komusubi.feeder.model.Message;
import org.komusubi.feeder.model.Url;
import org.komusubi.feeder.model.WebSite;
import org.komusubi.feeder.utils.Types.ScrapeType;

/**
 * @author jun.ozeki
 */
public class MessageDaoTest {

    @Rule
    public ExternalStorageResource storage = new ExternalStorageResource("jdbc:h2:mem:feeder", "user", "");
    private MessageDao target;
    
    @Before
    public void before() {
        FeedDao feedDao = storage.open(FeedDao.class);
        feedDao.createTable();
        ChannelDao channelDao = storage.open(ChannelDao.class);
        channelDao.createTable();
        CategoryDao categoryDao = storage.open(CategoryDao.class);
        categoryDao.createTable();
        SiteDao siteDao = storage.open(SiteDao.class);
        siteDao.createTable();
        target = storage.open(MessageDao.class);
        target.createTable();
        ScriptDao scriptDao = storage.open(ScriptDao.class);
        scriptDao.createTable();
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
        storage.execute("insert into sites values (null, '運行の見通し(国内線)', 1, 1, 1, 'http://www.jal.co.jp/cms/other/ja/weather_info_dom.html')");
        storage.execute("insert into sites values (null, '運行の見通し(国際線)', 1, 2, 1, 'http://www.jal.co.jp/cms/other/ja/weather_info_int.html')");
        storage.execute("insert into sites values (null, 'JALからのお知らせ', 2, 0, 2, 'http://rss.jal.co.jp/f4728/index.rdf')");
        storage.execute("insert into sites values (null, '国内線のお知らせ', 2, 1, 2, 'http://rss.jal.co.jp/f4746/index.rdf')");
        storage.execute("insert into sites values (null, '国際線のお知らせ', 2, 2, 2, 'http://rss.jal.co.jp/f4747/index.rdf')");
        storage.execute("insert into sites values (null, 'JALマイレージバンクのお知らせ', 2, 3, 2, 'http://rss.jal.co.jp/f4749/index.rdf')");
       
        storage.execute("insert into messages values (null, 1, CURRENT_TIMESTAMP)");
        storage.execute("insert into scripts values ('hashed-string1', 'script1', null, 1)");
        storage.execute("insert into scripts values ('hashed-string2', 'script2', null, 1)");
    }
    
    @After
    public void after() {
        storage.execute("drop table sites");
        storage.execute("drop table categories");
        storage.execute("drop table channels");
        storage.execute("drop table feeds");
        storage.execute("drop table messages");
        storage.execute("drop table scripts");
    }
    
    @Test
    public void findSimple() {
        // setup
        // exercise
        Message message = target.findById(new Integer(1));

        // verify
        assertThat(message.site().url().toExternalForm(), is("http://www.jal.co.jp/cms/other/ja/weather_info_dom.html"));
        assertThat(message.text(), is("script1script2"));
    }
    
    @Test
    public void simplePersist() {
        // setup
        FeederMessage message = new FeederMessage();
        String feedMessage = "フィードメッセージ";
        String url = "http://rss.jal.co.jp/f4728/index.rdf";
        message.append(feedMessage);
        message.setSite(new WebSite(new Url(url, new BitlyUrlShortening(ScrapeType.JAL5931))));

        // exercise
        Integer id = target.persist(message);
        Message actual = target.findById(id);

        // verify
        assertThat(actual.text(), is(feedMessage));
        assertThat(actual.site().url().toExternalForm(), is(url));
    }

    @Test
    public void simpleExists() {
        // setup
        FeederMessage message = new FeederMessage();
        message.append("テスト用メッセージ");
        message.append("追加メッセージテスト");
        String url = "http://rss.jal.co.jp/f4747/index.rdf";
        message.setSite(new WebSite(new Url(url, new BitlyUrlShortening(ScrapeType.JAL5931))));
        
        // exercise
        target.persist(message);
        Boolean actual = target.exists(message);
        
        // verify
        assertTrue(actual);
    }   

    @Test
    public void notExists() {
        // setup 
        FeederMessage message1 = new FeederMessage();
        message1.append("存在確認メッセージ用");
        message1.append("追加メッセージその１");
        String url = "http://rss.jal.co.jp/f4747/index.rdf";
        message1.setSite(new WebSite(new Url(url, new BitlyUrlShortening(ScrapeType.JAL5971))));

        FeederMessage message2 = new FeederMessage();
        message2.append("未永続化メッセージ");
        message2.setSite(new WebSite(new Url(url, new BitlyUrlShortening(ScrapeType.JAL5931))));

        // exercise
        target.persist(message1);
        Boolean actual = target.exists(message2);

        // verify
        assertFalse(actual);
    }
}
