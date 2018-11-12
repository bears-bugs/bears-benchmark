package org.broadinstitute.dsde.consent.ontology;

import com.google.common.io.Resources;
import org.broadinstitute.dsde.consent.ontology.actor.MatchWorkerMessage;
import org.broadinstitute.dsde.consent.ontology.actor.OntModelCache;
import org.broadinstitute.dsde.consent.ontology.datause.models.*;
import org.broadinstitute.dsde.consent.ontology.resources.MatchPair;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;

import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.AGGREGATE_RESEARCH;
import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.METHODS_RESEARCH;


public class DataUseOwlTest extends AbstractTest {

    private static final OntModelCache ONT_MODEL_CACHE = OntModelCache.INSTANCE;
    private static final Collection<URL> resources = Collections.singletonList(Resources.getResource("data-use.owl"));
    private static final UseRestriction methodsPurpose = new Named(METHODS_RESEARCH);
    private static final UseRestriction aggregatePurpose = new Named(AGGREGATE_RESEARCH);

    @BeforeClass
    public static void setUp() throws Exception {
    }

    @AfterClass
    public static void after() throws Exception {
    }


    @Test
    public void testNegativeMethodsAgainstInverse() throws Exception {
        UseRestriction consent = new Not(new Named(METHODS_RESEARCH));
        Boolean b = ONT_MODEL_CACHE.matchPurpose(new MatchWorkerMessage(resources, new MatchPair(methodsPurpose, consent)));
        Assert.assertFalse(b);
    }

    @Test
    public void testNegativeMethodsAgainstNothing() throws Exception {
        UseRestriction consent = new Nothing();
        Boolean b = ONT_MODEL_CACHE.matchPurpose(new MatchWorkerMessage(resources, new MatchPair(methodsPurpose, consent)));
        Assert.assertFalse(b);
    }

    @Test
    public void testPositiveAggregate() throws Exception {
        UseRestriction consent = new Everything();
        Boolean b = ONT_MODEL_CACHE.matchPurpose(new MatchWorkerMessage(resources, new MatchPair(aggregatePurpose, consent)));
        Assert.assertTrue(b);
    }

    @Test
    public void testNegativeAggregateAgainstInverse() throws Exception {
        UseRestriction consent = new Not(new Named(AGGREGATE_RESEARCH));
        Boolean b = ONT_MODEL_CACHE.matchPurpose(new MatchWorkerMessage(resources, new MatchPair(aggregatePurpose, consent)));
        Assert.assertFalse(b);
    }

    @Test
    public void testNegativeAggregateAgainstNothing() throws Exception {
        UseRestriction consent = new Nothing();
        Boolean b = ONT_MODEL_CACHE.matchPurpose(new MatchWorkerMessage(resources, new MatchPair(aggregatePurpose, consent)));
        Assert.assertFalse(b);
    }

}
