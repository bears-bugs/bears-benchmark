package org.broadinstitute.dsde.consent.ontology.match;

import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class HMBMatchTest extends MatchTestBase {
    
    public HMBMatchTest(DataUse purpose, DataUse consent, Boolean expectedMatchResult, String testName) {
        super(purpose, consent, expectedMatchResult, testName);
    }
    
    // define all your test cases here! order is purpose, consent, expected result, test name.
    @Parameterized.Parameters(name="HMBMatchTest {index}: {3}")
    public static Collection<Object[]> tests() {
        // Note that the purposes in this truth table are exactly the same as MRP, so we reuse them below
        return Arrays.asList(new Object[][]{
            { Fixtures.MRP.mrpa, Fixtures.HMB.uc1, true, "MRPA vs. UC1" },
            { Fixtures.MRP.mrpa, Fixtures.HMB.uc2, true, "MRPA vs. UC2" },
            { Fixtures.MRP.mrpa, Fixtures.HMB.uc3, true, "MRPA vs. UC3" },
            { Fixtures.MRP.mrpa, Fixtures.HMB.uc4, true, "MRPA vs. UC4" },
            
            { Fixtures.MRP.mrpb, Fixtures.HMB.uc1, true, "MRPB vs. UC1" },
            { Fixtures.MRP.mrpb, Fixtures.HMB.uc2, true, "MRPB vs. UC2" },
            { Fixtures.MRP.mrpb, Fixtures.HMB.uc3, true, "MRPB vs. UC3" },
            { Fixtures.MRP.mrpb, Fixtures.HMB.uc4, true, "MRPB vs. UC4" },
                
            { Fixtures.MRP.mrpc, Fixtures.HMB.uc1, true, "MRPC vs. UC1" },
            // { Fixtures.MRP.mrpc, Fixtures.HMB.uc2, false, "MRPC vs. UC2" }, // TODO: fix test. Doc says match should fail.
            // { Fixtures.MRP.mrpc, Fixtures.HMB.uc3, false, "MRPC vs. UC3" }, // TODO: fix test. Doc says match should fail.
            { Fixtures.MRP.mrpc, Fixtures.HMB.uc4, true, "MRPC vs. UC4" },
        
            { Fixtures.CS.csa, Fixtures.HMB.uc1, true, "CSA vs. UC1" },
            { Fixtures.CS.csb, Fixtures.HMB.uc1, true, "CSB vs. UC1" },
            { Fixtures.CS.csc, Fixtures.HMB.uc1, true, "CSC vs. UC1" },
            { Fixtures.CS.csd, Fixtures.HMB.uc1, true, "CSD vs. UC1" }
        });
    }
    
}
