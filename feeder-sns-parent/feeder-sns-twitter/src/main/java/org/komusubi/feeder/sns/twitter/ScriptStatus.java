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
package org.komusubi.feeder.sns.twitter;

import org.komusubi.feeder.model.ScriptLine;

import twitter4j.Status;


/**
 * @author jun.ozeki
 */
public class ScriptStatus extends ScriptLine {

    private static final long serialVersionUID = 1L;
    private Status status;
    
    public ScriptStatus(Status status) {
        // FIXME set teeded url. how to get from twitter4j's Status object ?
//        status.getURLEntities()
        super(status.getText(), null);
        this.status = status;
    }
}
