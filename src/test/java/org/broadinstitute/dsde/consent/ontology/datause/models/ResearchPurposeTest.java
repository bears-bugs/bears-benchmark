package org.broadinstitute.dsde.consent.ontology.datause.models;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.FEMALE;
import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.PEDIATRIC;

public class ResearchPurposeTest extends ResearchPurpose {

    @Test
    public void testNullConstructor() {
        assertNull(getId());
        assertNull(getPurpose());
    }

    @Test
    public void testEqualsFalseInstanceOf() {
        String test = "test";
        boolean result = equals(test);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualsInstanceOf() {
        ResearchPurpose researchPurpose = new ResearchPurpose();
        boolean result = equals(researchPurpose);
        Assert.assertTrue(result);
    }

    @Test
    public void testPropertiesAssignment() throws IOException {
        setId("Id Test");
        assertNotNull(getId());
        UseRestriction and = new And(
            new Named("http://purl.obolibrary.org/obo/DOID_162"),
            new Named(FEMALE),
            new Named(PEDIATRIC)
        );
        setPurpose(and);
        assertNotNull(getPurpose());
    }
}
