package org.komusubi.feeder.model.airline;

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
import java.util.Date;

import javax.inject.Provider;

import org.komusubi.feeder.utils.Providers;

/**
 * 
 * @author jun.ozeki 2013/11/27
 */
public class Duration {

    private Date departure;
    private Date arrival;

    public Duration() {
        this(Providers.DefaultDate, Providers.DefaultDate);
    }

    public Duration(Provider<Date> departure, Provider<Date> arrival) {
        this.departure = departure.get();
        this.arrival = arrival.get();
    }

    public Date getDeparture() {
        return departure;
    }
    
    public Date getArrival() {
        return arrival;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Schedule [arrival=").append(arrival).append(", departure=").append(departure).append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((arrival == null) ? 0 : arrival.hashCode());
        result = prime * result + ((departure == null) ? 0 : departure.hashCode());
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
        Duration other = (Duration) obj;
        if (arrival == null) {
            if (other.arrival != null)
                return false;
        } else if (!arrival.equals(other.arrival))
            return false;
        if (departure == null) {
            if (other.departure != null)
                return false;
        } else if (!departure.equals(other.departure))
            return false;
        return true;
    }
    
}
