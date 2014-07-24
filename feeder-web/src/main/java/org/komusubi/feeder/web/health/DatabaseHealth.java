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

import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.setup.Environment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.komusubi.feeder.web.FeederConfiguration;

import com.codahale.metrics.health.HealthCheck;

/**
 * @author jun.ozeki
 */
public class DatabaseHealth extends HealthCheck {

    private ManagedDataSource dataSource;
    private String expect;
    private String query;

    /**
     * @param configuration
     * @param environment
     * @throws ClassNotFoundException 
     */
    public DatabaseHealth(FeederConfiguration configuration, Environment environment) throws ClassNotFoundException {
        this.dataSource = configuration.getDataSourceFactory().build(environment.metrics(), "database-health");
        this.query = configuration.getQuery();
        this.expect = configuration.getExpect();
    }

    /**
     * @see com.codahale.metrics.health.HealthCheck#check()
     */
    @Override
    protected Result check() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            connection.setReadOnly(true);
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet result = statement.executeQuery()) {
                    String value = "";
                    if (result.next())
                        value = result.getString(1);
                    if (value.equals(expect))
                        return Result.healthy("success query: " + query);
                    else
                        return Result.unhealthy("failuer query: " + query);
                } catch (SQLException e) {
                    throw e;
                }
            } catch (SQLException ex) {
                throw ex; 
            }
        } catch (SQLException exn) {
            throw exn;
        } 
    }

}
