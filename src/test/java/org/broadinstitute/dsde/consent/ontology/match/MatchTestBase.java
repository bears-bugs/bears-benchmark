package org.broadinstitute.dsde.consent.ontology.match;

import org.broadinstitute.dsde.consent.ontology.datause.builder.ConsentRestrictionBuilder;
import org.broadinstitute.dsde.consent.ontology.datause.builder.DARRestrictionBuilder;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.resources.MatchPair;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.truthtable.TruthTableTests;
import org.junit.Test;

/**
 * Created by davidan on 5/3/17.
 */
abstract class MatchTestBase extends TruthTableTests {
    
    private DataUse purpose;
    private DataUse consent;
    private Boolean expectedMatchResult;
    private String testName;
    
    public MatchTestBase(DataUse purpose, DataUse consent, Boolean expectedMatchResult, String testName) {
        this.purpose = purpose;
        this.consent = consent;
        this.expectedMatchResult = expectedMatchResult;
        this.testName = testName;
    }
    
    @Test
    public void parameterizedTest() {
        assertResponse(toMatchPair(purpose, consent), expectedMatchResult);
    }
    
    MatchPair toMatchPair(DataUse purpose, DataUse consent) {
        UseRestriction structuredPurpose = new DARRestrictionBuilder().buildUseRestriction(purpose);
        UseRestriction structuredConsent = new ConsentRestrictionBuilder().buildUseRestriction(consent);
        return new MatchPair(structuredPurpose, structuredConsent);
    }
}
