package org.broadinstitute.dsde.consent.ontology;

import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;

import java.io.IOException;

public class MockTranslationHelper implements TextTranslationService {

    @Override
    public String translateSample(String restrictionStr) throws IOException {
        return "translated sampleset";  
    }

    @Override
    public String translatePurpose(String restrictionStr) throws IOException {
        return "translated purpose";  
    }

}
