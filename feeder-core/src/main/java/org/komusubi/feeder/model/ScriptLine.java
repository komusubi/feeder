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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.nio.charset.Charset;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

import org.komusubi.feeder.FeederException;

/**
 * @author jun.ozeki
 */
public class ScriptLine extends AbstractScript {

    private static final long serialVersionUID = 1L;
    private String line;
    private Url url;

    /**
     * create new instance.
     */
    @Deprecated
    public ScriptLine(String line) {
        this.line = line;
    }
    
    /**
     * @param line
     * @param url
     */
    public ScriptLine(String line, Url url) {
        if (line == null && url == null)
            throw new IllegalArgumentException("argument must NOT be null");
        this.line = line;
        this.url = url;
    }

    public boolean isUrlResource() {
        return url != null;
    }

    public Url getUrl() {
        return url;
    }

    protected synchronized String getLine() {
        if (line == null && url != null)
            line = getResource(url);
        return line;
    }
    
    private String getResource(Url url) {
        try {
            // TODO if url shortend by bitly web service, must follow redirect.
            URLConnection con = url.openConnection();
            con.connect();
            MimeType mimeType = new MimeType(con.getContentType());
            Charset charset = Charset.forName(mimeType.getSubType());
            StringBuilder builder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), charset))) {
                String line = "";
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                return builder.toString();
            }
        } catch (IOException | MimeTypeParseException e) {
            throw new FeederException(e);
        }
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#append(java.lang.String)
     */
    @Override
    public ScriptLine append(String str) {
        getLine();
        line += str;
        return this;
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#codePointCount()
     */
    @Override
    public int codePointCount() {
        String text = getLine();
        if (text == null)
            return 0;
        return text.codePointCount(0, text.length());
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#codePointSubstring(int)
     */
    @Override
    public String codePointSubstring(int begin) {
        // FIXME code point substring.
        String text = getLine();
        if (text == null)
            return null;
        return codePointSubstring(begin, text.length());
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#codePointSubstring(int, int)
     */
    @Override
    public String codePointSubstring(int begin, int end) {
        // FIXME code point substring.
        String text = getLine();
        if (text == null)
            return null;
        return text.substring(begin, end);
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#line()
     */
    @Override
    public String line() {
        return getLine();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ScriptLine [line=").append(line).append(", url=").append(url).append("]");
        return builder.toString();
    }
}
