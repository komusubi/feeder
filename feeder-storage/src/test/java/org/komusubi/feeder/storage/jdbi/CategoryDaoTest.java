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

/**
 * @author jun.ozeki
 */
public class CategoryDaoTest {
    private static final String EXPECTED_WEATHER = "WEATHER";
    private static final String EXPECTED_INFORMATION = "INFORMATION";
    private static final String EXPECTED_TOUR = "TOUR";
    private static final String EXPECTED_INVESTER = "INVESTER RELATIONS";
    private static final String EXPECTED_PRESS = "PRESS RELEASE";
    private static final String EXPECTED_FLIGHT = "FLIGHT STATUS";
    
    private static final String[] EXPECTED_CATEGORY_NAMES = { EXPECTED_WEATHER,
                                                              EXPECTED_INFORMATION,
                                                              EXPECTED_TOUR,
                                                              EXPECTED_INVESTER,
                                                              EXPECTED_PRESS,
                                                              EXPECTED_FLIGHT };
    private static ExternalStorageResource storage;
    private CategoryDao target;
    
    @BeforeClass
    public static void beforeClass() {
        storage = new ExternalStorageResource("jdbc:h2:mem:feeder", "user", "");
        storage.before();
        CategoryDao categoryDao = storage.open(CategoryDao.class);
        categoryDao.createTable();
        storage.execute("insert into categories values (1, '" + EXPECTED_WEATHER + "')");
        storage.execute("insert into categories values (2, '" + EXPECTED_INFORMATION + "')");
        storage.execute("insert into categories values (3, '" + EXPECTED_TOUR + "')");
        storage.execute("insert into categories values (4, '" + EXPECTED_INVESTER + "')");
        storage.execute("insert into categories values (5, '" + EXPECTED_PRESS + "')");
        storage.execute("insert into categories values (6, '" + EXPECTED_FLIGHT + "')");
    }
    
    @AfterClass
    public static void afterClass() {
        storage.execute("drop table categories");
        storage.after();
    }
    
    @Before
    public void before() {
        target = storage.open(CategoryDao.class);
    }
    
    @Test
    public void findNames() {
        // setup
        // exercise
        List<String> list = target.findAll();
        
        // verify
        assertArrayEquals(list.toArray(new String[0]), EXPECTED_CATEGORY_NAMES);
    }
}
