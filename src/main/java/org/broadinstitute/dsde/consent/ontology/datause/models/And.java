package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import org.apache.commons.collections4.CollectionUtils;
import org.broadinstitute.dsde.consent.ontology.datause.api.OntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.UseRestrictionVisitor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class And extends UseRestriction {

    private String type = "and";

    private UseRestriction[] operands;

    public And() {
    }

    public And(UseRestriction... operands) {
        this.operands = operands;
    }

    @JsonIgnore
    public String getType() {
        return type;
    }

    public void setOperands(UseRestriction[] ops) {
        this.operands = ops.clone();
    }

    public UseRestriction[] getOperands() {
        return operands;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, operands);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof And &&
            CollectionUtils.containsAll(
                Arrays.asList(this.operands),
                Arrays.asList(((And) o).operands)) &&
            CollectionUtils.containsAll(
                Arrays.asList(((And) o).operands),
                Arrays.asList(this.operands)) &&
            this.operands.length == (((And) o).operands).length;
    }

    @Override
    public OntClass createOntologicalRestriction(OntModel model) {
        RDFNode[] nodes = new RDFNode[operands.length];
        for(int i = 0; i < nodes.length; i++) {
            nodes[i] = operands[i].createOntologicalRestriction(model);
        }
        RDFList list = model.createList(nodes);
        return model.createIntersectionClass(null, list);
    }

    public boolean visitAndContinue(UseRestrictionVisitor visitor) {
        for (UseRestriction child : operands) {
            if (!child.visit(visitor)) {
                return false;
            }
        }
        return true;
    }

    @JsonIgnore
    public String getDescriptiveLabel(OntologyTermSearchAPI api) throws IOException {
        List<String> clauses = Arrays.stream(operands).map(o -> {
            try { return wrapListItem(o.getDescriptiveLabel(api)); }
            catch(Exception e) { return wrapListItem(o.toString()); }
        }).collect(Collectors.toList());
        return "All of the following:" + wrapList(clauses.stream().collect(Collectors.joining("\n")));
    }

}
