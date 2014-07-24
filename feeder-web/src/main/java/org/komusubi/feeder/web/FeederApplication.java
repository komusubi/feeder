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
package org.komusubi.feeder.web;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.komusubi.feeder.web.resource.FeederResource;

/**
 *
 * @author jun.ozeki
 */
public class FeederApplication extends Application<FeederConfiguration> {

    /**
     * application main method. 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new FeederApplication().run(args);
    }
     
    @Override
    public String getName() {
        return "Feeder"; 
    }

    /**
     * @see io.dropwizard.Application#initialize(io.dropwizard.setup.Bootstrap)
     */
    @Override
    public void initialize(Bootstrap<FeederConfiguration> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<FeederConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(FeederConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    /**
     * @see io.dropwizard.Application#run(io.dropwizard.Configuration, io.dropwizard.setup.Environment)
     */
    @Override
    public void run(FeederConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new FeederResource());
    }
}

