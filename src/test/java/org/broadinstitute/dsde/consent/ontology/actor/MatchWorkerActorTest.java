package org.broadinstitute.dsde.consent.ontology.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import com.google.common.io.Resources;
import org.broadinstitute.dsde.consent.ontology.datause.models.Named;
import org.broadinstitute.dsde.consent.ontology.datause.models.Or;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.resources.MatchPair;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.duration.FiniteDuration;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;

public class MatchWorkerActorTest {

    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    private static final UseRestriction APNEA = new Named("http://purl.obolibrary.org/obo/DOID_9220");
    private static final UseRestriction HEART_DISEASE = new Named("http://purl.obolibrary.org/obo/DOID_114");
    private static final UseRestriction CHF = new Named("http://purl.obolibrary.org/obo/DOID_6000");
    private static final UseRestriction SARCOMA = new Named("http://purl.obolibrary.org/obo/DOID_1115");
    private static final UseRestriction CANCER = new Named("http://purl.obolibrary.org/obo/DOID_162");
    private static final UseRestriction HD_CANCER = new Or(HEART_DISEASE, CANCER);

    private static final Collection<URL> RESOURCES = Collections.singletonList(Resources.getResource("diseases.owl"));

    @Test
    public void testIt() {

        new JavaTestKit(system) {{
            final Props props = Props.create(MatchWorkerActor.class);
            final ActorRef subject = system.actorOf(props);

            MatchWorkerMessage message1 = new MatchWorkerMessage(RESOURCES, new MatchPair(CHF, CANCER));
            MatchWorkerMessage message2 = new MatchWorkerMessage(RESOURCES, new MatchPair(SARCOMA, CANCER));
            MatchWorkerMessage message3 = new MatchWorkerMessage(RESOURCES, new MatchPair(CHF, HEART_DISEASE));
            MatchWorkerMessage message4 = new MatchWorkerMessage(RESOURCES, new MatchPair(SARCOMA, HD_CANCER));
            MatchWorkerMessage message5 = new MatchWorkerMessage(RESOURCES, new MatchPair(CHF, HD_CANCER));
            MatchWorkerMessage message6 = new MatchWorkerMessage(RESOURCES, new MatchPair(APNEA, CANCER));

            FiniteDuration finiteDuration = duration("60 seconds");
            new Within(finiteDuration) {
                protected void run() {
                    subject.tell(message1, getRef());
                    expectMsgEquals(finiteDuration, false);

                    subject.tell(message2, getRef());
                    expectMsgEquals(finiteDuration, true);

                    subject.tell(message3, getRef());
                    expectMsgEquals(finiteDuration, true);

                    subject.tell(message4, getRef());
                    expectMsgEquals(finiteDuration, true);

                    subject.tell(message5, getRef());
                    expectMsgEquals(finiteDuration, true);

                    subject.tell(message6, getRef());
                    expectMsgEquals(finiteDuration, false);
                }
            };
        }};

    }

}
