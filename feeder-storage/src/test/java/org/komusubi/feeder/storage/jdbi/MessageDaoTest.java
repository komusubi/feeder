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

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.komusubi.feeder.bind.BitlyUrlShortening;
import org.komusubi.feeder.bind.FeederMessage;
import org.komusubi.feeder.model.Message;
import org.komusubi.feeder.model.Url;
import org.komusubi.feeder.model.WebSite;
import org.komusubi.feeder.storage.jdbi.binder.MessageContainerFactory;

/**
 * @author jun.ozeki
 */
public class MessageDaoTest {

    @Rule
    public ExternalStorageResource storage = new ExternalStorageResource("jdbc:h2:mem:feeder", "user", "");
    private MessageDao target;
    
    @Before
    public void before() {
        // add container factory
        storage.registerContainerFactory(new MessageContainerFactory());

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
    
    @Ignore
    @Test
    public void simplePersist() {
        FeederMessage message = new FeederMessage();
        message.append("フィードメッセージ");
        message.setSite(new WebSite(new Url("http://unknown.com", new BitlyUrlShortening())));
        // FIXME why success to persist in wrong foreign key by Url value?
        Integer id = target.persist(message);
        Message result = target.findById(id);
        System.out.printf("result(%d) = %s%n", id, result);
    }
}
