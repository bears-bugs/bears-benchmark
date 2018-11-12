package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.OWL;
import org.broadinstitute.dsde.consent.ontology.datause.api.OntologyTermSearchAPI;

import java.io.IOException;
import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.UseRestrictionVisitor;

public class Nothing extends UseRestriction {

    private String type = "nothing";

    public Nothing() {
    }

    @JsonIgnore
    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Nothing;
    }

    @Override
    public OntClass createOntologicalRestriction(OntModel model) {
        return model.getOntClass(OWL.Nothing.getURI());
    }

    public boolean visitAndContinue(UseRestrictionVisitor visitor) {
        return true;
    }

    @JsonIgnore
    public String getDescriptiveLabel(OntologyTermSearchAPI api) throws IOException {
        return "Nothing";
    }

}