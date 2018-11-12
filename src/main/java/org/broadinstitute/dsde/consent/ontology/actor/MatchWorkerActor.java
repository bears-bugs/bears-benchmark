package org.broadinstitute.dsde.consent.ontology.actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.codahale.metrics.annotation.Timed;

public class MatchWorkerActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    @Timed(name = "onReceive")
    public void onReceive(Object msg) throws Exception {
        OntModelCache ontModelCache = OntModelCache.INSTANCE;
        log.debug("Received Event: " + msg);
        if (msg instanceof MatchWorkerMessage) {
            Boolean match = ontModelCache.matchPurpose((MatchWorkerMessage) msg);
            log.debug("Received match result: " + match);
            getSender().tell(match, getSelf());
        } else {
            log.error("Received unknown message: " + msg);
        }

    }

}
