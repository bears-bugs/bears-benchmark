package org.broadinstitute.dsde.consent.ontology.datause.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AndTest {

    @Test
    public void testEquals(){
        UseRestriction and1 = new And(
            new Named("one"),
            new Named("two"),
            new Named("three")
        );
        UseRestriction and2 = new And(
            new Named("three"),
            new Named("two"),
            new Named("one")
        );
        UseRestriction and3 = new And(
            new Named("one"),
            new Named("two"),
            new Named("two")
        );

        // Tests that order is not used in equals comparison
        assertTrue(and1.equals(and2));
        assertTrue(and2.equals(and1));

        // Tests that same number of elements, first having all elements in the second,
        // but the second doesn't have all elements of the first, will fail
        assertFalse(and1.equals(and3));
        assertFalse(and2.equals(and3));

        // For completeness' sake, check the inverse
        assertFalse(and3.equals(and1));
        assertFalse(and3.equals(and2));

    }

}
