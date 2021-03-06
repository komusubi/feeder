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
package org.komusubi.feeder.storage.jdbi.binder;

import org.komusubi.feeder.model.Message;
import org.skife.jdbi.v2.ContainerBuilder;
import org.skife.jdbi.v2.tweak.ContainerFactory;

/**
 * @author jun.ozeki
 */
public class MessageContainerFactory implements ContainerFactory<Message> {

    /**
     * @see org.skife.jdbi.v2.tweak.ContainerFactory#accepts(java.lang.Class)
     */
    @Override
    public boolean accepts(Class<?> type) {
        return Message.class.isAssignableFrom(type);
    }

    /**
     * @see org.skife.jdbi.v2.tweak.ContainerFactory#newContainerBuilderFor(java.lang.Class)
     */
    @Override
    public ContainerBuilder<Message> newContainerBuilderFor(Class<?> type) {
        return new MessageBuilder();
    }

    private static class MessageBuilder implements ContainerBuilder<Message> {
        private Message m;

        /**
         * @see org.skife.jdbi.v2.ContainerBuilder#add(java.lang.Object)
         */
        @Override
        public ContainerBuilder<Message> add(Object it) {
            m = (Message) it;
            return this;
        }

        /**
         * @see org.skife.jdbi.v2.ContainerBuilder#build()
         */
        @Override
        public Message build() {
            return m;
        }
        
    }
}
