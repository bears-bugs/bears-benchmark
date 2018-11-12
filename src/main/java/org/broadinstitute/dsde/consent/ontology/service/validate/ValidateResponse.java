package org.broadinstitute.dsde.consent.ontology.service.validate;

import java.util.ArrayList;
import java.util.Collection;

public class ValidateResponse {

    private boolean valid = false;
    private String useRestriction;
    private Collection<String> errors;

    public ValidateResponse(boolean valid, String useRestriction) {
        this.valid = valid;
        this.useRestriction = useRestriction;
        this.errors = new ArrayList<>();
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getUseRestriction() {
        return useRestriction;
    }

    public void setUseRestriction(String useRestriction) {
        this.useRestriction = useRestriction;
    }

    public Collection<String> getErrors() {
        return errors;
    }

    public void addError(String error) {
        this.errors.add(error);
    }


    public void addErrors(Collection<String> errors) {
        this.errors.addAll(errors);
    }
}
