package org.broadinstitute.dsde.consent.ontology;

import com.google.inject.*;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import org.broadinstitute.dsde.consent.ontology.cloudstore.GCSHealthCheck;
import org.broadinstitute.dsde.consent.ontology.cloudstore.GCSStore;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteAPI;
import org.broadinstitute.dsde.consent.ontology.service.ElasticSearchAutocompleteAPI;
import org.broadinstitute.dsde.consent.ontology.service.StoreOntologyService;
import org.broadinstitute.dsde.consent.ontology.service.validate.UseRestrictionValidateAPI;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class OntologyModule extends AbstractModule {

    @Inject
    private final OntologyConfiguration config;
    @Inject
    private final Environment environment;

    public OntologyModule(OntologyConfiguration configuration, Environment environment){
        this.config = configuration;
        this.environment = environment;
    }

    @Override
    protected void configure() {
        bind(Configuration.class).toInstance(config);
        bind(Environment.class).toInstance(environment);
        bind(UseRestrictionValidateAPI.class).in(Scopes.SINGLETON);
        bind(TextTranslationService.class).in(Scopes.SINGLETON);
        bind(UseRestrictionValidateAPI.class).in(Scopes.SINGLETON);
        bind(TextTranslationService.class).in(Scopes.SINGLETON);
    }

    @Provides
    @Singleton
    public AutocompleteAPI providesAPI() {
        return new ElasticSearchAutocompleteAPI(config.getElasticSearchConfiguration());
    }

    @Provides
    @Singleton
    public StoreOntologyService providesStore() {
        GCSStore googleStore;
        try {
            googleStore = new GCSStore(config.getCloudStoreConfiguration());
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException(e);
        }
        environment.healthChecks().register("google-cloud-storage", new GCSHealthCheck(googleStore));
        return new StoreOntologyService(googleStore,
            config.getStoreOntologyConfiguration().getBucketSubdirectory(),
            config.getStoreOntologyConfiguration().getConfigurationFileName());
    }
}

