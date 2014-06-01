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

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.inject.Inject;

import org.komusubi.feeder.FeederException;
import org.komusubi.feeder.spi.UrlShortening;

/**
 * @author jun.ozeki
 */
public class Url implements Serializable {

    private static final long serialVersionUID = 1L;
    private URL url;
    private UrlShortening shortening;
    private boolean shortened;

    public Url(URL url, UrlShortening shortening) {
        this.url = url;
        this.shortening = shortening;
    }

    @Inject
    public Url(String url, UrlShortening shortening) {
        try {
            this.url = new URL(url);
            this.shortening = shortening;
        } catch (MalformedURLException e) {
            throw new FeederException(e);
        }
    }

    public String getQuery() {
        return url.getQuery();
    }

    public String getPath() {
        return url.getPath();
    }

    public String getUserInfo() {
        return url.getUserInfo();
    }

    public String getAuthority() {
        return url.getAuthority();
    }

    public int getPort() {
        return url.getPort();
    }

    public int getDefaultPort() {
        return url.getDefaultPort();
    }

    public String getProtocol() {
        return url.getProtocol();
    }

    public String getHost() {
        return url.getHost();
    }

    public String getFile() {
        return url.getFile();
    }

    public String getRef() {
        return url.getRef();
    }

    @Override
    public boolean equals(Object obj) {
        try {
            if (obj instanceof Url) {
                // fix findbug report, URL#equals/hashCode blocking domain name resolution.(too slow)
                // @see http://findbugs.sourceforge.net/bugDescriptions.html#DMI_BLOCKING_METHODS_ON_URL
                URI uri = ((Url) obj).toURI();
                return this.url.toURI().equals(uri);
            } else {
                return false;
            }
        } catch (URISyntaxException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (shortened ? 1231 : 1237);
        result = prime * result + ((shortening == null) ? 0 : shortening.hashCode());
        try {
            // fix findbug report, URL#equals/hashCode blocking domain name resolution.(too slow)
            // @see http://findbugs.sourceforge.net/bugDescriptions.html#DMI_BLOCKING_METHODS_ON_URL
            result = prime * result + ((url == null) ? 0 : url.toURI().hashCode());
        } catch (URISyntaxException ignore) {
        }
        return result;
    }

    public boolean sameFile(URL other) {
        return url.sameFile(other);
    }

    public String toString() {
        return url.toString();
    }

    public String toExternalForm() {
        return url.toExternalForm();
    }

    public URI toURI() throws URISyntaxException {
        return url.toURI();
    }

    public URLConnection openConnection() throws IOException {
        return url.openConnection();
    }

    public URLConnection openConnection(Proxy proxy) throws IOException {
        return url.openConnection(proxy);
    }

    public final InputStream openStream() throws IOException {
        return url.openStream();
    }

    public final Object getContent() throws IOException {
        return url.getContent();
    }

    public final Object getContent(@SuppressWarnings("rawtypes") Class[] classes) throws IOException {
        return url.getContent(classes);
    }

    public boolean isShortened() {
        return shortened;
    }

    /**
     * @see org.komusubi.feeder.spi.UrlShortening#shorten()
     */
    public Url shorten() {
        if (isShortened())
            return this;
        this.url = shortening.shorten(this.url);
        shortened = true;
        return this;
    }

    /**
     * @return
     */
    public URL toURL() {
        return url;
    }
    
}
