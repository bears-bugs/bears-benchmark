package org.broadinstitute.dsde.consent.ontology.datause.models.visitor;

import org.broadinstitute.dsde.consent.ontology.datause.models.And;
import org.broadinstitute.dsde.consent.ontology.datause.models.Named;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.FEMALE;
import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.MALE;

public class NamedVisitorTest {

    NamedVisitor namedVisitor;
    UseRestriction name;

    @Before
    public void setUp() {
        name = new And(
            new Named("http://purl.obolibrary.org/obo/DOID_162"),
            new Named(FEMALE),
            new Named(MALE)
        );
        namedVisitor = new NamedVisitor();
    }

    @Test
    public void testVisitTrue() {
        assertTrue(namedVisitor.visit(name));
    }

}