package org.broadinstitute.dsde.consent.ontology.resources;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.OnComplete;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import org.broadinstitute.dsde.consent.ontology.actor.MatchWorkerActor;
import org.broadinstitute.dsde.consent.ontology.actor.MatchWorkerMessage;
import org.broadinstitute.dsde.consent.ontology.service.StoreOntologyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.Collection;

@Path("/match")
@Consumes("application/json")
@Produces("application/json")
public class MatchResource {

    private final Logger log = LoggerFactory.getLogger(MatchResource.class);
    private StoreOntologyService storeOntologyService;

    private static final ActorSystem actorSystem = ActorSystem.create("actorSystem");
    private static final ActorRef matchWorkerActor = actorSystem.actorOf(Props.create(MatchWorkerActor.class), "MatchWorkerActor");

    public MatchResource() {}

    @POST
    public void match(@Suspended final AsyncResponse response, final MatchPair matchPair) throws Exception {
        // TODO: Timeout should likely be an application-wide property
        Timeout timeout = new Timeout(Duration.create(15000, "seconds"));
        log.debug("Received the following: " + matchPair.toString());
        Collection<URL> urls = storeOntologyService.retrieveOntologyURLs();
        final MatchWorkerMessage matchMessage = new MatchWorkerMessage(urls, matchPair);
        final Future<Object> matchFuture = Patterns.ask(matchWorkerActor, matchMessage, timeout);
        matchFuture.onComplete(
            new OnComplete<Object>() {
                @Override
                public void onComplete(Throwable failure, Object result) {
                    if (failure != null) {
                        log.error("Failure: " + failure.getMessage());
                        if(failure instanceof InternalError){
                            response.resume(new WebApplicationException(failure.getMessage(), Response.Status.INTERNAL_SERVER_ERROR));
                        }
                        response.resume(new WebApplicationException(failure));
                    } else{
                        response.resume(ImmutableMap.of("result", result, "matchPair", matchPair));
                    }
                }
            },
            actorSystem.dispatcher()
        );
    }

    @Inject
    public void setStoreOntologyService(StoreOntologyService storeOntologyService) {
        this.storeOntologyService = storeOntologyService;
    }

}
