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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.apache.commons.lang3.StringUtils;
import org.komusubi.common.util.Resolver;
import org.komusubi.feeder.model.AbstractScript;
import org.komusubi.feeder.model.Message;
import org.komusubi.feeder.model.Message.Script;
import org.komusubi.feeder.model.ScriptLine;
import org.komusubi.feeder.model.Topic;

import com.twitter.Extractor;
import com.twitter.Extractor.Entity;

/**
 * @author jun.ozeki
 */
public class TweetMessage extends ArrayList<Script> implements Message {

    /**
     * 
     * @author jun.ozeki
     */
    public interface Fragment {
        String get();
    }

    /**
     * 
     * @author jun.ozeki
     */
    public static class TimestampFragment implements Fragment, Serializable {

        private static final long serialVersionUID = 1L;
        private static final Provider<Date> DEFAULT_DATE_PROVIDER = new Provider<Date>() {
            @Override
            public Date get() {
                return new Date();
            }
        };
        private SimpleDateFormat formatter;
        private transient Provider<Date> provider;

        public TimestampFragment(@Named("fragment format") String fragmentFormat) {
            this(fragmentFormat, DEFAULT_DATE_PROVIDER);
        }

        public TimestampFragment(String fragmentFormat, Provider<Date> provider) {
            if (StringUtils.isEmpty(fragmentFormat))
                throw new IllegalArgumentException("argument fragmentFormat MUST not be blank");
            this.formatter = new SimpleDateFormat(fragmentFormat);
            if (provider != null)
                this.provider = provider;
            else
                this.provider = DEFAULT_DATE_PROVIDER;
        }

        @Deprecated
        // ignore argument "resolver" because does not need this method.
        public TimestampFragment(String fragmentFormat, Resolver<Date> resolver) {
            if (fragmentFormat == null || fragmentFormat.length() == 0)
                throw new IllegalArgumentException("arguemnt fragmentFormat MUST not be blank");
            this.formatter = new SimpleDateFormat(fragmentFormat);
            this.provider = DEFAULT_DATE_PROVIDER;
        }

        /**
         * @see org.komusubi.feeder.sns.twitter.TweetMessage.Fragment#get()
         */
        @Override
        public String get() {
            return formatter.format(provider.get());
        }
    }

    /**
     * 
     * @author jun.ozeki
     */
    public static class TweetScript extends AbstractScript {

        private static final long serialVersionUID = 1L;
        private static final int MESSAGE_LENGTH_MAX = 140;
        private StringBuilder line;
        private String fragment;

        /**
         * create new instance.
         * 
         * @param line
         */
        public TweetScript(String line) {
            this(null, line);
        }

        /**
         * create new instance
         * 
         * @param fragment
         * @param line
         */
        public TweetScript(Fragment fragment, String line) {
            if (line == null)
                throw new Twitter4jException("line must NOT be null");
            if (line.codePointCount(0, line.length()) > MESSAGE_LENGTH_MAX) {
                int length = line.codePointCount(0, line.length());
                throw new Twitter4jException("over max length of line: "
                                + length);
            }
            if (fragment != null) {
                this.fragment = fragment.get() + "\n";
                // length check again.
                int length = line.codePointCount(0, line.length())
                                + this.fragment.codePointCount(0,
                                                this.fragment.length());
                if (length > MESSAGE_LENGTH_MAX) {
                    throw new Twitter4jException(
                                    "over max length of line and fragment: " + length);
                }
                this.line = new StringBuilder(this.fragment);
                this.line.append(line);
            } else {
                this.line = new StringBuilder(line);
            }
        }

        public TweetScript append(String buffer) {
            if (this.lengthAfterTweeted() + lengthAfterTweeted(buffer) > TweetScript.MESSAGE_LENGTH_MAX) {
                throw new Twitter4jException(String.format(
                                "max length(%d) over(now:%d append length:%d): %s ",
                                TweetScript.MESSAGE_LENGTH_MAX,
                                this.lengthAfterTweeted(), lengthAfterTweeted(buffer),
                                buffer));
            }
            line.append(buffer);
            return this;
        }

        @Override
        public String fragment() {
            return this.fragment;
        }

        @Override
        public boolean isFragment() {
            return fragment != null;
        }

        public int lengthAfterTweeted() {
            return lengthAfterTweeted(line());
        }

        public static int lengthAfterTweeted(String text) {
            if (text == null || text.isEmpty())
                return 0;
            Extractor extractor = new Extractor();
            List<Entity> entities = extractor.extractURLsWithIndices(text);
            // TODO t.co shorten url length is size 1 longer than bit.ly one.
            // (2013.10.15 now.)
            // if you want correctly attribute value,
            // refer to
            // https://dev.twitter.com/docs/api/1.1/get/help/configuration
            return text.codePointCount(0, text.length()) + entities.size();
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
            // FIXME consider codepoint
            if (begin > line.length())
                throw new StringIndexOutOfBoundsException(
                                "wrong index size: argument is " + begin
                                                + " but actual " + line.length());
            return line.substring(begin);
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
    private Fragment fragment;
    private Topic topic;

    /**
     * create new instance.
     */
    public TweetMessage() {
        this(null);
    }

    /**
     * create new instance. default constructor.
     */
    @Inject
    public TweetMessage(Fragment fragment) {
        this.fragment = fragment;
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
    public boolean addAll(Collection<? extends Script> c) {
        for (Script s: c)
            add(s);
        return true;
    }

    @Override
    public boolean add(Script script) {
        if (script instanceof TweetScript) {
            super.add(script);
        } else {
            String line = script.line();
            // FIXME code point count
            if (TweetScript.lengthAfterTweeted(line) > TweetScript.MESSAGE_LENGTH_MAX) {
                // lengthAfterTweeted size different from script.codePointCount by url shortening.
                // adjust position when match max length.
                int adjust = script.codePointCount() == TweetScript.MESSAGE_LENGTH_MAX ? 1 : 0;
                // edit value to 140 character
                for (int position = TweetScript.MESSAGE_LENGTH_MAX - adjust; position >= 0; position--) {
                    if (line.codePointAt(position) != '\n')
                        continue;
                    super.add(new TweetScript(fragment, line.substring(0, position)));
                    this.add(new ScriptLine(line.substring(position + 1))); // call recursively.
                    return true;
                }
                // not found line feed '\n'
                int offset = 0;
                for (; TweetScript.lengthAfterTweeted(line.substring(offset)) > TweetScript.MESSAGE_LENGTH_MAX; offset += TweetScript.MESSAGE_LENGTH_MAX) {
                    super.add(new TweetScript(fragment, line.substring(offset, TweetScript.MESSAGE_LENGTH_MAX)));
                }
                if (line.length() - offset > 0)
                    super.add(new TweetScript(fragment, line.substring(offset)));
            } else {
                super.add(script);
            }
        }
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
        if (TweetScript.lengthAfterTweeted(line) > TweetScript.MESSAGE_LENGTH_MAX) {
            // FIXME consider code point and word wrap.
            int offset = 0;
            for (; TweetScript.lengthAfterTweeted(line.substring(offset)) > TweetScript.MESSAGE_LENGTH_MAX; offset += TweetScript.MESSAGE_LENGTH_MAX) {
                super.add(new TweetScript(fragment, line.substring(offset,
                                TweetScript.MESSAGE_LENGTH_MAX)));
            }
            // append remain
            if (line.length() - offset > 0) {
                super.add(new TweetScript(fragment, line.substring(offset)));
            }
        } else {
            // try append latest script object.
            if (size() > 0) {
                AbstractScript latest = (AbstractScript) get(size() - 1);
                if (TweetScript.lengthAfterTweeted(latest.line())
                                + TweetScript.lengthAfterTweeted(line) <= TweetScript.MESSAGE_LENGTH_MAX) {
                    latest.append(line);
                } else {
                    super.add(new TweetScript(fragment, line));
                }
            } else {
                super.add(new TweetScript(fragment, line));
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
    public Topic topic() {
        return this.topic;
    }

    @Override
    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TweetMessage [fragment=").append(fragment).append("]");
        builder.append(super.toString());
        return builder.toString();
    }

}
