package org.broadinstitute.dsde.consent.ontology.service;

import com.codahale.metrics.health.HealthCheck;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class ElasticSearchHealthCheckTest {

    private ElasticSearchHealthCheck elasticSearchHealthCheck;
    private ClientAndServer server;

    @Before
    public void setUpClass() {
        ElasticSearchConfiguration configuration = new ElasticSearchConfiguration();
        configuration.setIndex("test-ontology");
        configuration.setServers(Collections.singletonList("localhost"));
        elasticSearchHealthCheck = new ElasticSearchHealthCheck(configuration);
        server = startClientAndServer(9200);
    }

    @After
    public void shutDown() {
        if (server != null && server.isRunning()) {
            server.stop();
        }
    }

    private void mockRequest(String color, Boolean timedOut) {
        server.reset();
        server.when(request()).respond(response().withStatusCode(200).
            withBody("{\n" +
                "  \"cluster_name\": \"docker-cluster\",\n" +
                "  \"status\": \"" + color + "\",\n" +
                "  \"timed_out\": " + timedOut + ",\n" +
                "  \"number_of_nodes\": 1,\n" +
                "  \"number_of_data_nodes\": 1,\n" +
                "  \"active_primary_shards\": 2,\n" +
                "  \"active_shards\": 2,\n" +
                "  \"relocating_shards\": 0,\n" +
                "  \"initializing_shards\": 0,\n" +
                "  \"unassigned_shards\": 2,\n" +
                "  \"delayed_unassigned_shards\": 0,\n" +
                "  \"number_of_pending_tasks\": 0,\n" +
                "  \"number_of_in_flight_fetch\": 0,\n" +
                "  \"task_max_waiting_in_queue_millis\": 0,\n" +
                "  \"active_shards_percent_as_number\": 50\n" +
                "}"));
    }

    @Test
    public void testCheckTimeOut() throws Exception {
        mockRequest("red", true);
        HealthCheck.Result result = elasticSearchHealthCheck.check();
        assertFalse(result.isHealthy());
        assertTrue(result.getMessage().contains("HealthCheck timed out"));
    }

    @Test
    public void testCheckStatusRed() throws Exception {
        mockRequest("red", false);
        HealthCheck.Result result = elasticSearchHealthCheck.check();
        assertFalse(result.isHealthy());
        assertTrue(result.getMessage().contains("ClusterHealth is RED"));
    }

    @Test
    public void testCheckStatusYellow() throws Exception {
        mockRequest("yellow", false);
        HealthCheck.Result result = elasticSearchHealthCheck.check();
        assertFalse(result.isHealthy());
        assertTrue(result.getMessage().contains("ClusterHealth is YELLOW"));
    }

    @Test
    public void testCheckStatusOK() throws Exception {
        mockRequest("green", false);
        HealthCheck.Result result = elasticSearchHealthCheck.check();
        assertTrue(result.isHealthy());
    }
}
