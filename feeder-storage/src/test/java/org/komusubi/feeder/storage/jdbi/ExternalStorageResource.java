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

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.rules.ExternalResource;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jun.ozeki
 */
public class ExternalStorageResource extends ExternalResource {
    private static final Logger logger = LoggerFactory.getLogger(ExternalStorageResource.class);
    private DBI dbi;
    private DataSource ds;
    private Handle handle;

    /**
     * create instance from DataSource.
     * @param ds
     */
    public ExternalStorageResource(DataSource ds) {
        this.ds = ds; 
    }

    public ExternalStorageResource(String jdbcUrl, String user, String password) {
        this(JdbcConnectionPool.create(jdbcUrl, user, password));
    }

    @Override
    public void before() {
        logger.info("before test open resource.");
        dbi = new DBI(ds); 
        handle = dbi.open();
    }

    @Override
    public void after() {
        handle.close();
        logger.info("after test resource closed.");
    }

    public void execute(String sql, Object... args) {
        handle.execute(sql, args); 
    }

    public <T> T open(Class<T> dao) {
        return dbi.open(dao);
    }
}

