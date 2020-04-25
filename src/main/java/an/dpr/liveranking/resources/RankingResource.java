package an.dpr.liveranking.resources;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.liveranking.model.Ranking;

@Path("/ranking")
public class RankingResource {

    private static final Logger logger = LoggerFactory.getLogger(RankingResource.class);

    @Inject
    @Channel("ranking-stream") Publisher<Ranking> ranking;

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS) 
    @SseElementType(MediaType.TEXT_PLAIN) 
    public Publisher<Ranking> get() {
        logger.debug("stream resource called");
        return ranking;
    }
}