package org.broadinstitute.dsde.consent.ontology.resources;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SwaggerResourceTest {

    private SwaggerResource swaggerResource;

    @Before
    public void setUp() {
        swaggerResource = new SwaggerResource();
    }

    @Test
    public void testIndex() {
        Response response = swaggerResource.content("index.html");
        checkStatusAndHeader(response, MediaType.TEXT_HTML);
        String content = response.getEntity().toString().trim();
        Assert.assertTrue(content.startsWith("<!DOCTYPE html>"));
        Assert.assertTrue(content.endsWith("</html>"));
    }

    @Test
    public void testStyle() {
        Response response = swaggerResource.content("css/style.css");
        checkStatusAndHeader(response, "text/css");
        String content = response.getEntity().toString().trim();
        Assert.assertTrue(content.startsWith(".swagger-section"));
    }

    @Test
    public void testJavascript() {
        Response response = swaggerResource.content("lib/marked.js");
        checkStatusAndHeader(response, "application/js");
        String content = response.getEntity().toString().trim();
        Assert.assertTrue(content.startsWith("(function()"));
    }

    @Test
    public void testPng() {
        Response response = swaggerResource.content("images/explorer_icons.png");
        checkStatusAndHeader(response, "image/png");
    }

    @Test
    public void testGif() {
        Response response = swaggerResource.content("images/expand.gif");
        checkStatusAndHeader(response, "image/gif");
    }

    @Test
    public void testNotFound() {
        Response response = swaggerResource.content("foo/bar.txt");
        Assert.assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
    }

    private void checkStatusAndHeader(Response response, String header) {
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        Object headerObject = response.getHeaders().get("Content-type");
        Assert.assertTrue(headerObject.toString().contains(header));
    }
}
