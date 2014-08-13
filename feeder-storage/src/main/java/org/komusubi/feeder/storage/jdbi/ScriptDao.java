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
import org.komusubi.feeder.storage.jdbi.mapper.ScriptMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

/**
 * @author jun.ozeki
 */
public interface ScriptDao extends Transactional<ScriptDao> {

      // TODO how to return boolean ?
//    boolean exists(@ScriptExistBinder Script script);

    @SqlQuery("select text from scripts where message_id = :mid")
    @Mapper(ScriptMapper.class)
    List<Script> findByMessageId(@Bind("mid") Integer id);
    
    @SqlUpdate("insert into scripts (hash, text, message_id) values (:hash, :text, :message_id)")
    void persist(@ScriptBinder Script script, @Bind("message_id") Integer mid);
}