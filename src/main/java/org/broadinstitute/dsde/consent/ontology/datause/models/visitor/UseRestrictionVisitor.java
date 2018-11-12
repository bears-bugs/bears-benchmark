package org.broadinstitute.dsde.consent.ontology.datause.models.visitor;

import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;

public interface UseRestrictionVisitor {

    void startChildren();
    void endChildren();
    boolean visit(UseRestriction r);

}
