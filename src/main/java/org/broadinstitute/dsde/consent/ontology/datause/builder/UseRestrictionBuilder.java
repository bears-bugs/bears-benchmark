package org.broadinstitute.dsde.consent.ontology.datause.builder;

import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;

/**
 * Implementors generate a data use restriction specific to case-specific business rules
 */
public interface UseRestrictionBuilder {

    /**
     * Generate an appropriate UseRestriction for all possible cases of DataUse.
     *
     * @param dataUse The DataUseSchema
     * @return UseRestriction
     */
    UseRestriction buildUseRestriction(DataUse dataUse);

}
