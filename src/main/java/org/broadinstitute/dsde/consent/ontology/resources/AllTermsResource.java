package org.broadinstitute.dsde.consent.ontology.resources;

import com.google.inject.Inject;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteAPI;

import javax.ws.rs.*;
import java.util.Arrays;
import java.util.List;

@Path("/autocomplete")
public class AllTermsResource {
    private AutocompleteAPI api;

    public AllTermsResource() {}

    @Inject
    public AllTermsResource(AutocompleteAPI api) {
        this.api = api;
    }

    @GET
    @Produces("application/json")
    public List<TermResource> getTerms(
            @QueryParam("q") String queryTerm,
            @QueryParam("types") @DefaultValue("") String ontologyNames,
            @QueryParam("count") @DefaultValue("20") int limit) {
        if (ontologyNames == null || ontologyNames.isEmpty()) {
            return api.lookup(queryTerm, limit);
        } else {
            return api.lookup(Arrays.asList(ontologyNames.split(",")), queryTerm, limit);
        }
    }

}
