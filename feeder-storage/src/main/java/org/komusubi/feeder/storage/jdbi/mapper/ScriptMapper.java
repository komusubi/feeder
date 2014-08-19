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
package org.komusubi.feeder.storage.jdbi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.komusubi.feeder.bind.BitlyUrlShortening;
import org.komusubi.feeder.model.Message.Script;
import org.komusubi.feeder.model.ScriptLine;
import org.komusubi.feeder.model.Url;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class ScriptMapper implements ResultSetMapper<Script> {

    /**
     * @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet, org.skife.jdbi.v2.StatementContext)
     */
    @Override
    public Script map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        // FIXME UrlShortening Factory need ?
        Url url = null;
        if (r.getString("url") != null)
            url = new Url(r.getString("url"), new BitlyUrlShortening());
        ScriptLine script = new ScriptLine(r.getString("text"), url);
        return script;
    }

}

