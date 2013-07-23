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
package org.komusubi.feeder.aggregator.topic;

import java.io.Serializable;

import org.komusubi.feeder.model.Message.Script;
import org.komusubi.feeder.model.Region;

/**
 * @author jun.ozeki
 */
public class WeatherScript implements Script {
    /**
     * 
     * @author jun.ozeki
     */
    public static class WeatherStatus implements Serializable {
        private static final long serialVersionUID = 1L;
        private String status;

        /**
         * @param status
         */
        public WeatherStatus(String status) {
            this.status = status;
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            WeatherStatus other = (WeatherStatus) obj;
            if (status == null) {
                if (other.status != null)
                    return false;
            } else if (!status.equals(other.status))
                return false;
            return true;
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((status == null) ? 0 : status.hashCode());
            return result;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("WeatherStatus [status=").append(status).append("]");
            return builder.toString();
        }

        /**
         * 
         * @return
         */
        public String value() {
            return status;
        }
    }

    private static final long serialVersionUID = 1L;
    private Region region;
    private WeatherStatus status;

    /**
     * create new instance.
     * @param region
     * @param status
     */
    public WeatherScript(Region region, WeatherStatus status) {
        this.region = region;
        this.status = status;
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#line()
     */
    @Override
    public String line() {
        if (region == null || status == null)
            return "";
        return region.name() + ": " + status.value();
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#codePointCount()
     */
    @Override
    public int codePointCount() {
        if (line() == null)
            return 0;
        return line().codePointCount(0, line().length());
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#codePointSubstring(int, int)
     */
    @Override
    public String codePointSubstring(int begin, int end) {
        // FIXME code point substring.
        if (line() == null)
            return null;
        return line().substring(begin, end);
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#codePointSubstring(int)
     */
    @Override
    public String codePointSubstring(int begin) {
        if (line() == null)
            return null;
        return codePointSubstring(begin, line().length());
    }

}
