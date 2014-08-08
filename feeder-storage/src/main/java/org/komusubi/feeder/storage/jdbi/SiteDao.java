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

import org.komusubi.feeder.model.WebSite;
import org.komusubi.feeder.storage.jdbi.binder.AggregateTypeBinder;
import org.komusubi.feeder.storage.jdbi.binder.ScrapeTypeBinder;
import org.komusubi.feeder.storage.jdbi.mapper.WebSiteMapper;
import org.komusubi.feeder.utils.Types.AggregateType;
import org.komusubi.feeder.utils.Types.ScrapeType;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

@RegisterMapper(WebSiteMapper.class)
public interface SiteDao extends Transactional<SiteDao> {
    @SqlUpdate("create table if not exists sites (id int auto_increment," 
                                  + "name varchar(255),"
                                  + "feed int,"
                                  + "channel int,"
                                  + "category int,"
                                  + "url varchar(255),"
                                  + "primary key (id),"
                                  + "foreign key (feed) references feeds(id),"
                                  + "foreign key (channel) references channels(id),"
                                  + "foreign key (category) references categories(id))")
    void createTable();

    @SqlQuery("select s.id, s.name, f.name, c.name as c_name, s.category, s.url as s_url from sites s, channels c, feeds f "
                    + "where s.id = :id and s.channel = c.id and s.feed = f.id")
    WebSite findById(@Bind("id") Integer id);

    @SqlQuery("select s.name, c.name, s.feed, s.url from sites s, channels c where feed = "
                    + "(select id from feeds where name = :feed) and s.channel = c.id")
    List<WebSite> findByFeed(@AggregateTypeBinder AggregateType type);

    @SqlQuery("select s.name, c.name as c_name, s.feed, s.url as s_url from sites s, channels c where channel = "
                    + "(select id from channels where name = :channel) and s.channel = c.id")
    List<WebSite> findByChannel(@ScrapeTypeBinder ScrapeType type);
    
    @SqlQuery("select s.name, c.name as c_name, s.feed, s.url as s_url from sites s, channels c, feeds f where "
                    + "feed = (select id from feeds where name = :feed) and "
                    + "channel = (select id from channels where name = :channel) and "
                    + "s.channel = c.id and s.feed = f.id")
    WebSite findByFeedAndChannel(@AggregateTypeBinder AggregateType aggregate,
                                 @ScrapeTypeBinder ScrapeType scrape);

    void close();
}
