package org.broadinstitute.dsde.consent.ontology.resources.validate;


import com.google.inject.Inject;
import org.broadinstitute.dsde.consent.ontology.resources.ErrorResponse;
import org.broadinstitute.dsde.consent.ontology.service.validate.UseRestrictionValidateAPI;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/validate")
@Consumes("application/json")
@Produces("application/json")
public class ValidationResource {

    private UseRestrictionValidateAPI validateAPI;

    @Inject
    public void setValidateAPI(UseRestrictionValidateAPI validateAPI) {
        this.validateAPI = validateAPI;
    }

    @POST
    @Path("/userestriction")
    public Response validateUseRestriction(String useRestriction){
        try{
            return Response.ok().entity(validateAPI.validateUseRestriction(useRestriction)).build();
        } catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode())).build();
        }
    }
}
