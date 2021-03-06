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

import java.util.Date;

import org.komusubi.feeder.bind.FeederMessage;

/**
 * @author jun.ozeki
 */
public class StorageMessage extends FeederMessage {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer siteId;
    
    public StorageMessage(Integer id, Date created, Integer siteId) {
        super();
        this.id = id;
        this.siteId = siteId;
        super.initialize(created);
    }

    public Integer id() {
        return id;
    }

    public Integer siteId() {
        return siteId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(super.toString());
        builder.append(", StorageMessage [id=").append(id).append(", siteId=").append(siteId).append("]");
        return builder.toString();
    }
}
