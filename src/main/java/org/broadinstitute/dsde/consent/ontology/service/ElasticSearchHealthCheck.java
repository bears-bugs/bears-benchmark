package org.broadinstitute.dsde.consent.ontology.service;

import com.codahale.metrics.health.HealthCheck;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.dropwizard.lifecycle.Managed;
import org.apache.commons.io.IOUtils;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;

public class ElasticSearchHealthCheck extends HealthCheck implements Managed {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ElasticSearchHealthCheck.class);
    private ElasticSearchConfiguration configuration;
    private JsonParser parser = new JsonParser();
    private RestClient client;


    @Override
    public void start() throws Exception { }

    @Override
    public void stop() throws Exception {
        if (client != null) {
            client.close();
        }
    }

    public ElasticSearchHealthCheck(ElasticSearchConfiguration config) {
        this.configuration = config;
        this.client = ElasticSearchSupport.createRestClient(this.configuration);
    }

    @Override
    protected Result check() throws Exception {
        try {
            Response esResponse = client.performRequest("GET",
                ElasticSearchSupport.getClusterHealthPath(configuration.getIndex()),
                ElasticSearchSupport.jsonHeader);
            if (esResponse.getStatusLine().getStatusCode() != 200) {
                logger.error("Invalid health check request: " + esResponse.getStatusLine().getReasonPhrase());
                throw new InternalServerErrorException(esResponse.getStatusLine().getReasonPhrase());
            }
            String stringResponse = IOUtils.toString(esResponse.getEntity().getContent());
            JsonObject jsonResponse = parser.parse(stringResponse).getAsJsonObject();
            Boolean timeout = jsonResponse.get("timed_out").getAsBoolean();
            String status = jsonResponse.get("status").getAsString();
            if (timeout) {
                return Result.unhealthy("HealthCheck timed out");
            }
            if (status.equalsIgnoreCase("red")) {
                return Result.unhealthy("ClusterHealth is RED\n" + jsonResponse.toString());
            }
            if (status.equalsIgnoreCase("yellow")) {
                return Result.unhealthy("ClusterHealth is YELLOW\n" + jsonResponse.toString());
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new InternalServerErrorException();
        }
        return Result.healthy("ClusterHealth is GREEN");
    }
}
