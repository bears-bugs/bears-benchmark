package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import org.broadinstitute.dsde.consent.ontology.datause.api.OntologyTermSearchAPI;

import java.io.IOException;
import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.UseRestrictionVisitor;

public class Only extends UseRestriction {

    private String type = "only";
    private String property;
    private UseRestriction target;

    public Only() {
    }

    public Only(String prop, UseRestriction obj) {
        this.property = prop;
        this.target = obj;
    }

    @JsonIgnore
    public String getType() {
        return type;
    }

    public String getProperty() {
        return property;
    }

    public UseRestriction getTarget() {
        return target;
    }

    public void setProperty(String p) {
        property = p;
    }

    public void setTarget(UseRestriction r) {
        target = r;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, property, target);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Only &&
                Objects.equal(this.property, ((Only) o).property) &&
                Objects.equal(this.target, ((Only) o).target);
    }

    @Override
    public OntClass createOntologicalRestriction(OntModel model) {
        Property prop = model.createProperty(property);
        Resource objectClass = target.createOntologicalRestriction(model);
        return model.createAllValuesFromRestriction(null, prop, objectClass);
    }

    public boolean visitAndContinue(UseRestrictionVisitor visitor) {
        return target.visit(visitor);
    }

    @JsonIgnore
    public String getDescriptiveLabel(OntologyTermSearchAPI api) throws IOException {
        return "Only the following:\n" + target.getDescriptiveLabel(api);
    }

}
