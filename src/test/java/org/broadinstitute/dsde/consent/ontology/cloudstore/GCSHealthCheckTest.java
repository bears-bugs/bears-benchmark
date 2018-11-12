package org.broadinstitute.dsde.consent.ontology.cloudstore;

import com.codahale.metrics.health.HealthCheck;
import com.google.api.services.storage.model.Bucket;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class GCSHealthCheckTest {

    private GCSHealthCheck healthCheck;

    @Mock
    private GCSStore store;

    @Before
    public void setUpClass() throws Exception {
        MockitoAnnotations.initMocks(this);
        healthCheck = new GCSHealthCheck(store);
    }

    @Test
    public void testBucketExists() throws IOException, GeneralSecurityException {
        when(store.getBucketMetadata()).thenReturn(new Bucket());

        HealthCheck.Result result = healthCheck.execute();
        assertTrue(result.isHealthy());
    }

    @Test
    public void testBucketMissing() throws Exception {
        when(store.getBucketMetadata()).thenReturn(null);

        HealthCheck.Result result = healthCheck.execute();
        assertFalse(result.isHealthy());
        assertTrue(result.getMessage().contains("GCS bucket unreachable"));
    }
}
