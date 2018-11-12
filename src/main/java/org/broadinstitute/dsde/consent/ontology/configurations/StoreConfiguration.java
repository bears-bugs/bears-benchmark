package org.broadinstitute.dsde.consent.ontology.configurations;

import javax.validation.constraints.NotNull;

public class StoreConfiguration {

    @NotNull
    public String password;

    @NotNull
    public String endpoint;

    @NotNull
    public String bucket;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

}
