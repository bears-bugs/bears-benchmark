package org.broadinstitute.dsde.consent.ontology;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.broadinstitute.dsde.consent.ontology.configurations.*;

public class OntologyConfiguration extends Configuration {


    public OntologyConfiguration() {}

    @JsonProperty
    private final ElasticSearchConfiguration elasticSearch = new ElasticSearchConfiguration();

    @JsonProperty
    private final StoreConfiguration googleStore = new StoreConfiguration();

    @JsonProperty
    private final StoreOntologyConfiguration storeOntology = new StoreOntologyConfiguration();

    @JsonProperty
    private final CorsConfiguration cors = new CorsConfiguration();


    public ElasticSearchConfiguration getElasticSearchConfiguration() {
        return elasticSearch;
    }

    public StoreConfiguration getCloudStoreConfiguration() { return googleStore; }

    public StoreOntologyConfiguration getStoreOntologyConfiguration() {
        return storeOntology;
    }

    public CorsConfiguration getCorsConfiguration() {
        return cors;
    }

}
