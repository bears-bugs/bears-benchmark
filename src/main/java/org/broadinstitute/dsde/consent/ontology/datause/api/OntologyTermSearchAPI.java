package org.broadinstitute.dsde.consent.ontology.datause.api;

import com.google.inject.ImplementedBy;
import org.broadinstitute.dsde.consent.ontology.datause.models.OntologyTerm;

import java.io.IOException;

@ImplementedBy(LuceneOntologyTermSearchAPI.class)
public interface OntologyTermSearchAPI {
    OntologyTerm findById(String id) throws IOException;
}
