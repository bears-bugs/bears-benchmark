package org.broadinstitute.dsde.consent.ontology.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {

    @JsonProperty
    private String message;

    @JsonProperty
    private Integer code;

    public ErrorResponse() {
    }

    public ErrorResponse(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String error) {
        this.message = error;
    }
}
