package org.komusubi.feeder.web;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.komusubi.feeder.model.Topics;
import org.komusubi.feeder.sns.Speaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jal 5971 Servlet implementation.
 */
@Path("/jal5971")
public class Jal5971Resource {
    
    private static final Logger logger = LoggerFactory.getLogger(Jal5971Resource.class);
    @Context UriInfo uriInfo;
    @Context Request request;
    private Speaker talker;
    private Topics topics;

    /**
     * Default constructor.
     */
    @Inject
    public Jal5971Resource(Speaker talker, Topics topics) {
        this.talker = talker;
        this.topics = topics;
    }

    /**
     * 
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("weather")
    public String weather() {
        // TODO what is the trigger of jal5971 feed ? (GET/POST)
        logger.info("HTTP GET");
        talker.talk(topics);
        return "";
    }

}
