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
package org.komusubi.feeder.web.scheduler;

import org.junit.rules.ExternalResource;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author jun.ozeki
 */
public class SchedulerResource extends ExternalResource {

    private Scheduler scheduler;

    /**
     * @see org.junit.rules.ExternalResource#before()
     */
    @Override
    protected void before() throws Throwable {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
    }
    
    /**
     * @see org.junit.rules.ExternalResource#after()
     */
    @Override
    protected void after() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
    
    public Scheduler scheduler() {
        return scheduler;
    }
}
