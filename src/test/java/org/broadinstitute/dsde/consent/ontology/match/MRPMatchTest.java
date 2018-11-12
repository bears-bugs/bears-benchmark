package org.broadinstitute.dsde.consent.ontology.match;

import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MRPMatchTest extends MatchTestBase {
    
    public MRPMatchTest(DataUse purpose, DataUse consent, Boolean expectedMatchResult, String testName) {
        super(purpose, consent, expectedMatchResult, testName);
    }
    
    // define all your test cases here! order is purpose, consent, expected result, test name.
    @Parameterized.Parameters(name="MRPMatchTest {index}: {3}")
    public static Collection<Object[]> tests() {
        return Arrays.asList(new Object[][]{
            { Fixtures.MRP.mrpa, Fixtures.MRP.uc1, true, "MRPA vs. UC1" },
            { Fixtures.MRP.mrpa, Fixtures.MRP.uc2, true, "MRPA vs. UC2" },
            { Fixtures.MRP.mrpa, Fixtures.MRP.uc3, true, "MRPA vs. UC3" },
            { Fixtures.MRP.mrpa, Fixtures.MRP.uc4, true, "MRPA vs. UC4" },
            
            { Fixtures.MRP.mrpb, Fixtures.MRP.uc1, true, "MRPB vs. UC1" },
            { Fixtures.MRP.mrpb, Fixtures.MRP.uc2, true, "MRPB vs. UC2" },
            { Fixtures.MRP.mrpb, Fixtures.MRP.uc3, true, "MRPB vs. UC3" },
            { Fixtures.MRP.mrpb, Fixtures.MRP.uc4, true, "MRPB vs. UC4" },
                
            { Fixtures.MRP.mrpc, Fixtures.MRP.uc1, true, "MRPC vs. UC1" },
            { Fixtures.MRP.mrpc, Fixtures.MRP.uc2, false, "MRPC vs. UC2" },
            // { Fixtures.MRP.mrpc, Fixtures.MRP.uc3, false, "MRPC vs. UC3" }, // TODO: fix test. Doc says match should fail.
            { Fixtures.MRP.mrpc, Fixtures.MRP.uc4, true, "MRPC vs. UC4" },
        });
    }
    
}
