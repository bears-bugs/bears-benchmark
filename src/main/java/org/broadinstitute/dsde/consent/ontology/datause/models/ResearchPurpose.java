package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.google.common.base.Objects;

public class ResearchPurpose {

    private String id;
    private UseRestriction purpose;

    public ResearchPurpose() {
        this.id = null;
        this.purpose = null;
    }

    public ResearchPurpose(String id, UseRestriction purpose) {
        this.id = id;
        this.purpose = purpose;
    }

    public String getId() { return id; }
    public UseRestriction getPurpose() { return purpose; }

    public void setId(String id) { this.id = id; }
    public void setPurpose(UseRestriction purpose) { this.purpose = purpose; }

    public int hashCode() {
        return Objects.hashCode(
            this.getId(), this.getPurpose());
    }

    public boolean equals(Object o) {
        if(!(o instanceof ResearchPurpose)) { return false; }
        ResearchPurpose otherPurpose = (ResearchPurpose) o;
        return Objects.equal(this.getId(), otherPurpose.getId()) &&
            Objects.equal(this.getPurpose(), otherPurpose.getPurpose());
    }

}
