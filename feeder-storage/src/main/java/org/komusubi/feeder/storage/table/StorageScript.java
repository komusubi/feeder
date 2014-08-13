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
package org.komusubi.feeder.storage.table;

import org.komusubi.feeder.model.Message.Script;
import org.komusubi.feeder.model.Url;

/**
 * @author jun.ozeki
 */
public class StorageScript implements Script {

    private static final long serialVersionUID = 1L;
    private Url url; // TODO refer to tweeted url.

    public StorageScript(Url referUrl) {
        this.url = referUrl;
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#line()
     */
    @Override
    public String line() {
        // TODO get message from twitter url.
        return null;
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#trimedLine()
     */
    @Override
    public String trimedLine() {
        return null;
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#isFragment()
     */
    @Override
    public boolean isFragment() {
        return false;
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#fragment()
     */
    @Override
    public String fragment() {
        return null;
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#codePointCount()
     */
    @Override
    public int codePointCount() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#append(java.lang.String)
     */
    @Override
    public Script append(String str) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#codePointSubstring(int, int)
     */
    @Override
    public String codePointSubstring(int begin, int end) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#codePointSubstring(int)
     */
    @Override
    public String codePointSubstring(int begin) {
        throw new UnsupportedOperationException();
    }

}
