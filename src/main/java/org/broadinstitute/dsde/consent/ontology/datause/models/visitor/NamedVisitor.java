package org.broadinstitute.dsde.consent.ontology.datause.models.visitor;

import java.util.ArrayList;
import java.util.Collection;
import org.broadinstitute.dsde.consent.ontology.datause.models.Named;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;

public class NamedVisitor implements UseRestrictionVisitor {

    private Collection<String> namedClasses = new ArrayList<>();

    @Override
    public void startChildren() {

    }

    @Override
    public void endChildren() {

    }

    public void addNamed(Named useRestriction){
        namedClasses.add(useRestriction.getName());
    }

    @Override
    public boolean visit(UseRestriction r) {
        if(r instanceof Named){
            namedClasses.add(((Named) r).getName());
        }
        return true;
    }

    public Collection<String> getNamedClasses() {
        return namedClasses;
    }
}
