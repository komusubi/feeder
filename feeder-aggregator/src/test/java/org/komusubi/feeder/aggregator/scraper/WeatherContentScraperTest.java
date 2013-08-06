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
package org.komusubi.feeder.aggregator.scraper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.List;

import org.htmlparser.NodeFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableHeader;
import org.htmlparser.util.NodeList;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.komusubi.feeder.aggregator.scraper.WeatherContentScraper.Status;
import org.komusubi.feeder.aggregator.site.WeatherTopicSite;
import org.komusubi.feeder.model.Region;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author jun.ozeki
 */
@RunWith(Theories.class)
public class WeatherContentScraperTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();
    @Mock private HtmlScraper scraper; 
    @DataPoints public static Class<?>[] VALUES = new Class<?>[]{ Region.class, Status.class };
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void スクレイプ処理引数の例外検証() {
        // setup
        WeatherContentScraper target = new WeatherContentScraper();
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("argument MUST be Region.class or WeatherStatus.class");
        // exercise
        target.scrape(String.class);
    }
   
    @Theory
    public void スクレイプ処理引数の検証(Class<?> clazz) throws Exception {
        // setup
        String url = "http://localhost";
        WeatherTopicSite site = new WeatherTopicSite(new URL(url));
        NodeFilter filter = new WeatherContentScraper().filter();
        when(scraper.scrape(url, filter)).thenReturn(null);
        WeatherContentScraper target = new WeatherContentScraper(site, scraper);
        // exercise
        NodeList actual = target.scrape(clazz);
        // verify
        assertThat(actual, is(nullValue()));
    }
    
    @Test
    public void Regionを指定時にTableHeaderでスクレイプ出来る事() throws Exception {
        // setup
        URL url = new URL("http://localhost");
        WeatherTopicSite site = new WeatherTopicSite(url);
        final NodeFilter filter = new WeatherContentScraper().filter();
        // build html node list
        TableHeader header = new TableHeader(); 
        NodeList returnNode = new NodeList(header);
        when(scraper.scrapeMatchNodes(url, filter, TableHeader.class)).thenReturn(returnNode);
        WeatherContentScraper target = new WeatherContentScraper(site, scraper) {
            @Override
            protected NodeFilter filter() {
                return filter;
            }
        };
        // exercise
        NodeList actual = target.scrape(Region.class);
        // verify
        verify(scraper).scrapeMatchNodes(url, filter, TableHeader.class);
        assertThat(actual, is(returnNode));
    }
    
    @Test
    public void WeatherStatus指定時にTableColumnでスクレイプ出来る事() throws Exception {
                // setup
        URL url = new URL("http://localhost");
        WeatherTopicSite site = new WeatherTopicSite(url);
        final NodeFilter filter = new WeatherContentScraper().filter();
        // build html node list
        TableColumn column = new TableColumn();
        NodeList returnNode = new NodeList(column);
        when(scraper.scrapeMatchNodes(url, filter, TableColumn.class)).thenReturn(returnNode);
        WeatherContentScraper target = new WeatherContentScraper(site, scraper) {
            @Override
            protected NodeFilter filter() {
                return filter;
            }
        };
        // exercise
        NodeList actual = target.scrape(Status.class);
        // verify
        verify(scraper).scrapeMatchNodes(url, filter, TableColumn.class);
        assertThat(actual, is(returnNode));
    }
 
    @Test
    public void WeatherStausの値はリターンコードがスペースに変換されている事() throws Exception {
        // setup
        URL url = new URL("http://localhost");
        WeatherTopicSite site = new WeatherTopicSite(url);
        final NodeFilter filter = new WeatherContentScraper().filter();
        NodeList returnNode = new NodeList();
        returnNode.add(new TextNode("value1\nvalue2"));
        returnNode.add(new TextNode("value3\nvalue4\n"));
        String expected1 = "value1 value2"; 
        String expected2 = "value3 value4";
        when(scraper.scrapeMatchNodes(url, filter, TableColumn.class)).thenReturn(returnNode);
        WeatherContentScraper target = new WeatherContentScraper(site, scraper) {
            @Override
            protected NodeFilter filter() {
                return filter;
            }
        };
        // exercise
        List<Status> actual = target.scrapeWeatherStatus();
        // verify
        assertThat(actual.get(0).value(), is(expected1));
        assertThat(actual.get(1).value(), is(expected2));
    }
}
