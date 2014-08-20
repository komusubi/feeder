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
package org.komusubi.feeder.web.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.komusubi.feeder.utils.Types.AggregateType;
import org.komusubi.feeder.utils.Types.ScrapeType;
import org.komusubi.feeder.web.module.Module;
import org.komusubi.feeder.web.module.TopicsBuilder;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;

/**
 * @author jun.ozeki
 */
@Path("/feeder")
@Produces(MediaType.APPLICATION_JSON)
public class FeederResource {
    private static final Logger logger = LoggerFactory.getLogger(FeederResource.class);
    private DBI dbi;
    @Context private UriInfo uriInfo;
    
    /**
     * @param dbi
     */
    public FeederResource(DBI dbi) {
        this.dbi = dbi;
    }

    @GET
    public String get() {
        return "hell world";
    }
    
    @POST
    @Path("/{channel:5971|5931}/{feed:rss|scrape}")
    @Timed
    public Response post(@PathParam("channel") String channel, @PathParam("feed") String feed) {
        logger.info("post url: {}", uriInfo.getAbsolutePath().toASCIIString());
        TopicsBuilder builder = new TopicsBuilder(dbi,
                                                AggregateType.valueOf(feed.toUpperCase()), 
                                                ScrapeType.valueOf(channel));
        Module module = builder.build();
        // TODO implements return object will be able to parse json format.
        module.run();
        return Response.noContent().build();
    }
}
