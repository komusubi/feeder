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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URL;

import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.util.NodeList;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.komusubi.feeder.aggregator.ExternalFileResource;
import org.komusubi.feeder.aggregator.scraper.WeatherTitleScraper.WeatherTitleVisitor;
import org.komusubi.feeder.spi.UrlShortening;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author jun.ozeki
 */
@RunWith(Enclosed.class)
public class WeatherTitleScraperTest {

    /**
     * visit all node in weather_info_dom.html.
     * @author jun.ozeki
     */
    public static class WeatherInfoDomHtmlTest {
        @Rule public ExternalFileResource fileResource = new ExternalFileResource(WeatherTitleScraper.class,
                        "weather_info_dom.html", "Shift_JIS");
        @Rule public ExternalFileResource noRegionStatus = new ExternalFileResource(WeatherTitleScraper.class,
                        "no_region_and_status.html", "Shift_JIS");
        @Mock private UrlShortening urlShortening;

        @Before
        public void before() {
            MockitoAnnotations.initMocks(this);
        }
        
        @Test
        public void 運行概況タイトル取得() throws Exception {
            // setup
            String resources = fileResource.getResource();
            String in = "http://www.5971.jal.co.jp/rsv/ArrivalAndDepartureInput.do";
            String shortened = "http://bit.ly/shorten";
            URL out = new URL(shortened);
            when(urlShortening.shorten(in)).thenReturn(out);

            HtmlScraper scraper = new HtmlScraper(true, urlShortening);
            NodeList nodes = scraper.scrapeMatchNodes(resources, new WeatherTitleScraper().filter(), ParagraphTag.class); 
            NodeList actual = new NodeList();
            WeatherTitleVisitor target = new WeatherTitleVisitor(actual, scraper);
            String expected1 = "新年明けましておめでとうございます。";
            String expected2 = "2013年も、日本航空をご愛顧賜りますよう、どうぞ宜しくお願い申し上げます。";
            String expected3 = "≪北海道・東北地方　降雪による運航便情報について≫";
            String expected4 = "明日2日は、北海道・東北地方に降雪の予報がでており、各空港を発着する運航便への影響が懸念されております。";
            String expected5 = "なお、札幌千歳空港を夕刻以降に発着する運航便につきましては、降雪が強まる見込みのため影響が発生する可能性がございます。";
            String expected6 = "利用のお客さまは、お出かけ前に最新の運航状況を";
            String expected7 = shortened;
            String expected8 = "にてご確認ください。";
            String expected9 = "また、本日1日および明日2日の遅延、欠航、条件付運航（出発空港への引き返し、他空港への着陸）の可能性がある空港は、以下のとおりです。";
            // exercise
            nodes.visitAllNodesWith(target);
            // verify
            assertThat(actual, is(not(nullValue())));
            assertThat(actual.size(), equalTo(9));
            assertThat(actual.elementAt(0).getText(), equalTo(expected1));
            assertThat(actual.elementAt(1).getText(), equalTo(expected2));
            assertThat(actual.elementAt(2).getText(), equalTo(expected3));
            assertThat(actual.elementAt(3).getText(), equalTo(expected4));
            assertThat(actual.elementAt(4).getText(), equalTo(expected5));
            assertThat(actual.elementAt(5).getText(), equalTo(expected6));
            assertThat(actual.elementAt(6).getText(), equalTo(expected7));
            assertThat(actual.elementAt(7).getText(), equalTo(expected8));
            assertThat(actual.elementAt(8).getText(), equalTo(expected9));
            verify(urlShortening, times(1)).shorten(in);
        }
        
        @Test
        public void scrapedFooter() throws Exception {
            // setup
            String resources = noRegionStatus.getResource();
            String firstUrl = "http://weather.jal.co.jp/typh/index.html";
            String secondUrl = "https://fltinfo.5971.jal.co.jp/rsv/ArrivalAndDepartureInput.do";
            URL first = new URL("http://bit.ly/firstUrl");
            URL second = new URL("http://bit.ly/secondUrl");
            when(urlShortening.shorten(firstUrl)).thenReturn(first);
            when(urlShortening.shorten(secondUrl)).thenReturn(second);
            
            HtmlScraper scraper = new HtmlScraper(true, urlShortening);
            NodeList nodes = scraper.scrapeMatchNodes(resources, new WeatherTitleScraper().filter(), ParagraphTag.class);
            NodeList actual = new NodeList();
            WeatherTitleVisitor target = new WeatherTitleVisitor(actual, scraper);
            String expected1 = "≪台風15号の影響による運航便情報について≫";
            String expected2 = "http://bit.ly/firstUrl";
            String expected3 = "は、日本時間30日3時現在、東シナ海をゆっくりと北東に進んでいます。";
            String expected4 = "本日30日のJALグループ国内線運航便につきましては、現在のところ平常どおりの運航を予定しております。";
            String expected5 = "なお、今後の台風の進路によっては、状況が変わる可能性もございます。";
            String expected6 = "ご利用のお客さまは、最新の運航状況を当ページや";
            String expected7 = "http://bit.ly/secondUrl";
            String expected8 = "にてご確認ください。";
            String expected9 = "今後も状況が変わり次第、当ページにて最新情報をご案内させていただきます。";
            String expected10 = "また、本日30日の遅延、欠航、条件付運航(出発空港への引き返し、他空港への着陸)の可能性がある空港は、以下のとおりです。";
            String expected11 = "出雲・隠岐（視界不良）";
            String expected12 = "その他は、平常どおりの運航を予定しています。";
            String expected13 = "※上記に空港名の記載がありましても、条件付運航のご案内は、運航する機種によって異なる場合があります。";
            String expected14 = "※このページは､機材故障および使用機材の到着遅れによる遅延･欠航などや、他社運航のコードシェア便の情報は含まれておりません｡";
            String expected15 = "ご利用便の発着状況については、";
            String expected16 = "http://bit.ly/secondUrl";
            String expected17 = "をご確認ください。";
            
            // exercise
            nodes.visitAllNodesWith(target);
            
            // verify
            assertThat(actual, is(not(nullValue())));
            assertThat(actual.size(), equalTo(17));
            assertThat(actual.elementAt(0).getText(), equalTo(expected1));
            assertThat(actual.elementAt(1).getText(), equalTo(expected2));
            assertThat(actual.elementAt(2).getText(), equalTo(expected3));
            assertThat(actual.elementAt(3).getText(), equalTo(expected4));
            assertThat(actual.elementAt(4).getText(), equalTo(expected5));
            assertThat(actual.elementAt(5).getText(), equalTo(expected6));
            assertThat(actual.elementAt(6).getText(), equalTo(expected7));
            assertThat(actual.elementAt(7).getText(), equalTo(expected8));
            assertThat(actual.elementAt(8).getText(), equalTo(expected9));
            assertThat(actual.elementAt(9).getText(), equalTo(expected10));
            assertThat(actual.elementAt(10).getText(), equalTo(expected11));
            assertThat(actual.elementAt(11).getText(), equalTo(expected12));
            assertThat((String) actual.elementAt(12).getText(), equalTo(expected13));
            assertThat(actual.elementAt(13).getText(), equalTo(expected14));
            assertThat(actual.elementAt(14).getText(), equalTo(expected15));
            assertThat(actual.elementAt(15).getText(), equalTo(expected16));
            assertThat(actual.elementAt(16).getText(), equalTo(expected17));
            verify(urlShortening, times(1)).shorten(firstUrl);
            verify(urlShortening, times(2)).shorten(secondUrl);
        }
        
    }
    
    /**
     * visit in weather_info_dom_noon_normal.html.
     * @author jun.ozeki
     */
    public static class WeatherInfoDomNoonNormalHtmlTest {
        @Rule public ExternalFileResource fileResource = new ExternalFileResource(WeatherTitleScraper.class,
                        "weather_info_dom_noon_normal.html", "Shift_JIS");

        private HtmlScraper scraper;
        
        @Before
        public void before() {
            scraper = new HtmlScraper();
        }
        
        @Test
        public void 運行概況タイトル取得() throws Exception {
            // setup
            String resources = fileResource.getResource();
            NodeList nodes = scraper.scrapeMatchNodes(resources, new WeatherTitleScraper().filter(), ParagraphTag.class); 
            NodeList actual = new NodeList();
            WeatherTitleVisitor target = new WeatherTitleVisitor(actual, new HtmlScraper());
            String expected1 = "【運航概況】";
            String expected2 = "本日5日の運航状況は、以下のとおりです。";
            // exercise
            nodes.visitAllNodesWith(target);
            // verify
            assertThat(actual, is(not(nullValue())));
            assertThat(actual.size(), equalTo(2));
            assertThat(actual.elementAt(0).getText(), equalTo(expected1));
            assertThat(actual.elementAt(1).getText(), equalTo(expected2));
        }
    }
}
