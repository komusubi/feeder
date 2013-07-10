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
package org.komusubi.feeder.aggregator;

import java.io.IOException;
import java.io.InputStream;

import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jun.ozeki
 */
public class ExternalFileResource extends ExternalResource {
    private static final Logger logger = LoggerFactory.getLogger(ExternalFileResource.class);
    private Class<?> clazz;
    private String resourceName;
    private InputStream is;
    private String encoding;
    
    /**
     * 
     */
    public ExternalFileResource(Class<?> clazz, String resourceName) {
        this(clazz, resourceName, "utf-8");
    }
    
    public ExternalFileResource(Class<?> clazz, String resourceName, String encoding) {
        this.clazz = clazz;
        this.resourceName = resourceName;
        this.encoding = encoding;
    }
    
    public String getResource() {
        String resources;
        int length = 32768;
        int offset = 0;
        byte[] bytes = new byte[length + 1];
        
        try {
            while (is.read(bytes, offset, bytes.length - offset) >= 0) {
                offset += length;
                if (offset >= bytes.length)
                    throw new StringIndexOutOfBoundsException();
            }
            resources = new String(bytes, encoding);
        } catch (IOException e) {
            resources = "";
            logger.error("resource read:", e);
        }
        return resources;
    }
    
    @Override
    public void before() {
        is = clazz.getResourceAsStream(resourceName);
    }
    
    @Override
    public void after() {
        
        if (is != null) {
            try {
                is.close();
            } catch (IOException ignore) {
                logger.error("resouce close:", ignore);
            }
        }
    }
}
