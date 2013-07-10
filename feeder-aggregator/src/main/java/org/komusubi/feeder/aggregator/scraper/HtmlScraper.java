/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.komusubi.feeder.aggregator.scraper;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.http.ConnectionManager;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.ParserFeedback;
import org.komusubi.feeder.aggregator.AggregatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * html scraper.
 * @author jun.ozeki
 */
public class HtmlScraper {
    private Parser parser;
    private boolean cacheable;
    private Map<String, NodeList> cachedNodes;

    /**
     * create new instance.
     */
    public HtmlScraper() {
        this(true);
        parser = newParser();
        cachedNodes = new HashMap<String, NodeList>();
        configure(parser, Parser.getConnectionManager());
    }

    /**
     * create new instance.
     * @param cacheable
     */
    public HtmlScraper(boolean cacheable) {
        this.cacheable = cacheable;
    }

    /**
     * create new parser.
     * for unit test method for override.
     * @return
     */
    Parser newParser() {
        return new Parser();
    }

    /**
     * 
     * @param manager
     */
    protected void configure(Parser parser, ConnectionManager manager) {
        parser.setFeedback(new Slf4jFeedback());
        manager.setMonitor(parser);
        manager.setRedirectionProcessingEnabled(true);
        manager.setCookieProcessingEnabled(true);
    }

    /**
     * scrape html in site.
     * @param resource
     * @param filter
     */
    protected NodeList scrape(String resource, NodeFilter filter) {
        try {
            if (cachedNodes.containsKey(resource))
                return cachedNodes.get(resource);
            // parser can handle html string or URL
            parser.setResource(resource);
            NodeList nodes = parser.parse(filter);
            // FIXME It is not proper cache of Node as parsed resource.
            if (cacheable) {
//                cachedNodes.put(resource, nodes);
            }
            
            return nodes;
        } catch (ParserException e) {
            throw new AggregatorException(e);
        }
    }

    /**
     * scrape.
     * @param url
     * @return
     */
    public NodeList scrape(URL url, NodeFilter filter) {
        return scrape(url.toExternalForm(), filter);
    }

    /**
     * scrape and match node.
     * @param resources html source
     * @param filter filter for html tag class(from html parser library)
     * @param clazz parse node
     * @return
     */
    public NodeList scrapeMatchNodes(String resources, NodeFilter filter, Class<?> clazz) {
        NodeList nodes = scrape(resources, filter);
        return nodes.extractAllNodesThatMatch(new NodeClassFilter(clazz), true);
    }

    /**
     * 
     * @param clazz
     * @return
     */
    public NodeList scrapeMatchNodes(URL url, NodeFilter filter, Class<?> clazz) {
        return scrapeMatchNodes(url.toExternalForm(), filter, clazz);
    }

    /**
     * parser feedback for slf4j.
     * @author jun.ozeki
     */
    private static class Slf4jFeedback implements ParserFeedback {
        private static final Logger logger = LoggerFactory.getLogger(HtmlScraper.class);

        /**
         * @see org.htmlparser.util.ParserFeedback#info(java.lang.String)
         */
        @Override
        public void info(String message) {
            logger.info(message);
        }

        /**
         * @see org.htmlparser.util.ParserFeedback#warning(java.lang.String)
         */
        @Override
        public void warning(String message) {
            logger.warn(message);
        }

        /**
         * @see org.htmlparser.util.ParserFeedback#error(java.lang.String, org.htmlparser.util.ParserException)
         */
        @Override
        public void error(String message, ParserException e) {
            logger.error(message);
        }

    }
}
