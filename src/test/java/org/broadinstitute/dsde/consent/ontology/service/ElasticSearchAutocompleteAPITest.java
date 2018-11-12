package org.broadinstitute.dsde.consent.ontology.service;

import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpResponse;

import java.util.Collections;
import java.util.List;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class ElasticSearchAutocompleteAPITest {

    private ElasticSearchAutocompleteAPI autocompleteAPI;
    private static final String INDEX_NAME = "local-ontology";
    private ClientAndServer server;

    @Before
    public void setUp() throws Exception {
        ElasticSearchConfiguration configuration = new ElasticSearchConfiguration();
        configuration.setIndex(INDEX_NAME);
        configuration.setServers(Collections.singletonList("localhost"));
        autocompleteAPI = new ElasticSearchAutocompleteAPI(configuration);
        server = startClientAndServer(9200);
    }

    @After
    public void shutDown() throws Exception {
        if (server != null && server.isRunning()) {
            server.stop();
        }
    }

    private void mockResponse(HttpResponse response) {
        server.reset();
        server.when(request()).respond(response);
    }

    @Test
    public void testLookup() throws Exception {
        mockResponse(response().withStatusCode(200).withBody(cancerJson));
        List<TermResource> termResource = autocompleteAPI.lookup("cancer", 1);
        Assert.assertTrue(termResource.size() == 1);
        Assert.assertTrue(termResource.get(0).getSynonyms().contains("primary cancer"));
    }

    @Test
    public void testLookupWithTags() throws Exception {
        mockResponse(response().withStatusCode(200).withBody(cancerJson));
        List<TermResource> termResource = autocompleteAPI.lookup(Collections.singletonList("tag"), "cancer", 1);
        Assert.assertTrue(termResource.size() == 1);
        Assert.assertTrue(termResource.get(0).getSynonyms().contains("primary cancer"));
    }

    @Test
    public void  testLookupById() throws Exception {
        mockResponse(response().withStatusCode(200).withBody(cancerGetJson));
        List<TermResource> termResource = autocompleteAPI.lookupById("http://purl.obolibrary.org/obo/DOID_162");
        Assert.assertTrue(termResource.size() == 1);
        Assert.assertTrue(termResource.get(0).getSynonyms().contains("primary cancer"));
    }

    // mock response for a search
    private static String cancerJson = "{\n" +
        "  \"took\": 15,\n" +
        "  \"timed_out\": false,\n" +
        "  \"_shards\": {\n" +
        "    \"total\": 5,\n" +
        "    \"successful\": 5,\n" +
        "    \"failed\": 0\n" +
        "  },\n" +
        "  \"hits\": {\n" +
        "    \"total\": 1,\n" +
        "    \"max_score\": 100,\n" +
        "    \"hits\": [\n" +
        "      {\n" +
        "        \"_index\": \"local-ontology\",\n" +
        "        \"_type\": \"ontology_term\",\n" +
        "        \"_id\": \"http://purl.obolibrary.org/obo/DOID_162\",\n" +
        "        \"_score\": 21.416782,\n" +
        "        \"_source\": {\n" +
        "          \"id\": \"http://purl.obolibrary.org/obo/DOID_162\",\n" +
        "          \"ontology\": \"Disease\",\n" +
        "          \"synonyms\": [\n" +
        "            \"primary cancer\",\n" +
        "            \"malignant tumor \",\n" +
        "            \"malignant neoplasm\"\n" +
        "          ],\n" +
        "          \"label\": \"cancer\",\n" +
        "          \"definition\": \"A disease of cellular proliferation that is malignant and primary, characterized by uncontrolled cellular proliferation, local cell invasion and metastasis.\",\n" +
        "          \"usable\": true,\n" +
        "          \"parents\": [\n" +
        "            {\n" +
        "              \"id\": \"http://purl.obolibrary.org/obo/DOID_14566\",\n" +
        "              \"order\": 1\n" +
        "            },\n" +
        "            {\n" +
        "              \"id\": \"http://purl.obolibrary.org/obo/DOID_4\",\n" +
        "              \"order\": 2\n" +
        "            }\n" +
        "          ]\n" +
        "        }\n" +
        "      }\n" +
        "    ]\n" +
        "  }\n" +
        "}";

    // mock response for a document get-by-id
    private static String cancerGetJson = "{\n" +
            "  \"_index\": \"ontology\",\n" +
            "  \"_type\": \"ontology_term\",\n" +
            "  \"_id\": \"http://purl.obolibrary.org/obo/DOID_162\",\n" +
            "  \"_version\": 32,\n" +
            "  \"found\": true,\n" +
            "  \"_source\": {\n" +
            "    \"id\": \"http://purl.obolibrary.org/obo/DOID_162\",\n" +
            "    \"ontology\": \"Disease\",\n" +
            "    \"synonyms\": [\n" +
            "      \"primary cancer\",\n" +
            "      \"malignant tumor \",\n" +
            "      \"malignant neoplasm\"\n" +
            "    ],\n" +
            "    \"label\": \"cancer\",\n" +
            "    \"definition\": \"A disease of cellular proliferation that is malignant and primary, characterized by uncontrolled cellular proliferation, local cell invasion and metastasis.\",\n" +
            "    \"usable\": true,\n" +
            "    \"parents\": [\n" +
            "      {\n" +
            "        \"id\": \"http://purl.obolibrary.org/obo/DOID_14566\",\n" +
            "        \"order\": 1\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"http://purl.obolibrary.org/obo/DOID_4\",\n" +
            "        \"order\": 2\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

}
