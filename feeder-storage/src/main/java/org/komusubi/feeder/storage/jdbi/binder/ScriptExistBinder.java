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

import static org.komusubi.feeder.storage.jdbi.binder.Digester.hex;
import static org.komusubi.feeder.storage.jdbi.binder.Digester.sha1;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.komusubi.feeder.model.Message.Script;
import org.komusubi.feeder.model.ScriptLine;
import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;
/**
 * @author jun.ozeki
 */
@BindingAnnotation(ScriptExistBinder.ScriptExistBinderFactory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface ScriptExistBinder {

    public static class ScriptExistBinderFactory implements BinderFactory {

        /**
         * @see org.skife.jdbi.v2.sqlobject.BinderFactory#build(java.lang.annotation.Annotation)
         */
        @Override
        public Binder<? extends Annotation, Script> build(Annotation annotation) {
            return new Binder<ScriptExistBinder, Script>() {

                @Override
                public void bind(SQLStatement<?> q, ScriptExistBinder bind, Script arg) {
                    q.bind("hash", hex(sha1(arg.line())));
                    if (arg instanceof ScriptLine) {
                        ScriptLine scriptLine = (ScriptLine) arg;
                        if (scriptLine.isUrlResource()) {
                            q.bind("url", scriptLine.getUrl().getOrigin().toExternalForm());
                        } else {
                            q.bind("url", "");
                        }
                    } else {
                        q.bind("url", "");
                    }
                }
                
            };
        }
        
    }
}
