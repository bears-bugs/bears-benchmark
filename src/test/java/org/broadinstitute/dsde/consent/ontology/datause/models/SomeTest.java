package org.broadinstitute.dsde.consent.ontology.datause.models;


import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.NamedVisitor;
import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.UseRestrictionVisitor;
import org.junit.Before;
import org.junit.Test;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import static junit.framework.TestCase.assertNotNull;
import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.FEMALE;
import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.PEDIATRIC;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SomeTest {

    private Some some;

    private UseRestriction useRestriction;

    private String property;

    @Before
    public void setUp() {
        property = "name";
        useRestriction = new Or(
            new Named("http://purl.obolibrary.org/obo/DOID_162"),
            new Named(FEMALE),
            new Named(PEDIATRIC)
        );

        some = new Some(property, useRestriction);
    }

    @Test
    public void testCreateOntologicalRestriction() {
        OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
        OntClass ontClass = some.createOntologicalRestriction(model);
        assertNotNull(ontClass);
    }

    @Test
    public void testOrEqualsFalse() {
        Some newSome = new Some("test", new Everything());
        assertFalse(some.equals(newSome));
    }

    @Test
    public void testOrEqualsTrue() {
        Some testObj = new Some(property, useRestriction);
        assertTrue(some.equals(testObj));
    }

    @Test
    public void testVisitAndContinue() {
        UseRestrictionVisitor visitor = new NamedVisitor();
        assertTrue(useRestriction.visitAndContinue(visitor));
    }

}
