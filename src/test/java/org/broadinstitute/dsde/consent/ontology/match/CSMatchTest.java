package org.broadinstitute.dsde.consent.ontology.match;

import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class CSMatchTest extends MatchTestBase {
    
    public CSMatchTest(DataUse purpose, DataUse consent, Boolean expectedMatchResult, String testName) {
        super(purpose, consent, expectedMatchResult, testName);
    }
    
    // define all your test cases here! order is purpose, consent, expected result, test name.
    @Parameterized.Parameters(name="CSMatchTest {index}: {3}")
    public static Collection<Object[]> tests() {
        return Arrays.asList(new Object[][]{
            { Fixtures.CS.csa, Fixtures.CS.uc1, true, "CSA vs. UC1" },
            { Fixtures.CS.csa, Fixtures.CS.uc2, true, "CSA vs. UC2" },
            { Fixtures.CS.csa, Fixtures.CS.uc3, true, "CSA vs. UC3" },
            // { Fixtures.CS.csa, Fixtures.CS.uc4, true, "CSA vs. UC4" }, // TODO: fix test. Doc says match should succeed.
        
            { Fixtures.CS.csb, Fixtures.CS.uc1, true, "CSB vs. UC1" },
            { Fixtures.CS.csb, Fixtures.CS.uc2, true, "CSB vs. UC2" },
            { Fixtures.CS.csb, Fixtures.CS.uc3, true, "CSB vs. UC3" },
            // { Fixtures.CS.csb, Fixtures.CS.uc4, true, "CSB vs. UC4" }, // TODO: fix test. Doc says match should succeed.
        
            { Fixtures.CS.csc, Fixtures.CS.uc1, false, "CSC vs. UC1" },
            { Fixtures.CS.csc, Fixtures.CS.uc2, false, "CSC vs. UC2" },
            // { Fixtures.CS.csc, Fixtures.CS.uc3, true, "CSC vs. UC3" }, // TODO: fix test. Doc says match should succeed.
            // { Fixtures.CS.csc, Fixtures.CS.uc4, true, "CSC vs. UC4" }, // TODO: fix test. Doc says match should succeed.
        
            { Fixtures.CS.csd, Fixtures.CS.uc1, false, "CSD vs. UC1" },
            { Fixtures.CS.csd, Fixtures.CS.uc2, false, "CSD vs. UC2" },
            // { Fixtures.CS.csd, Fixtures.CS.uc3, true, "CSD vs. UC3" }, // TODO: fix test. Doc says match should succeed.
            { Fixtures.CS.csd, Fixtures.CS.uc4, true, "CSD vs. UC4" }
        });
    }
    
}
