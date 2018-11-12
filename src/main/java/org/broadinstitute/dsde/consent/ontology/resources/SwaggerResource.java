package org.broadinstitute.dsde.consent.ontology.resources;

import org.parboiled.common.FileUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/swagger")
public class SwaggerResource {

    private final static String swaggerResource = "META-INF/resources/webjars/swagger-ui/2.2.8/";

    @Context
    UriInfo uriInfo;

    @GET
    @Path("{path:.*}")
    public Response content(@PathParam("path") String path) {
        Response response;
        String mediaType = getMediaTypeFromPath(path);
        if (path.isEmpty() || path.equals("index.html")) {
            response = Response.ok().entity(getIndex()).type(mediaType).build();
        } else {
            mediaType = getMediaTypeFromPath(path);
            if (path.endsWith("png") || path.endsWith("gif")) {
                byte[] content = FileUtils.readAllBytesFromResource(swaggerResource + path);
                response = Response.ok().entity(content).type(mediaType).build();
            } else {
                String content = FileUtils.readAllTextFromResource(swaggerResource + path);
                response = Response.ok().entity(content).type(mediaType).build();
            }
        }
        return response;
    }

    private String getMediaTypeFromPath(String path) {
        // Default case:
        String mediaType = MediaType.TEXT_HTML;

        // Handle specific cases for the various swagger ui file content types:
        if (path.endsWith("css")) {
            mediaType = "text/css";
        }
        if (path.endsWith("js")) {
            mediaType = "application/js";
        }
        if (path.endsWith("png")) {
            mediaType = "image/png";
        }
        if (path.endsWith("gif")) {
            mediaType = "image/gif";
        }
        return mediaType;
    }

    private String getIndex() {
        String content = FileUtils.readAllTextFromResource(swaggerResource + "index.html");
        return content
            .replace("your-client-secret-if-required", "")
            .replace("your-realms", "Broad Institute")
            .replace("your-app-name", "Consent Ontology")
            .replace("scopeSeparator: \",\"", "scopeSeparator: \" \"")
            .replace("jsonEditor: false,", "jsonEditor: false," + "validatorUrl: null, apisSorter: \"alpha\", operationsSorter: \"alpha\",")
            .replace("url = \"http://petstore.swagger.io/v2/swagger.json\";", "url = '/api-docs/api-docs.yaml';");
    }

}
