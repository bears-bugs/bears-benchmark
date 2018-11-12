package org.broadinstitute.dsde.consent.ontology.datause.services;

import org.apache.log4j.Logger;
import org.broadinstitute.dsde.consent.ontology.AbstractTest;
import org.broadinstitute.dsde.consent.ontology.datause.api.LuceneOntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.api.OntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.service.StoreOntologyService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.NON_PROFIT;
import static org.junit.Assert.assertEquals;

public class TextTranslationServiceImplTest extends AbstractTest {

    private static TextTranslationServiceImpl service;
    private static OntologyTermSearchAPI api;
    private final Logger log = Logger.getLogger(TextTranslationServiceImplTest.class);

    public TextTranslationServiceImplTest() {
    }

    @Before
    public void setUpClass() throws IOException, GeneralSecurityException {
        StoreOntologyService storeOntologyServiceMock = getStorageServiceMock();
        api = new LuceneOntologyTermSearchAPI(storeOntologyServiceMock);
        service = new TextTranslationServiceImpl(storeOntologyServiceMock);
        service.setApi(api);
    }

    @After
    public void tearDownClass() {
        service = null;
        api = null;
    }

    /**
     * Test of translateSample method, of class TextTranslationServiceImpl.
     *
     * @throws Exception The Exception
     */
    @Test
    public void testTranslateSample() throws Exception {
        log.debug("translateSample");
        String restrictionStr = "{\"type\":\"and\",\"operands\":[{\"type\":\"named\",\"name\":\"http://purl.obolibrary.org/obo/DOID_162\"},"
            + "{\"type\":\"named\",\"name\":\"" + NON_PROFIT + "\"}]}";
        String expResult = "Samples may only be used for the purpose of studying cancer. "
            + "In addition, samples may not be used for commercial purposes.";
        String result = service.translateSample(restrictionStr);
        assertEquals(expResult, result);
    }

}
