package org.broadinstitute.dsde.consent.ontology.service.validate;

import org.apache.log4j.Logger;
import org.broadinstitute.dsde.consent.ontology.AbstractTest;
import org.broadinstitute.dsde.consent.ontology.datause.api.LuceneOntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.api.OntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.service.StoreOntologyService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.NON_PROFIT;
import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.PEDIATRIC;

public class UseRestrictionValidateImplTest extends AbstractTest {

    private static UseRestrictionValidateImpl service;
    private final Logger log = Logger.getLogger(UseRestrictionValidateImplTest.class);

    @BeforeClass
    public static void setUpClass() throws Exception {
        StoreOntologyService storeOntologyServiceMock = getStorageServiceMock();
        OntologyTermSearchAPI api = new LuceneOntologyTermSearchAPI(storeOntologyServiceMock);
        service = new UseRestrictionValidateImpl();
        service.setOntologySearchTermAPI(api);
    }

    /**
     * Test of validateUseRestriction method, of class
     * UseRestrictionValidateImpl.
     */
    @Test
    public void testValidateUseRestrictionValid() throws Exception {
        log.debug("validateUseRestriction");
        String validRestriction = "{\"type\":\"and\",\"operands\":[{\"type\":\"named\",\"name\":\"http://purl.obolibrary.org/obo/DOID_162\"},"
            + "{\"type\":\"named\",\"name\":\"" + PEDIATRIC + "\"},"
            + "{\"type\":\"named\",\"name\":\"" + NON_PROFIT + "\"}]}";
        ValidateResponse result = service.validateUseRestriction(validRestriction);
        Assert.assertEquals(true, result.isValid());
    }

    @Test
    public void testValidateUseRestrictionFail() throws Exception {
        log.debug("validateUseRestriction");
        String invalidRestriction = "{\"type\":\"and\",\"operands\":[{\"type\":\"named\",\"name\":\"http://purl.obolibrary.org/obo/DOID_X162\"},"
            + "{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DUOS/choldren\"},"
            + "{\"type\":\"named\",\"name\":\"" + NON_PROFIT + "\"}]}";
        ValidateResponse result = service.validateUseRestriction(invalidRestriction);
        Assert.assertEquals(false, result.isValid());
    }

    @Test
    public void testValidateUseRestrictionInvalidTerm() throws Exception {
        String invalidRestriction = "{\"type\":\"named\",\"name\": \"http://purl.obolibrary.org/obo/BLURB\"}";
        ValidateResponse result = service.validateUseRestriction(invalidRestriction);
        Assert.assertEquals(false, result.isValid());
        Assert.assertEquals("Term not found: http://purl.obolibrary.org/obo/BLURB", ((ArrayList) result.getErrors()).get(0));
    }

    @Test
    public void testValidateUseRestrictionErrorMessage() throws Exception {
        String invalidRestriction = "{\"type\":\"named\",\"weird\": \"http://purl.obolibrary.org/obo/BLURB\"}";
        ValidateResponse result = service.validateUseRestriction(invalidRestriction);
        Assert.assertEquals(false, result.isValid());
        Assert.assertEquals("Could not resolve the following keys: weird into a subtype of type, operands, operand, name", ((ArrayList) result.getErrors()).get(0));
    }
}
