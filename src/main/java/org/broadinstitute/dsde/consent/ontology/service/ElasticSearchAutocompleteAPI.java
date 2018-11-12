package org.broadinstitute.dsde.consent.ontology.service;

import com.google.gson.*;
import io.dropwizard.lifecycle.Managed;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermParent;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ElasticSearchAutocompleteAPI implements AutocompleteAPI, Managed {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchAutocompleteAPI.class);
    private final ElasticSearchConfiguration configuration;
    private JsonParser parser = new JsonParser();
    private Gson gson = new GsonBuilder().create();
    private RestClient client;

    @Override
    public void start() throws Exception { }

    @Override
    public void stop() throws Exception {
        if (client != null) {
            client.close();
        }
    }

    public ElasticSearchAutocompleteAPI(ElasticSearchConfiguration configuration) {
        this.configuration = configuration;
        this.client = ElasticSearchSupport.createRestClient(configuration);
    }

    /**
     * Basic search execution method that queries ES and returns results.
     *
     * @param query      Query string in the form of an ES json query object
     * @param limit      How many to limit the results to
     * @param thinFilter When true, we provide the minimal amount of information to keep the API responses thin. When
     *                   false, we provide the fully populated object.
     * @return List of TermResources that match the query
     */
    private List<TermResource> executeSearch(String query, int limit, Boolean thinFilter) {
        List<TermResource> termList = new ArrayList<>();
        try {
            Map<String, String> params = new HashMap<>();
            params.put("size", String.valueOf(limit));
            Response esResponse = client.performRequest(
                "GET",
                ElasticSearchSupport.getSearchPath(configuration.getIndex()),
                params,
                new NStringEntity(query, ContentType.APPLICATION_JSON),
                ElasticSearchSupport.jsonHeader);
            if (esResponse.getStatusLine().getStatusCode() != 200) {
                logger.error("Invalid search request: " + esResponse.getStatusLine().getReasonPhrase());
                throw new InternalServerErrorException();
            }
            String stringResponse = IOUtils.toString(esResponse.getEntity().getContent());
            JsonObject jsonResponse = parser.parse(stringResponse).getAsJsonObject();
            JsonObject hitsSummary = jsonResponse.getAsJsonObject("hits");
            if (hitsSummary != null) {
                JsonArray hits = hitsSummary.getAsJsonArray("hits");
                for (JsonElement hit : hits) {
                    JsonElement data = hit.getAsJsonObject().getAsJsonObject("_source");
                    TermResource resource = gson.fromJson(data, TermResource.class);
                    if (thinFilter) {
                        resource.setOntology(null);
                        resource.setUsable(null);
                        resource.setParents(null);
                    }
                    termList.add(resource);
                }
            } else {
                logger.error("Unable to parse 'hits' from: " + stringResponse);
            }
        } catch (IOException e) {
            logger.error("Unable to parse query response for " + query + "\n" + e.getMessage());
            throw new RuntimeException(e);
        }
        return termList;
    }

    /**
     * Retrieve a single document from ES by its exact id.
     *
     * @param query      id of the document to return
     * @param thinFilter When true, we provide the minimal amount of information to keep the API responses thin. When
     *                   false, we provide the fully populated object.
     * @return List of TermResources that match the query. Will have either zero or one result.
     */
    private List<TermResource> executeGet(String query, Boolean thinFilter) {
        List<TermResource> termList = new ArrayList<>();
        try {
            Response esResponse = client.performRequest(
                    "GET",
                    ElasticSearchSupport.getTermPath(configuration.getIndex()) + "/" + java.net.URLEncoder.encode(query, "UTF-8"),
                    ElasticSearchSupport.jsonHeader);
            if (esResponse.getStatusLine().getStatusCode() != 200) {
                logger.error("Invalid search request: " + esResponse.getStatusLine().getReasonPhrase());
                throw new InternalServerErrorException();
            }
            String stringResponse = IOUtils.toString(esResponse.getEntity().getContent());
            JsonObject jsonResponse = parser.parse(stringResponse).getAsJsonObject();
            JsonElement data = jsonResponse.getAsJsonObject("_source");
            TermResource resource = gson.fromJson(data, TermResource.class);
            if (thinFilter) {
                resource.setOntology(null);
                resource.setUsable(null);
                resource.setParents(null);
            }
            termList.add(resource);
        } catch (ResponseException re) {
            if (re.getResponse().getStatusLine().getStatusCode() == 404) {
                // 404 is an expected case from ES, indicating the ID was not found
                return termList;
            } else {
                logger.error("Unable to parse query response for " + query + "\n" + re.getMessage());
                throw new RuntimeException(re);
            }
        } catch (IOException e) {
            logger.error("Unable to parse query response for " + query + "\n" + e.getMessage());
            throw new RuntimeException(e);
        }
        return termList;
    }

    @Override
    public List<TermResource> lookup(String query, int limit) {
        return lookup(Collections.emptyList(), query, limit);
    }

    @Override
    public List<TermResource> lookup(Collection<String> tags, String query, int limit) {
        return executeSearch(ElasticSearchSupport.buildFilterQuery(query, tags), limit, true);
    }

    @Override
    public List<TermResource> lookupById(String query) throws IOException {
        List<TermResource> terms = executeGet(query, false);

        Collection<String> parentIds = terms.stream().
            flatMap(p -> Optional.ofNullable(p.getParents()).orElse(new ArrayList<>()).stream()).
            map(TermParent::getId).
            collect(Collectors.toList());

        Collection<TermResource> parentTerms = parentIds.stream().
            flatMap(t -> executeGet(t,true).stream()).
            collect(Collectors.toList());

        // Populate each of the parent nodes with more complete information
        for (TermResource term : terms) {
            for (TermParent p : Optional.ofNullable(term.getParents()).orElse(new ArrayList<>())) {
                Optional<TermResource> parentTermResource = parentTerms.stream().
                    filter(x -> x.getId().equals(p.getId())).
                    findFirst();
                if (parentTermResource.isPresent()) {
                    p.setLabel(parentTermResource.get().getLabel());
                    p.setSynonyms(parentTermResource.get().getSynonyms());
                    p.setDefinition(parentTermResource.get().getDefinition());
                }
            }
        }

        return terms;
    }

}
