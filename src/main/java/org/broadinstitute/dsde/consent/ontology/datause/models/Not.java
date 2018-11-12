package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import org.broadinstitute.dsde.consent.ontology.datause.api.OntologyTermSearchAPI;

import java.io.IOException;
import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.UseRestrictionVisitor;

public class Not extends UseRestriction {

    private String type = "not";

    private UseRestriction operand;

    public Not() {
    }

    public Not(UseRestriction operand) {
        this.operand = operand;
    }

    @JsonIgnore
    public String getType() {
        return type;
    }

    public UseRestriction getOperand() {
        return operand;
    }

    public void setOperand(UseRestriction op) {
        operand = op;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, operand);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Not &&
                Objects.equal(this.operand, ((Not) o).operand);
    }

    @Override
    public OntClass createOntologicalRestriction(OntModel model) {
        return model.createComplementClass(null, operand.createOntologicalRestriction(model));
    }

    public boolean visitAndContinue(UseRestrictionVisitor visitor) {
        return visitor.visit(operand);
    }

    @JsonIgnore
    public String getDescriptiveLabel(OntologyTermSearchAPI api) throws IOException {
        return "None of the following:\n" + operand.getDescriptiveLabel(api);
    }

}
