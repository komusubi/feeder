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

import static org.junit.Assert.assertArrayEquals;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FeedDaoTest {

    private static ExternalStorageResource storage;
    private static String EXPECTED_SCRAPE = "SCRAPE";
    private static String EXPECTED_RSS = "RSS";
    private static String[] EXPECTED_FEEDS = { EXPECTED_SCRAPE, EXPECTED_RSS };
    private FeedDao target;

    @BeforeClass
    public static void beforeClass() {
        storage = new ExternalStorageResource("jdbc:h2:mem:feeder", "user", ""); 
        storage.before();
        FeedDao feedDao = storage.open(FeedDao.class);
        feedDao.createTable();
        storage.execute("insert into feeds values (1, 'SCRAPE')");
        storage.execute("insert into feeds values (2, 'RSS')");
    }

    @AfterClass
    public static void afterClass() {
        storage.execute("drop table feeds");
        storage.after();
    }

    @Before
    public void before() {
        target = storage.open(FeedDao.class); 
    }

    @Test
    public void findName() {
        // setup
        // exercise
        List<String> list = target.findAll();

        // verify
        assertArrayEquals(list.toArray(new String[0]), EXPECTED_FEEDS);
    }
}
