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
public class Airline implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Code {
        JAL("JAL", "JL"),
        JTA("JTA", "unknown"),
        RAC("RAC", "unknown"),
        JAC("JAC", "unknown");
        
        private String code;
        private String abbreviation;

        public static Code findByCode(String code) {
            if (code == null)
                throw new IllegalArgumentException("code is must not null");
            Code carrierCode = null;
            for (Code c: values()) {
                if (code.equals(c.getCode())) {
                    carrierCode = c;
                    break;
                }
            }
            if (carrierCode == null)
                throw new IllegalArgumentException("unknown code: " + code);
            return carrierCode;
        }

        public static Code findByAbbreviation(String abbreviation) {
            if (abbreviation == null)
                throw new IllegalArgumentException("abbreviation is must not null");
            Code carrierCode = null;
            for (Code c: values()) {
                if (abbreviation.equals(c.getAbbriviation())) {
                    carrierCode = c;
                    break;
                }
            }
            if (carrierCode == null)
                throw new IllegalArgumentException("unknown abbreviaion code: " + abbreviation);
            return carrierCode;
        }

        public String getCode() {
            return code;
        }
        
        public String getAbbriviation() {
            return abbreviation;
        }

        private Code(String code, String abbreviation) {
            this.code = code;
            this.abbreviation = abbreviation;
        }
    }

    private Airline.Code airlineCode;
    private String name;

    public Airline(Airline.Code code, String name) {
        this.airlineCode = code;
        this.name = name;
    }

    public Airline.Code getCode() {
        return airlineCode;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Airline [airlineCode=").append(airlineCode).append(", name=").append(name).append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((airlineCode == null) ? 0 : airlineCode.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Airline other = (Airline) obj;
        if (airlineCode != other.airlineCode)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
