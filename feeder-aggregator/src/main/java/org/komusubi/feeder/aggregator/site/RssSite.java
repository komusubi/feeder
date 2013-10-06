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
package org.komusubi.feeder.aggregator.site;

import java.util.Arrays;

import org.komusubi.feeder.model.Site;
import org.komusubi.feeder.model.Tag;
import org.komusubi.feeder.model.Tags;
import org.komusubi.feeder.model.Url;
import org.komusubi.feeder.utils.ResourceBundleMessage;

/**
 * @author jun.ozeki
 */
public class RssSite implements Site {

    private static final ResourceBundleMessage RESOURCE = new ResourceBundleMessage(RssSite.class);
    private Url url;
    private Tags tags;
                    
    /**
     * 
     */
    public RssSite(String resourceKey) {
        this(new Url(RESOURCE.getString(resourceKey)));
    }

    /**
     * @param url
     */
    public RssSite(Url url, Tag... tags) {
        this.url = url;
        this.tags = new Tags();
        this.tags.addAll(Arrays.asList(tags));
    }

    /**
     * @see org.komusubi.feeder.model.Site#url()
     */
    @Override
    public Url url() {
        return url;
    }

    /**
     * @see org.komusubi.feeder.model.Site#tags()
     */
    @Override
    public Tags tags() {
        return tags;
    }

}
