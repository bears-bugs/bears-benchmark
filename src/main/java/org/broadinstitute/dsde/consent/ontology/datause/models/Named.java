package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import org.broadinstitute.dsde.consent.ontology.datause.api.OntologyTermSearchAPI;

import java.io.IOException;

import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.NamedVisitor;
import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.UseRestrictionVisitor;

public class Named extends UseRestriction {

    private String type = "named";

    private String name;

    public Named() {
    }

    public Named(String name) {
        this.name = name;
    }

    @JsonIgnore
    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, name);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Named &&
                Objects.equal(this.name, ((Named) o).name);
    }

    @Override
    public OntClass createOntologicalRestriction(OntModel model) {
        return model.createClass(name);
    }

    public boolean visitAndContinue(UseRestrictionVisitor visitor) {
        if(visitor instanceof NamedVisitor){
            ((NamedVisitor) visitor).addNamed(this);
        }
        return true;
    }

    @JsonIgnore
    public String getDescriptiveLabel(OntologyTermSearchAPI api) throws IOException {
        OntologyTerm term = api.findById(getName());
        if (getName().toLowerCase().contains("www.broadinstitute.org")) {
            return (term.getComment() == null) ? capitalize(term.getLabel()) : capitalize(term.getComment());
        }
        return capitalize(term.getLabel());
    }

}
