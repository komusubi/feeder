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
package org.komusubi.feeder.web.servlet;

import java.util.Properties;

import javax.servlet.ServletContextEvent;

import org.komusubi.feeder.web.FeederWebException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;

import com.google.inject.Injector;

/**
 * @author jun.ozeki
 */
public class QuartzSchedulerListener extends QuartzInitializerListener {

    /**
     * extend quartz StdSchedulerFactory.
     * @author jun.ozeki
     */
    public static class ExSchedulerFactory extends StdSchedulerFactory {

        private Injector injector;

        /**
         * create new instance.
         */
        public ExSchedulerFactory(Injector injector) {
            this.injector = injector;
        }

        /**
         * create new instance.
         * @param props
         * @throws SchedulerException
         */
        public ExSchedulerFactory(Injector injector, Properties props) throws SchedulerException {
            super(props);
            this.injector = injector;
        }

        /**
         * create new instance.
         * @param configFile
         * @throws SchedulerException
         */
        public ExSchedulerFactory(Injector injector, String configFile) throws SchedulerException {
            super(configFile);
            this.injector = injector;
        }

        /**
         * @see org.quartz.impl.StdSchedulerFactory#getScheduler()
         */
        @Override
        public Scheduler getScheduler() throws SchedulerException {
            Scheduler scheduler = super.getScheduler();
            JobFactory jobFactory = injector.getInstance(JobFactory.class);
            if (jobFactory == null)
                throw new FeederWebException("JobFactory must NOT be null.");
            scheduler.setJobFactory(jobFactory);
            return scheduler;
        }
    }

    private Injector injector;

    /**
     * @see org.quartz.ee.servlet.QuartzInitializerListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        injector = (Injector) sce.getServletContext().getAttribute(Injector.class.getName());
        if (injector == null)
            throw new FeederWebException("injector must NOT be null.");

        super.contextInitialized(sce);
    }

    /**
     * @see org.quartz.ee.servlet.QuartzInitializerListener#getSchedulerFactory(java.lang.String)
     */
    @Override
    protected StdSchedulerFactory getSchedulerFactory(String configFile) throws SchedulerException {
        StdSchedulerFactory factory;
        // get Properties
        if (configFile != null) {
            factory = new ExSchedulerFactory(injector, configFile);
        } else {
            factory = new ExSchedulerFactory(injector);
        }
        return factory;

    }
}
