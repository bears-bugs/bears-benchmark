package org.broadinstitute.dsde.consent.ontology.truthtable;

import com.google.common.io.Resources;
import org.broadinstitute.dsde.consent.ontology.AbstractTest;
import org.broadinstitute.dsde.consent.ontology.actor.OntModelCache;
import org.broadinstitute.dsde.consent.ontology.actor.MatchWorkerMessage;
import org.broadinstitute.dsde.consent.ontology.resources.MatchPair;
import org.junit.Assert;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

/**
 * See https://docs.google.com/document/d/1xyeYoIKBDFGAsQ_spoK5Ye5esMOXqpRBojd6ijZWJkk
 * for a summary of use cases for which this test class covers.
 *
 * See org.broadinstitute.dsde.consent.ontology.datause.builder.ConsentUseCases and
 * org.broadinstitute.dsde.consent.ontology.datause.builder.DARUseCases
 * for test cases.
 */
@Deprecated
public class TruthTableTests extends AbstractTest {

    private Collection<URL> RESOURCES = Arrays.asList(
        Resources.getResource("diseases.owl"),
        Resources.getResource("data-use.owl"));

    private OntModelCache ontModelCache = OntModelCache.INSTANCE;

    public void assertResponse(MatchPair pair, Boolean expected) {
        MatchWorkerMessage message = new MatchWorkerMessage(RESOURCES, pair);
        try {
            Assert.assertTrue(ontModelCache.matchPurpose(message).equals(expected));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

}
