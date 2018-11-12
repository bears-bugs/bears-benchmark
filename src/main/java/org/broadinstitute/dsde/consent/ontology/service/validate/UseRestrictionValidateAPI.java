package org.broadinstitute.dsde.consent.ontology.service.validate;

import com.google.inject.ImplementedBy;

@ImplementedBy(UseRestrictionValidateImpl.class)
public interface UseRestrictionValidateAPI {

    ValidateResponse validateUseRestriction(String useRestriction) throws Exception;

}
