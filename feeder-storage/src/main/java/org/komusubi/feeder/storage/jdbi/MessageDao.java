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
import org.komusubi.feeder.storage.mapper.MessageBinder;
import org.komusubi.feeder.storage.mapper.MessageMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

/**
 * @author jun.ozeki
 */
public interface MessageDao extends Transactional<MessageDao> {

    @SqlUpdate("create table messages (id int auto_increment,"
                                    + "text varchar(1024),"
                                    + "hash varchar(128),"
                                    + "created timestamp,"
                                    + "site_id int,"
                                    + "primary key (id),"
                                    + "foreign key (site_id) references sites(id)")
    void createTable();

    void close();

    boolean exists(@MessageBinder Message message);

    @SqlUpdate("select id, text, hash, created, site_id from messages where id = :id")
    @Mapper(MessageMapper.class)
    Message findById(@Bind("id") Integer id);
    
    @SqlQuery("")
    void persist(Message message);
}

