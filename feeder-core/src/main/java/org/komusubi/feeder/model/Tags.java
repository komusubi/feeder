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

import java.util.Collection;
import java.util.HashSet;

/**
 * @author jun.ozeki
 */
public class Tags extends HashSet<Tag> {

    private static final long serialVersionUID = 1L;
    private static Tags EMPTY = new Tags();

    public static Tags emptyTags() {
        return EMPTY;
    }

    /**
     * create new instance.
     */
    public Tags() {
        super();
    }

    /**
     * create new instance.
     * @param c
     */
    public Tags(Collection<? extends Tag> c) {
        super(c);
    }

    /**
     * create new instance.
     * @param initialCapacity
     */
    public Tags(int initialCapacity) {
        super(initialCapacity);
    }

    /*
     * append tag.
     * @param tag
     * @return
     */
//    public Tags append(Tag tag) {
//        add(tag);
//        return this;
//    }
}
