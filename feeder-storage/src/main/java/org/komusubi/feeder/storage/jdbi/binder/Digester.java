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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.komusubi.feeder.storage.StorageException;

public class Digester {

    public static byte[] sha1(String message) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(message.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new StorageException(e);
        }
    }

    public static String hex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            builder.append(Integer.toHexString(bytes[i] & 0xff));
        }
        return builder.toString();
    }
}

