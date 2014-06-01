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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.komusubi.feeder.model.Message;
import org.komusubi.feeder.model.Message.Script;
import org.komusubi.feeder.spi.PageCache;
import org.komusubi.feeder.storage.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jun.ozeki
 */
public class FilePageCache implements PageCache {

    private static final Logger logger = LoggerFactory.getLogger(FilePageCache.class);
    private static final String CHARSET = "UTF-8";
    private File file;
    private List<String> items;
    private String lineSeparator = System.lineSeparator();

    /**
     * create new instance.
     * @param path
     */
    @Inject
    public FilePageCache(@Named("tweet store file") String path) {
        this(new File(path));
    }

    /**
     * 
     * @param file
     */
    public FilePageCache(File file) {
        this.file = file;
        items = new ArrayList<String>();
    }

    /**
     * @see org.komusubi.feeder.sns.twitter.strategy.SleepStrategy.PageCache#refresh()
     */
    @Override
    public void refresh() {
        cache();
        int retainCount = 40;
        if (items.size() <= retainCount)
            return;
        File tmp;
        try {
            tmp = File.createTempFile("feeder-store", ".tmp");
        } catch (IOException e) {
            throw new StorageException(e);
        }

        try (BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(tmp), CHARSET))) {
            for (int i = items.size() - retainCount; items.size() > i; i++) {
                writer.write("tweet:");
                writer.write(items.get(i));
                writer.write(lineSeparator);
            }
        } catch (IOException e) {
            throw new StorageException(e);
        }
        // replace file
        if (System.getProperty("os.name").contains("Windows"))
            file.delete();
        if (!tmp.renameTo(file))
            logger.warn("rename file failure:{}", tmp.getAbsolutePath());
        items.clear();
    }

    public List<String> cache() {

        if (!file.exists() || items.size() > 0)
            return items;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), CHARSET))) {
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("tweet:")) {
                    if (builder.length() > 0) {
                        if (builder.toString().endsWith(lineSeparator))
                            builder.deleteCharAt(builder.length() - 1);
                        items.add(builder.toString());
                    }
                    builder = new StringBuilder(line.substring("tweet:".length()));
                    builder.append(lineSeparator);
                    continue;
                }
                builder.append(line)
                                .append(lineSeparator);
            }
            if (builder.length() > 0) {
                if (builder.toString().endsWith(lineSeparator))
                    builder.deleteCharAt(builder.length() - 1);
                items.add(builder.toString());
            }
        } catch (IOException e) {
            throw new StorageException(e);
        }
        return items;
    }

    protected String strip(String line) {
        return stripSchema(line, "http");
    }

    protected String stripSchema(String line, String schema) {
        if (!line.contains(schema)) 
            return line;
        int start = line.indexOf(schema);
        int finish = line.length();
        for (int index = start; index <= line.length(); index++) {
            if (Character.isWhitespace(line.charAt(index))) {
                finish = index + 1;
                break;
            }
        }
        StringBuilder builder = new StringBuilder(line.substring(0, start));
        builder.append(line.substring(finish));
        return builder.toString();
    }
    
    /**
     * @see org.komusubi.feeder.sns.twitter.strategy.SleepStrategy.PageCache#exists(org.komusubi.feeder.model.Message)
     */
    @Override
    public boolean exists(Message message) {

        // found same script to be tweet and history one.
        for (Iterator<Script> it = message.iterator(); it.hasNext(); ) {
            Script script = it.next();
            String stripped = strip(script.trimedLine());
            for (String item: cache()) {
                if (stripped.equals(strip(item))) {
                    logger.info("duplicated script found: from cache:{}, script:{}", item, script.line());
                    it.remove();
                    break;
                }
            }
        }
        return message.size() <= 0;
    }

    /**
     * @see org.komusubi.feeder.sns.twitter.strategy.SleepStrategy.PageCache#store(org.komusubi.feeder.model.Message)
     */
    @Override
    public void store(Message message) {

        try (BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(file, true), CHARSET))) {
            for (Script script: message) {
                writer.write("tweet:");
//                writer.write(script.trimedLine());
                writer.write(line(script));
                writer.write(lineSeparator);
            }
            items.clear();
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    public String line(Script script) {
        if (script != null)
            return script.trimedLine(); 
        return "";
    }
}
