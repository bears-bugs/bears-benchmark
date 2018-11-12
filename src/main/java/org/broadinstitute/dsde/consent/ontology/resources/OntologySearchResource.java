package org.broadinstitute.dsde.consent.ontology.resources;

import com.google.inject.Inject;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteAPI;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/search")
public class OntologySearchResource {

    private AutocompleteAPI api;

    public OntologySearchResource() {}

    @Inject
    public OntologySearchResource(AutocompleteAPI api) {
        this.api = api;
    }

    @GET
    @Produces("application/json")
    public Response getOntologyById(@QueryParam("id") @DefaultValue("") String queryTerm) throws IOException {
        if (!queryTerm.isEmpty()) {
            List<TermResource> result = api.lookupById(queryTerm);
            if(!result.isEmpty()){
                return Response.ok().entity(result).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse(" Supplied ID doesn't match any known ontology. ", Response.Status.NOT_FOUND.getStatusCode()))
                        .build();
            }
        } else {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(" Ontology ID term cannot be empty. ", Response.Status.BAD_REQUEST.getStatusCode()))
                    .build();
        }
    }
}
