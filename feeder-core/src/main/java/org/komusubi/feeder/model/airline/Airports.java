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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.komusubi.feeder.model.airline.Airport.Attribute;

/**
 * 
 * @author jun.ozeki 2013/11/27
 */
public class Airports extends ArrayList<Airport> {
    private static final long serialVersionUID = 1L;
    private Map<String, Airport> map;

    public Airports() {
    }

    public Airports(Collection<Airport> c) {
        super(c);
    }

    public Airports findByAttribute(Attribute attr) {
        Airports result = new Airports();
        for (Airport a: this) {
            if (attr.equals(a.getAttribute()))
                result.add(a);
        }
        return result;
    }

    @Deprecated 
    // multi attribute can not handle. ex) CTS, FUK... 
    public Map<String, Airport> getMap() {
        if (map != null)
            return map;
        Map<String, Airport> map = new HashMap<String, Airport>();
        for (Airport a: this) {
            map.put(a.code(), a);
        }
        return map;
    }
    
    @Deprecated
    public Airport getAirport(String code) {
        return getMap().get(code);
    }

}
