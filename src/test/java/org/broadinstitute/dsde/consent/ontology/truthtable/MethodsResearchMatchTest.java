package org.broadinstitute.dsde.consent.ontology.truthtable;

import org.broadinstitute.dsde.consent.ontology.datause.models.*;
import org.broadinstitute.dsde.consent.ontology.resources.MatchPair;
import org.junit.Test;

import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.*;

public class MethodsResearchMatchTest extends TruthTableTests {

    private UseRestriction darMRPA = new And(
        new Named(METHODS_RESEARCH),
        new Named("http://purl.obolibrary.org/obo/DOID_162")
    );

    private UseRestriction darDefaultMRPA = new And(
        new And(
            new Named(METHODS_RESEARCH),
            new Not(new Named(POPULATION_STRUCTURE)),
            new Not(new Named(CONTROL))
        ),
        new Named("http://purl.obolibrary.org/obo/DOID_162"),
        new Named(NON_PROFIT)
    );

    private UseRestriction darMRPB = new Named("http://purl.obolibrary.org/obo/DOID_162");

    private UseRestriction darDefaultMRPB = new And(
        new And(
            new Not(new Named(METHODS_RESEARCH)),
            new Not(new Named(POPULATION_STRUCTURE)),
            new Not(new Named(CONTROL))
        ),
        new Named("http://purl.obolibrary.org/obo/DOID_162"),
        new Named(NON_PROFIT));

    private UseRestriction darMRPC = new Named(METHODS_RESEARCH);


    private UseRestriction darDefaultMRPC = new And(
        new And(
            new Named(METHODS_RESEARCH),
            new Not(new Named(POPULATION_STRUCTURE)),
            new Not(new Named(CONTROL))
        ),
        new Named(NON_PROFIT));

    private UseRestriction darCSA = new And(
        new Named("http://purl.obolibrary.org/obo/DOID_4422"),
        new Named(CONTROL)
    );

    private UseRestriction darCSC = new Named(CONTROL);

    private UseRestriction darCSD = new And(
        new Named("http://purl.obolibrary.org/obo/DOID_423"),
        new Named(CONTROL)
    );


    // Combined example from OD-329
    private UseRestriction dulUC1 = new Or(
        new Named(AGGREGATE_RESEARCH),
        new Or(
            new Named(METHODS_RESEARCH),
            new Named("http://purl.obolibrary.org/obo/DOID_162")
        )
    );

    // Modified dul for Control Set Usage Prohibited
    private UseRestriction mDulUC1 = new Named("http://purl.obolibrary.org/obo/DOID_162");

    // Combined example from OD-330
    private UseRestriction dulUC2 =
        new Or(
            new Named(AGGREGATE_RESEARCH),
            new Or(
                new And(
                    new Named("http://purl.obolibrary.org/obo/DOID_162"),
                    new Not(new Named(METHODS_RESEARCH))
                ),
                new Named("http://purl.obolibrary.org/obo/DOID_162")
            )
        );

    //Modified dul for Control Set Usage Prohibited
    private UseRestriction mDulUC2 = new And(
        new Named("http://purl.obolibrary.org/obo/DOID_162"),
        new Not(new Named(CONTROL))
    );

    // Combined example from OD-331
    private UseRestriction dulUC3 = new Or(
        new Named(AGGREGATE_RESEARCH),
        new Or(
            new And(
                new Everything(),
                new Not(new Named(METHODS_RESEARCH))
            ),
            new Everything()
        )
    );

    // Modified dulUC3 for MRPC.
    private UseRestriction mDulUC3 = new And(
        new Everything(),
        new Not(new Named(METHODS_RESEARCH))
    );

    //Modified dul for Control Set Usage Prohibited
    private UseRestriction csdDulUC3 = new And(
        new Named("http://purl.obolibrary.org/obo/DOID_162"),
        new Named(CONTROL));

    // Combined example from OD-332
    private UseRestriction dulUC4 = new Or(
        new Named(AGGREGATE_RESEARCH),
        new Or(
            new Named(METHODS_RESEARCH),
            new Named("http://purl.obolibrary.org/obo/DOID_162")
        )
    );

    // Modified dulUC4 for Control Set Usage Prohibited
    private UseRestriction mDulUC4 = new And(
        new Named("http://purl.obolibrary.org/obo/DOID_4422"),
        new Named(CONTROL));

    @Test
    public void testMRPA_UC1() {

        // Testing the case where:
        // DAR is yes methods, yes cancer
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPA, dulUC1);
        assertResponse(pair, true);
    }

    @Test
    public void testDefaultMRPA_UC1() {

        // Testing the case where:
        // DAR is yes methods, yes cancer, yes not profit and not population and control
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultMRPA, dulUC1);
        assertResponse(pair, true);
    }

    @Test
    public void testMRPA_UC2() {

        // Testing the case where:
        // DAR is yes methods, yes cancer
        // DUL is yes cancer, no methods
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPA, dulUC2);
        assertResponse(pair, true);
    }

    @Test
    public void testDefaultMRPA_UC2() {

        // Testing the case where:
        // DAR is yes methods, yes cancer
        // DAR is yes methods, yes cancer and not profit. Not population and control
        // DUL is yes cancer, no methods
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultMRPA, dulUC2);
        assertResponse(pair, true);
    }

    @Test
    public void testMRPA_UC3() {

        // Testing the case where:
        // DAR is yes methods, yes cancer
        // DUL is yes for everything, but no methods research allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPA, dulUC3);
        assertResponse(pair, true);
    }

    @Test
    public void testDefaultMRPA_UC3() {

        // Testing the case where:
        // DAR is yes methods, yes cancer and not profit. Not population and control
        // DUL is yes for everything, but no methods research allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultMRPA, dulUC3);
        assertResponse(pair, true);
    }

    @Test
    public void testMRPA_UC4() {

        // Testing the case where:
        // DAR is yes methods, yes cancer
        // DUL is yes for everything, but no methods research allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPA, dulUC4);
        assertResponse(pair, true);
    }

    @Test
    public void testDefaultMRPA_UC4() {

        // Testing the case where:
        // DAR is yes methods, yes cancer and not profit. Not population and control
        // DUL is yes for everything, but no methods research allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultMRPA, dulUC4);
        assertResponse(pair, true);
    }

    @Test
    public void testMRPB_UC1() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPB, dulUC1);
        assertResponse(pair, true);
    }


    @Test
    public void testDefaultMRPB_UC1() {

        // Testing the case where:
        // DAR is yes cancer and not profit. Not population and control
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultMRPB, dulUC1);
        assertResponse(pair, true);
    }

    @Test
    public void testMRPB_UC2() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes cancer, no methods
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPB, dulUC2);
        assertResponse(pair, true);
    }

    @Test
    public void testDefaultMRPB_UC2() {

        // Testing the case where:
        // DAR is yes cancer and not profit. Not population and control
        // DUL is yes cancer, no methods
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultMRPB, dulUC2);
        assertResponse(pair, true);
    }

    @Test
    public void testMRPB_UC3() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes for everything, but no methods research allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPB, dulUC3);
        assertResponse(pair, true);
    }

    @Test
    public void testDefaultMRPB_UC3() {

        // Testing the case where:
        // DAR is yes cancer and not profit. Not population and control
        // DUL is yes for everything, but no methods research allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultMRPB, dulUC3);
        assertResponse(pair, true);
    }

    @Test
    public void testMRPB_UC4() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes for everything, but no methods research allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPB, dulUC4);
        assertResponse(pair, true);
    }

    @Test
    public void testDefaultMRPB_UC4() {

        // Testing the case where:
        // DAR is yes cancer and not profit. Not population and control
        // DUL is yes for everything, but no methods research allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultMRPB, dulUC4);
        assertResponse(pair, true);
    }


    @Test
    public void testMRPC_UC1() {

        // Testing the case where:
        // DAR is methods research
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPC, dulUC1);
        assertResponse(pair, true);
    }

    @Test
    public void testDefaultMRPC_UC1() {

        // Testing the case where:
        // DAR is methods research
        // DAR is methods research and not profit. Not population and control
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultMRPC, dulUC1);
        assertResponse(pair, true);
    }

    @Test
    public void testMRPC_UC2() {

        // Testing the case where:
        // DAR is methods research
        // DUL is yes cancer, no methods
        // Response should be negative

        MatchPair pair = new MatchPair(darMRPC, dulUC2);
        assertResponse(pair, false);
    }

    @Test
    public void testDefaultMRPC_UC2() {

        // Testing the case where:
        // DAR is methods research and not profit. Not population and control
        // DUL is yes cancer, no methods
        // Response should be negative

        MatchPair pair = new MatchPair(darDefaultMRPC, dulUC2);
        assertResponse(pair, false);
    }

    @Test
    public void testMRPC_UC3() {

        // Testing the case where:
        // DAR is methods research
        // DUL is yes for everything, but no methods research allowed
        // Response should be negative

        MatchPair pair = new MatchPair(darMRPC, mDulUC3);
        assertResponse(pair, false);
    }

    @Test
    public void testDefaultMRPC_UC3() {

        // Testing the case where:
        // DAR is methods research and not profit. Not population and control
        // DUL is yes for everything, but no methods research allowed
        // Response should be negative

        MatchPair pair = new MatchPair(darDefaultMRPC, mDulUC3);
        assertResponse(pair, false);
    }

    @Test
    public void testMRPC_UC4() {

        // Testing the case where:
        // DAR is methods research
        // DUL is yes for everything, but no methods research allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPC, dulUC4);
        assertResponse(pair, true);
    }

    @Test
    public void testDefaultMRPC_UC4() {

        // Testing the case where:
        // DAR is methods research and not profit. Not population and control
        // DUL is yes for everything, but no methods research allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultMRPC, dulUC4);
        assertResponse(pair, true);
    }

    @Test
    public void testCSA_mUC4() {

        // Testing the case where:
        // DAR is cancer and Controls
        // DUL is Any Disease
        // Response should be positive

        MatchPair pair = new MatchPair(darCSA, mDulUC4);
        assertResponse(pair, true);
    }

    @Test
    public void testCSC_mUC4() {

        // Testing the case where:
        // DAR is yes Controls
        // DUL is Any Disease
        // Response should be negative

        MatchPair pair = new MatchPair(darCSC, mDulUC4);
        assertResponse(pair, false);
    }

    @Test
    public void testCSD_UC1() {

        // Testing the case where:
        // DAR is yes Controls and any disease
        // DUL is yes cancer
        // Response should be negative

        MatchPair pair = new MatchPair(darCSD, mDulUC1);
        assertResponse(pair, false);

    }

    @Test
    public void testCSD_UC2() {

        // Testing the case where:
        // DAR is yes Controls and Any Disease
        // DUL is yes cancer no Controls for others than cancer
        // Response should be negative

        MatchPair pair = new MatchPair(darCSA, mDulUC2);
        assertResponse(pair, false);
    }

    @Test
    public void testCSD_mUC3() {

        // Testing the case where:
        // DAR is yes Controls and Any Disease
        // DUL is yes cancer yes Controls.
        // Response should be positive

        MatchPair pair = new MatchPair(darCSA, csdDulUC3);
        assertResponse(pair, true);

    }

    @Test
    public void testCSD_mUC4() {

        // Testing the case where:
        // DAR is yes Controls and Any disease
        // DUL is Any Disease
        // Response should be positive

        MatchPair pair = new MatchPair(darCSA, mDulUC4);
        assertResponse(pair, true);
    }
}
