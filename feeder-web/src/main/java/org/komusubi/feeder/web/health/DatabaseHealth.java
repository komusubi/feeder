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
package org.komusubi.feeder.web.health;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.setup.Environment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.codahale.metrics.health.HealthCheck;

/**
 * @author jun.ozeki
 */
public class DatabaseHealth extends HealthCheck {

    private ManagedDataSource dataSource;

    /**
     * @param dataSourceFactory
     * @param environment
     * @throws ClassNotFoundException 
     */
    public DatabaseHealth(DataSourceFactory dataSourceFactory, Environment environment) throws ClassNotFoundException {
        dataSource = dataSourceFactory.build(environment.metrics(), "database");
    }

    /**
     * @see com.codahale.metrics.health.HealthCheck#check()
     */
    @Override
    protected Result check() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            connection.setReadOnly(true);
            PreparedStatement statement = connection.prepareStatement("");
            try (ResultSet result = statement.executeQuery()) {
                String value = "";
                if (result.next())
                    value = result.getString(1);
                if ("".equals(value))
                    return Result.healthy("");
                else
                    return Result.unhealthy("");
            } catch (SQLException e) {
                throw e;
            }
        } catch (SQLException ex) {
            throw ex;
        } 
    }

}
