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

import org.komusubi.feeder.model.airline.Airline.Code;

/**
 * 
 * @author jun.ozeki 2013/11/28
 */
public class FlightName implements Serializable {
    private static final long serialVersionUID = 1L;
    private Code code;
    private FlightNumber flightNumber;

    public FlightName(Airline.Code code, FlightNumber flightNumber) {
        this.code = code;
        this.flightNumber = flightNumber;
    }
    
    // FIXME fomatter
    public String getFlightName() {
        return code.getCode() + " " + flightNumber.getNumber();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((flightNumber == null) ? 0 : flightNumber.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FlightName other = (FlightName) obj;
        if (code != other.code)
            return false;
        if (flightNumber == null) {
            if (other.flightNumber != null)
                return false;
        } else if (!flightNumber.equals(other.flightNumber))
            return false;
        return true;
    }
    
}
