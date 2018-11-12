package org.broadinstitute.dsde.consent.ontology.resources;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SwaggerResourceTest {

    SwaggerResource swaggerResource;

    @Before
    public void setUp() throws Exception {
        swaggerResource = new SwaggerResource();
    }

    @Test
    public void testIndex() {
        Response response = swaggerResource.content("index.html");
        checkStatusAndHeader(response, Response.Status.OK, MediaType.TEXT_HTML);
        String content = response.getEntity().toString().trim();
        Assert.assertTrue(content.startsWith("<!DOCTYPE html>"));
        Assert.assertTrue(content.endsWith("</html>"));
    }

    @Test
    public void testStyle() {
        Response response = swaggerResource.content("css/style.css");
        checkStatusAndHeader(response, Response.Status.OK, "text/css");
        String content = response.getEntity().toString().trim();
        Assert.assertTrue(content.startsWith(".swagger-section"));
    }

    @Test
    public void testJavascript() {
        Response response = swaggerResource.content("lib/marked.js");
        checkStatusAndHeader(response, Response.Status.OK, "application/js");
        String content = response.getEntity().toString().trim();
        Assert.assertTrue(content.startsWith("(function()"));
    }


    private void checkStatusAndHeader(Response response, Response.Status status, String header) {
        Assert.assertTrue(response.getStatus() == status.getStatusCode());
        Object headerObject = response.getHeaders().get("Content-type");
        Assert.assertTrue(headerObject.toString().contains(header));
    }
}
