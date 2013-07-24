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

import org.komusubi.feeder.aggregator.topic.WeatherTopic;
import org.komusubi.feeder.model.Message;
import org.komusubi.feeder.web.Bootstrap.Jal5971Module;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author jun.ozeki
 */
public class StandAlone {


    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new Jal5971Module());
        new StandAlone().execute(injector);
    }

    /**
     * @param injector
     */
    private void execute(Injector injector) {
        WeatherTopic topic = injector.getInstance(WeatherTopic.class);
        Message message = topic.message();
        System.out.printf("message: %s\n", message.text());
    }

}
