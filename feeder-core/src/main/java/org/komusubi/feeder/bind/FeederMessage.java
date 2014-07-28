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
package org.komusubi.feeder.bind;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.komusubi.feeder.model.Message;
import org.komusubi.feeder.model.Message.Script;
import org.komusubi.feeder.model.ScriptLine;

/**
 * @author jun.ozeki
 */
public class FeederMessage extends ArrayList<Script> implements Message {

    private static final long serialVersionUID = 1L;
    private Date created;

    /**
     * create new instance.
     * default constructor.
     */
    public FeederMessage() {
        super();
        initialize();
    }

    /**
     * create new instance.
     * @param c
     */
    public FeederMessage(Collection<? extends Script> c) {
        super(c);
        initialize();
    }

    /**
     * create new instance.
     * @param initialCapacity
     */
    public FeederMessage(int initialCapacity) {
        super(initialCapacity);
        initialize();
    }

    private void initialize() {
        initialize(new Date());
    }

    protected void initialize(Date date) {
        this.created = date;
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

    @Override
    public Date createdAt() {
        return created;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FeederMessage [text()=").append(text()).append("]");
        return builder.toString();
    }
    
} 
