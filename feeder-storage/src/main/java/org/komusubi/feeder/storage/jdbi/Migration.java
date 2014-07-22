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

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.komusubi.feeder.storage.StorageException;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author jun.ozeki
 */
public class Migration {
    private static final Logger logger = LoggerFactory.getLogger(Migration.class);
    
    public static void main(String[] args) {
        if (args.length < 2) {
            usage(System.err);
            System.exit(1);
        }
        Migration migration = new Migration();
        migration.execute(args[0], args[1], args[2]);
        logger.info("feeder migration done.");
    }

    private static void usage(PrintStream stream) {
        stream.printf("Migration: jdbcurl user password");
    }

    private void execute(String url, String usr, String pass) {
        Class<?>[] daoArray = { 
                                CategoryDao.class,
                                ChannelDao.class,
                                FeedDao.class,
                              };
        DBI dbi = new DBI(url, usr, pass);
        for (Class<?> table: daoArray) {
            initTable(dbi, table);
        }
    }

    private void initTable(DBI dbi, Class<?> dao) {
        Object object = dbi.open(dao);
        try {
            Method method = dao.getDeclaredMethod("createTable");
            method.invoke(object, null);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException 
                | IllegalArgumentException | InvocationTargetException e) {
            throw new StorageException(e);
        }
    }
}
