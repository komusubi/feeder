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

import org.komusubi.feeder.model.Message.Script;
import org.komusubi.feeder.storage.jdbi.binder.ScriptBinder;
import org.komusubi.feeder.storage.jdbi.binder.ScriptExistBinder;
import org.komusubi.feeder.storage.jdbi.mapper.ScriptMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;
import org.skife.jdbi.v2.util.BooleanMapper;

/**
 * @author jun.ozeki
 */
public interface ScriptDao extends Transactional<ScriptDao> {

    @SqlUpdate("create table if not exists scripts (hash varchar(64) primary key,"
                                   + "text varchar(1024),"
                                   + "url varchar(255),"
                                   + "message_id int not null,"
                                   + "foreign key (message_id) references messages(id))")
    void createTable();

    void close();
    
    @SqlQuery("select exists(select 1 from scripts where hash = :hash and url = :url)") 
    @Mapper(BooleanMapper.class)
    Boolean exists(@ScriptExistBinder Script script);

    @SqlQuery("select text, url, (select name from channels where id = (select channel from sites where id = "
            + "(select site_id from messages where id = :mid))) as c_name from scripts where message_id = :mid")
    @Mapper(ScriptMapper.class)
    List<Script> findByMessageId(@Bind("mid") Integer id);
    
    @SqlUpdate("insert into scripts (hash, text, url, message_id) values (:hash, :text, :url, :message_id)")
    void persist(@ScriptBinder Script script, @Bind("message_id") Integer mid);
}
