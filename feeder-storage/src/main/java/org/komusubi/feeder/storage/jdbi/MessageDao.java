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

import org.komusubi.feeder.model.Message;
import org.komusubi.feeder.model.Message.Script;
import org.komusubi.feeder.storage.jdbi.binder.MessageBinder;
import org.komusubi.feeder.storage.jdbi.binder.MessageContainerFactory;
import org.komusubi.feeder.storage.jdbi.binder.MessageExistBinder;
import org.komusubi.feeder.storage.jdbi.mapper.MessageMapper;
import org.komusubi.feeder.storage.table.StorageMessage;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterContainerMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;
import org.skife.jdbi.v2.util.BooleanMapper;

/**
 * @author jun.ozeki
 */
public abstract class MessageDao implements Transactional<MessageDao> {

    @SqlUpdate("create table if not exists messages (id int auto_increment primary key,"
                                    + "site_id int not null,"
                                    + "created timestamp,"
                                    + "foreign key (site_id) references sites(id))")
    public abstract void createTable();

    public abstract void close();

    // it does NOT need to compare with "created" timestamp 
    @SqlQuery("select exists(select 1 from messages where site_id = (select id from sites where url = :url))")
    @Mapper(BooleanMapper.class)
    protected abstract Boolean _exists(@MessageExistBinder Message message);

    public Boolean exists(Message message) {
        if (!_exists(message)) { 
            return Boolean.FALSE;
        }
        for (Script s: message) {
            if (!getScriptDao().exists(s))
                return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @RegisterContainerMapper(MessageContainerFactory.class)
    @Mapper(MessageMapper.class)
    @SqlQuery("select id, created, site_id from messages where id = :id")
    protected abstract Message _findById(@Bind("id") Integer id);
    
    @CreateSqlObject
    protected abstract ScriptDao getScriptDao();
    
    @CreateSqlObject
    protected abstract SiteDao getSiteDao();

    public Message findById(Integer id) {
        StorageMessage message = (StorageMessage) _findById(id);
        message.addAll(getScriptDao().findByMessageId(id));
        message.setSite(getSiteDao().findById(message.siteId()));
        return message;
    }

    @GetGeneratedKeys
    @SqlUpdate("insert into messages (id, site_id, created) values "
             + "(null, (select id from sites where url = :url), :created)")
    protected abstract Integer _persist(@MessageBinder Message message);
    
    @Transaction
    public Integer persist(Message message) {
        Integer id = _persist(message);
        for (Script s: message)
            getScriptDao().persist(s, id);
        return id;
    }
}

