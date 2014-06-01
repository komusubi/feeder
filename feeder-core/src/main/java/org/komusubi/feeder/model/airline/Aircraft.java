/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.komusubi.feeder.model.airline;

import java.io.Serializable;

/**
 * 
 * @author jun.ozeki 2013/11/27
 */
public class Aircraft implements Serializable {
    private static final long serialVersionUID = 1L;
    // TODO aircraft code abbreviation ? (IATA?)
    private String code;
    private String name;

    public Aircraft(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Aircraft [code=").append(code).append(", name=").append(name).append("]");
        return builder.toString();
    }
    
}
    
