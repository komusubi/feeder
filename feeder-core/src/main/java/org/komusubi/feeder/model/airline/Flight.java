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
import java.util.ArrayList;

import org.komusubi.feeder.model.airline.Segment.Type;

/**
 *
 * @author jun.ozeki 2013/11/27
 */
public class Flight extends Tree<Segment> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Route route;

    public Flight(Route route) {
        this.route = route;
    }
    
    /**
     * return true: this flight is multi-leg.
     * @return
     */
    public boolean isMultiLeg() {
        return next();
    }

    // FIXME how to get kind of segments.
    public Segment[] getSegments(Segment.Type type) {
        ArrayList<Segment> list = new ArrayList<Segment>();
        rewind();
        switch (type) {
        case SCHEDULED:
            // nothing to do.
            break;
        case ESTIMATED:
            right();
            break;
        case ACTUAL:
            right();
            right(); 
            break;
        default:
            throw new UnsupportedOperationException("unknown type: " + type);
        }
        while (next()) {
            list.add(getElement());
        }
        return list.toArray(new Segment[0]);
    }

    public boolean isIrregular() {
        for (Segment s: getSegments(Type.ACTUAL)) {
            if (s.isIrregular())
                return true;
        }
        return false;
    }

    public Route getRoute() {
        return route;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Flight [route=").append(route).append("]");
        return builder.toString();
    }

}
