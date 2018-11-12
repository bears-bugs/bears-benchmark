package org.broadinstitute.dsde.consent.ontology.cloudstore;

import com.codahale.metrics.health.HealthCheck;
import com.google.api.services.storage.model.Bucket;

public class GCSHealthCheck extends HealthCheck {

    private GCSStore store;

    public GCSHealthCheck(GCSStore store) {
        this.store = store;
    }

    @Override
    protected Result check() throws Exception {

        Bucket bucket;

        try {
            bucket = store.getBucketMetadata();
        } catch (Exception e) {
            return Result.unhealthy("GCS bucket unreachable or does not exist: " + e.getMessage());
        }

        return (bucket != null) ? Result.healthy() : Result.unhealthy("GCS bucket unreachable or does not exist.");
    }

}
