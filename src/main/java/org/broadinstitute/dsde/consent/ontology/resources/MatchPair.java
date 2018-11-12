package org.broadinstitute.dsde.consent.ontology.resources;

import com.google.gson.Gson;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;

public class MatchPair {

    public UseRestriction purpose;

    public UseRestriction consent;

    public MatchPair(UseRestriction purpose, UseRestriction consent) {
        this.purpose = purpose;
        this.consent = consent;
    }

    public MatchPair() {
    }

    public UseRestriction getPurpose() {
        return purpose;
    }

    public void setPurpose(UseRestriction purpose) {
        this.purpose = purpose;
    }

    public UseRestriction getConsent() {
        return consent;
    }

    public void setConsent(UseRestriction consent) {
        this.consent = consent;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
