package org.broadinstitute.dsde.consent.ontology.enumerations;

public enum UseRestrictionKeys {

    TYPE("type"),
    OPERANDS("operands"),
    OPERAND("operand"),
    NAME("name");

    private String value;

    UseRestrictionKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
