package org.broadinstitute.dsde.consent.ontology.resources;

import org.broadinstitute.dsde.consent.ontology.datause.models.Everything;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

public class DataUseResourceTest {

    DataUseResource dataUseResource;
    String generalUse = "{ \"generalUse\": true }";

    @Before
    public void setUp() throws Exception {
        dataUseResource = new DataUseResource();
    }

    @Test
    public void testIndex() {
        Response response = dataUseResource.getSchema();
        assertStatusAndHeader(response, Response.Status.OK, MediaType.APPLICATION_JSON);
        String content = response.getEntity().toString().trim();
        Assert.assertTrue(content.contains("Data Use"));
        try {
            new JSONObject(content);
        } catch (JSONException e) {
            fail("The response entity is not valid json");
        }
    }

    @Test
    public void testConsent() {
        Response response = dataUseResource.translateConsent(generalUse);
        UseRestriction restriction = (UseRestriction) response.getEntity();
        assertNotNull(restriction);
        assertTrue(restriction.equals(new Everything()));
        assertStatusAndHeader(response, Response.Status.OK, MediaType.APPLICATION_JSON);
    }

    @Test
    public void testDAR() {
        Response response = dataUseResource.translateDAR(generalUse);
        UseRestriction restriction = (UseRestriction) response.getEntity();
        assertNotNull(restriction);
        assertTrue(restriction.equals(new Everything()));
        assertStatusAndHeader(response, Response.Status.OK, MediaType.APPLICATION_JSON);
    }

    private void assertStatusAndHeader(Response response, Response.Status status, String contentType) {
        Assert.assertTrue(response.getStatus() == status.getStatusCode());
        Object header = response.getHeaders().get("Content-type");
        Assert.assertTrue(header.toString().contains(contentType));
    }

}
