package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.google.common.base.Objects;

public class Consent {

    private String id;
    private UseRestriction restriction;

    public Consent() {
        this.id = null;
        this.restriction = null;
    }

    public Consent(String id, UseRestriction restriction) {
        this.id = id;
        this.restriction = restriction;
    }

    public String getId() { return id; }
    public UseRestriction getRestriction() { return restriction; }

    public void setId(String id) { this.id = id; }
    public void setRestriction(UseRestriction restriction) { this.restriction = restriction; }

    public int hashCode() {
        return Objects.hashCode(
            this.getId(), this.getRestriction());
    }

    public boolean equals(Object o) {
        if(!(o instanceof Consent)) { return false; }
        Consent otherConsent = (Consent) o;
        return Objects.equal(this.getId(), otherConsent.getId()) &&
            Objects.equal(this.getRestriction(), otherConsent.getRestriction());
    }

}
