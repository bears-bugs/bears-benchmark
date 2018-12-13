package com.hubspot.baragon.service.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.Inject;

@Path(MetricsResource.PATH)
@Produces({ MediaType.APPLICATION_JSON })
public class MetricsResource {
  public static final String PATH = "/metrics";

  private final MetricRegistry registry;

  @Inject
  public MetricsResource(MetricRegistry registry) {
    this.registry = registry;
  }

  @GET
  public MetricRegistry getRegistry() {
    return registry;
  }
}
