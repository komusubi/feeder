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
package org.komusubi.feeder.storage.cache;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

import org.komusubi.feeder.model.Message;
import org.komusubi.feeder.model.Message.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PartialMatchPageCache extends FilePageCache {

    private static final Logger logger = LoggerFactory.getLogger(PartialMatchPageCache.class);

    /**
     * @param path
     */
    @Inject
    public PartialMatchPageCache(@Named("tweet store file") String path) {
        super(path);
    }

    /**
     * @param file
     */
    public PartialMatchPageCache(File file) {
        super(file);
    }

    /**
     * @see org.komusubi.feeder.sns.twitter.strategy.SleepStrategy.PageCache#exists(org.komusubi.feeder.model.Message)
     */
    @Override
    public boolean exists(Message message) {
        if (message == null || message.size() <= 0)
            return false; // this right ?
        // compare to first script only.
        Script script = message.get(0);
        String comparison;
        if (script.isFragment()) {
            comparison = script.trimedLine().substring(script.fragment().length());
        } else {
            comparison = script.trimedLine();
        }
        for (String item: cache()) {
            if (comparison.equals(item)) {
                logger.info("duplicated message: {}", comparison);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String line(Script script) {
        String line;
        if (script.isFragment()) {
            line = script.trimedLine().substring(script.fragment().length());
        } else {
            line = script.trimedLine();
        }
        return line;
    }
}
