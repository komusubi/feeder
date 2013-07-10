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

import java.util.ArrayList;
import java.util.Collection;

import org.komusubi.feeder.model.Message.Script;

/**
 * @author jun.ozeki
 */
public class FeederMessage extends ArrayList<Script> implements Message {

    private static final long serialVersionUID = 1L;

    /**
     * create new instance.
     * default constructor.
     */
    public FeederMessage() {
        super();
    }

    /**
     * create new instance.
     * @param c
     */
    public FeederMessage(Collection<? extends Script> c) {
        super(c);
    }

    /**
     * create new instance.
     * @param initialCapacity
     */
    public FeederMessage(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * @see org.komusubi.feeder.model.Message#text()
     */
    public String text() {
        StringBuilder builder = new StringBuilder();
        for (Script script: this) {
            builder.append(script.line());
        }
        return builder.toString();
    }

    /**
     * @see org.komusubi.feeder.model.Message#append(org.komusubi.feeder.model.Message.Script)
     */
    @Override
    public FeederMessage append(Script script) {
        super.add(script);
        return this;
    }

    /**
     * 
     */
    @Override
    public FeederMessage append(String line) {
        return append(new ScriptLine(line));
    }
}