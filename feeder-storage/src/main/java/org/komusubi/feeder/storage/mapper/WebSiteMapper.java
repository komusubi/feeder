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
package org.komusubi.feeder.storage.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.komusubi.feeder.bind.BitlyUrlShortening;
import org.komusubi.feeder.model.Tag;
import org.komusubi.feeder.model.Url;
import org.komusubi.feeder.model.WebSite;
import org.komusubi.feeder.spi.UrlShortening;
import org.komusubi.feeder.utils.Types.ScrapeType;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author jun.ozeki
 */
public class WebSiteMapper implements ResultSetMapper<WebSite> {


    private Tag[] tags;

    public WebSiteMapper(Tag... tags) {
        this.tags = tags;
    }

    public WebSiteMapper() {
        this((Tag)null);
    }

    /**
     * @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet, org.skife.jdbi.v2.StatementContext)
     */
    @Override
    public WebSite map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        UrlShortening shortening = new BitlyUrlShortening(ScrapeType.find(r.getString("c_name")));
        Url url = new Url(r.getString("s_url"), shortening);
        return new WebSite(url, tags);
    }

}
