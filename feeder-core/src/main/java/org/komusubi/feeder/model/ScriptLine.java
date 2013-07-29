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
package org.komusubi.feeder.model;

import org.komusubi.feeder.model.Message.Script;

/**
 * @author jun.ozeki
 */
public class ScriptLine implements Script {

    private static final long serialVersionUID = 1L;
    private String line;

    /**
     * create new instance.
     */
    public ScriptLine(String line) {
        this.line = line;
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#codePointCount()
     */
    @Override
    public int codePointCount() {
        if (line == null)
            return 0;
        return line.codePointCount(0, line.length());
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#codePointSubstring(int)
     */
    @Override
    public String codePointSubstring(int begin) {
        // FIXME code point substring.
        if (line == null)
            return null;
        return codePointSubstring(begin, line.length());
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#codePointSubstring(int, int)
     */
    @Override
    public String codePointSubstring(int begin, int end) {
        // FIXME code point substring.
        if (line == null)
            return null;
        return line.substring(begin, end);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ScriptLine other = (ScriptLine) obj;
        if (line == null) {
            if (other.line != null)
                return false;
        } else if (!line.equals(other.line))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((line == null) ? 0 : line.hashCode());
        return result;
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#line()
     */
    @Override
    public String line() {
        return line;
    }

}
