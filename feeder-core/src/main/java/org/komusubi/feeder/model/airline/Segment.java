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
 * @author jun.ozeki 2013/11/28
 */
public interface Segment extends Serializable {
    /**
     * node type.
     * @author jun.ozeki 2013/11/27
     */
    public enum Type {
        SCHEDULED, // scheduled leg
        ESTIMATED, // before departure leg
        ACTUAL,    // after departure leg (include irregular node)
        VIRTUAL;   // virtual node ex) "ARUNK"...
    }

    public enum Status {
        
    }
    Route getRoute();
    FlightName getFlightName();
    Type getType();
    boolean isIrregular();
}
