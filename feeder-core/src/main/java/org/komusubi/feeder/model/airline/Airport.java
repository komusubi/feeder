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
import java.util.List;

/**
 * 
 * @author jun.ozeki 2013/11/27
 */
public class Airport implements Serializable {
    private static final long serialVersionUID = 1L;

    @Deprecated
    public enum Attribute {
        PRIMARY("2"),
        MULTI("1"),
        DEFAULT("0");
        
        private String value;

        public static Attribute findByAttribute(String attr) {
            if (attr == null || attr.isEmpty())
                throw new IllegalArgumentException("attr is wrong." );
            Attribute attribute = null;
            for (Attribute a: values()) {
                if (attr.equals(a.getValue())) {
                    attribute = a;
                    break;
                }
            }
            if (attribute == null)
                throw new IllegalArgumentException("not found attribute: " + attr);
            return attribute;
        }

        public String getValue() {
            return value;
        }

        private Attribute(String value) {
            this.value = value;
        }
    }

    /**
     * 
     * @author jun.ozeki 2013/11/27
     */
    public static class Gate implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;

        public Gate(String name) {
            this.name = name;
        }

        public String name() {
            return name;
        }
    }

    private String name;
    private String code;
    private Attribute attribute;
    private List<Gate> gates;
    private Airports branches;
    
    public Airport(String code, String name) {
        this(code, name, Attribute.DEFAULT);
    }

    public Airport(String code, String name, Attribute attr) {
        this.code = code;
        this.name = name;
        this.attribute = attr;
        this.branches = new Airports();
    }

    public Attribute getAttribute() {
        return attribute;
    }
  
    public String name() {
        return name;
    }

    public String code() {
        return code;
    }

    public Airport setGates(List<Gate> gates) {
        this.gates = gates;
        return this;
    }

    public List<Gate> getGates() {
        return gates;
    }

    @Deprecated
    public boolean isMulti() {
        return branches.size() > 1;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Airport [name=").append(name).append(", code=").append(code).append(", attribute=")
                        .append(attribute).append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attribute == null) ? 0 : attribute.hashCode());
        result = prime * result + ((code == null) ? 0 : code.hashCode());
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
        Airport other = (Airport) obj;
        if (attribute != other.attribute)
            return false;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
