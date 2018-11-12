package org.broadinstitute.dsde.consent.ontology;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.broadinstitute.dsde.consent.ontology.resources.AllTermsResource;
import org.broadinstitute.dsde.consent.ontology.resources.DataUseResource;
import org.broadinstitute.dsde.consent.ontology.resources.MatchResource;
import org.broadinstitute.dsde.consent.ontology.resources.OntologySearchResource;
import org.broadinstitute.dsde.consent.ontology.resources.StatusResource;
import org.broadinstitute.dsde.consent.ontology.resources.SwaggerResource;
import org.broadinstitute.dsde.consent.ontology.resources.TranslateResource;
import org.broadinstitute.dsde.consent.ontology.resources.validate.ValidationResource;
import org.broadinstitute.dsde.consent.ontology.service.ElasticSearchHealthCheck;
import org.dhatim.dropwizard.sentry.logging.SentryBootstrap;
import org.dhatim.dropwizard.sentry.logging.UncaughtExceptionHandlers;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

/**
 * Top-level entry point to the entire application.
 *
 * See the Dropwizard docs here:
 * https://dropwizard.github.io/dropwizard/manual/core.html
 *
 */
public class OntologyApp extends Application<OntologyConfiguration> {

    public static void main(String[] args) throws Exception {
        String dsn = System.getProperties().getProperty("sentry.dsn");
        if (null != dsn && !dsn.isEmpty()) {
            SentryBootstrap.bootstrap(dsn);
            Thread.currentThread().setUncaughtExceptionHandler(UncaughtExceptionHandlers.systemExit());
        }
        new OntologyApp().run(args);
    }

    @Override
    public void run(OntologyConfiguration config, Environment env) {

        Injector injector = Guice.createInjector(new OntologyModule(config, env));
        env.jersey().register(injector.getInstance(AllTermsResource.class));
        env.jersey().register(injector.getInstance(MatchResource.class));
        env.jersey().register(injector.getInstance(TranslateResource.class));
        env.jersey().register(injector.getInstance(ValidationResource.class));
        env.jersey().register(injector.getInstance(OntologySearchResource.class));
        env.jersey().register(injector.getInstance(DataUseResource.class));
        env.jersey().register(injector.getInstance(SwaggerResource.class));

        ElasticSearchConfiguration esConfig = config.getElasticSearchConfiguration();
        env.healthChecks().register("elastic-search", new ElasticSearchHealthCheck(esConfig));
        env.jersey().register(new StatusResource(env.healthChecks()));

        FilterRegistration.Dynamic corsFilter = env.servlets().addFilter("CORS", CrossOriginFilter.class);
        corsFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, env.getApplicationContext().getContextPath() + "/autocomplete");
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,OPTIONS");
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        corsFilter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,Pragma,Cache-Control,X-App-ID");

    }

    @Override
    public void initialize(Bootstrap<OntologyConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/api-docs", "index.html"));
    }

}
