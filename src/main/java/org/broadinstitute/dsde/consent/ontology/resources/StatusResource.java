package org.broadinstitute.dsde.consent.ontology.resources;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("status")
public class StatusResource {

    private HealthCheckRegistry healthChecks;

    public StatusResource(HealthCheckRegistry healthChecks) {
        this.healthChecks = healthChecks;
    }

    @GET
    @Produces("application/json")
    public Response getStatus() {
        Map<String, HealthCheck.Result> results = healthChecks.runHealthChecks();
        return Response.ok(results).build();
    }

}