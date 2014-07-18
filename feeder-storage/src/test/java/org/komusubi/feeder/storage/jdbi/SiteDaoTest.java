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

import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.komusubi.feeder.model.WebSite;
import org.komusubi.feeder.utils.Types.AggregateType;
import org.komusubi.feeder.utils.Types.ScrapeType;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

/**
 * @author jun.ozeki
 */
public class SiteDaoTest {

    @Ignore
    @BeforeClass
    public static void beforeClass() {
        DBI dbi = new DBI("jdbc:mysql://localhost:3306/development", "root", "");
        SiteDao siteDao = dbi.open(SiteDao.class);
        FeedDao feedDao = dbi.open(FeedDao.class);
        siteDao.createTable(); 
        feedDao.createTable();
        Handle handle = dbi.open();
        // TODO insert initial load data.
//        handle.execute("");
    }

    @Ignore
    @Test
    public void test() {
        DataSource ds = null;
        DBI dbi = new DBI("jdbc:mysql://localhost:3306/development", "root", "");
        SiteDao dao = dbi.open(SiteDao.class);
//        WebSite site = dao.findByFeed(AggregateType.FEEDER);
        WebSite site = dao.findById(new Integer(1));
        System.out.printf("site: %s%n", site.url());
        List<WebSite> list = dao.findByFeed(AggregateType.FEEDER);
        for (WebSite s: list) {
            System.out.printf("s: %s%n", s.url());
        }
        List<WebSite> sites = dao.findByChannel(ScrapeType.JAL5971);
        for (WebSite s: sites) {
            System.out.printf("channel from: %s%n", s.url());
        }
        WebSite s = dao.readByFeedAndChannel(AggregateType.SCRAPER, ScrapeType.JAL5931);
        System.out.printf("exec: %s:%s is %s%n", AggregateType.SCRAPER, ScrapeType.JAL5931, s.url());
        dao.close();
    }

}
