/*
 * Copyright 2016-2018, EnMasse authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */

package io.enmasse.controller.api.osb.v2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import io.enmasse.controller.api.osb.v2.catalog.CatalogResponse;
import io.enmasse.controller.api.osb.v2.catalog.OSBCatalogService;
import io.enmasse.controller.api.osb.v2.catalog.Plan;
import io.enmasse.controller.api.osb.v2.catalog.Service;
import io.enmasse.k8s.api.TestAddressSpaceApi;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import javax.ws.rs.core.Response;

@Ignore
public class CatalogServiceTest extends OSBTestBase {

    @Test
    public void testCatalog() throws IOException {
        OSBCatalogService catalogService = new OSBCatalogService(new TestAddressSpaceApi(), "controller");

        Response response = catalogService.getCatalog(getSecurityContext());
        CatalogResponse catalogResponse = (CatalogResponse) response.getEntity();
        List<Service> services = catalogResponse.getServices();

        assertThat(services.size(), is(4));
        assertService(services.get(0), "enmasse-anycast", "standard");
        assertService(services.get(1), "enmasse-multicast", "standard");
        assertService(services.get(2), "enmasse-queue", "inmemory", "persisted", "pooled-inmemory", "pooled-persisted");
        assertService(services.get(3), "enmasse-topic", "inmemory", "persisted");

        Service service = services.get(2);
        Plan plan = service.getPlans().get(0);
        assertThat(plan.getMetadata().get("displayName"), is("In-memory"));
        assertThat(plan.getDescription(), is("Creates a standalone broker cluster for queues. Messages are not persisted on stable storage."));
    }

    private void assertService(Service service, String name, String... planNames) {
        assertThat(service.getName(), is(name));
        assertThat(service.getPlans().size(), is(planNames.length));
        for (int i = 0; i < planNames.length; i++) {
            String planName = planNames[i];
            Plan plan = service.getPlans().get(i);
            assertThat(plan.getName(), is(planName));
        }
    }
}
