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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.komusubi.feeder.model.Message.Script;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author jun.ozeki
 */
public interface Message extends List<Script>, Serializable {

    @JsonProperty String text();
    @JsonProperty Site site();
    void setSite(Site site);
    Date createdAt();
    Message append(Script script);
    Message append(String line);
    
    public interface Script extends Serializable {
        String line();
        String trimedLine();
        boolean isFragment();
        String fragment();
        int codePointCount();
//        codePointAt(int index)
        Script append(String str);
        String codePointSubstring(int begin, int end);
        String codePointSubstring(int begin);
    }


}
