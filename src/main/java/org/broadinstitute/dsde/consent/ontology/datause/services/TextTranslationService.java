package org.broadinstitute.dsde.consent.ontology.datause.services;

import com.google.inject.ImplementedBy;
import java.io.IOException;

@ImplementedBy(TextTranslationServiceImpl.class)
public interface TextTranslationService {
    
    String translateSample(String restrictionStr) throws IOException;
    
    String translatePurpose(String restrictionStr) throws IOException;
}
