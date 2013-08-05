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

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.komusubi.feeder.model.AbstractScript;
import org.komusubi.feeder.model.Message;
import org.komusubi.feeder.model.Message.Script;

/**
 * @author jun.ozeki
 */
public class TweetMessage extends ArrayList<Script> implements Message {

    /**
     * 
     * @author jun.ozeki
     */
    public static class TweetScript extends AbstractScript {

        private static final long serialVersionUID = 1L;
        private static final int MESSAGE_LENGTH_MAX = 140;
        private StringBuilder line;

        /**
         * create new instance.
         * @param line
         */
        public TweetScript(String line) {
            if (line == null)
                throw new Twitter4jException("line must NOT be null");
            if (line != null && line.codePointCount(0, line.length()) > MESSAGE_LENGTH_MAX) {
                int length = line == null ? 0 : line.codePointCount(0, line.length());
                throw new Twitter4jException("over max length of line: " + length);
            }
            this.line = new StringBuilder(line);
        }

        public TweetScript append(String buffer) {
            line.append(buffer);
            return this;
        }

        @Override
        public int codePointCount() {
            return codePointCount(0, line.length());
        }
        
        public int codePointCount(int start, int end) {
            if (line == null)
                return 0;
            return line.codePointCount(start, end);
        }

        @Override
        public String codePointSubstring(int begin) {
            throw new UnsupportedOperationException("not implemented.");
        }

        @Override
        public String codePointSubstring(int begin, int end) {
            throw new UnsupportedOperationException("not implemented.");
        }

        @Override
        public String line() {
            return line.toString();
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("TweetScript [line=").append(line).append("]");
            return builder.toString();
        }

    }

    private static final long serialVersionUID = 1L;

    /**
     * create new instance.
     * default constructor.
     */
    public TweetMessage() {

    }

    @Override
    public boolean addAll(Collection<? extends Script> c) {
        for (Script s: c)
            append(s);
        return true;
    }

    /**
     * @see org.komusubi.feeder.model.Message#append(org.komusubi.feeder.model.Message.Script)
     */
    @Override
    public Message append(Script script) {
        append(script.line());
        return this;
    }

    @Override
    public boolean add(Script script) {
        append(script);
        return true;
    }

    /**
     * @see org.komusubi.feeder.model.Message#append(java.lang.String)
     */
    @Override
    public Message append(String line) {
        if (line == null)
            throw new Twitter4jException("line must NOT be null");
        // line over max size
        if (line.codePointCount(0, line.length()) > TweetScript.MESSAGE_LENGTH_MAX) {
            // FIXME consider code point and word wrap.
            int offset = 0;
            for ( ; 
                  line.codePointCount(offset, line.length()) > TweetScript.MESSAGE_LENGTH_MAX; 
                  offset += TweetScript.MESSAGE_LENGTH_MAX) {
                super.add(new TweetScript(line.substring(offset, TweetScript.MESSAGE_LENGTH_MAX)));
            }
            // append remain 
            if (line.length() - offset > 0)
                super.add(new TweetScript(line.substring(offset)));
            
        } else {
            // try append latest script object.
            if (size() > 0) {
                AbstractScript latest = (AbstractScript) get(size() - 1);
                if (latest.codePointCount() + line.codePointCount(0, line.length()) <= TweetScript.MESSAGE_LENGTH_MAX) {
                    latest.append(line);
                } else {
                    super.add(new TweetScript(line));
                }
            } else {
                super.add(new TweetScript(line));
            }
        }
        return this;
    }

    /**
     * @see org.komusubi.feeder.model.Message#text()
     */
    @Override
    public String text() {
        StringBuilder builder = new StringBuilder();
        for (Script script: this) {
           builder.append(script.line()); 
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TweetMessage [text()=").append(text()).append("]");
        return builder.toString();
    }

}
