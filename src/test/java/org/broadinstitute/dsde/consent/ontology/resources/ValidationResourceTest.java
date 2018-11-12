package org.broadinstitute.dsde.consent.ontology.resources;

import com.google.api.client.http.HttpStatusCodes;
import org.broadinstitute.dsde.consent.ontology.resources.validate.ValidationResource;
import org.broadinstitute.dsde.consent.ontology.service.validate.UseRestrictionValidateAPI;
import org.broadinstitute.dsde.consent.ontology.service.validate.ValidateResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValidationResourceTest {

    private ValidationResource validationResource;

    @Mock
    private UseRestrictionValidateAPI validateAPI;

    private String useRestriction = "{ + \"type\": \"and\","
                                   + "\"operands\": [{ \"type\": \"named\","
                                   + "\"name\": \"http://purl.obolibrary.org/obo/DOID_162\""
                                   + "}]}";
    private String invalidUseRestriction = "{\"test\": \"and\","
                                    + "\"operands\": [{ \"type\": \"named\","
                                    + "\"name\": \"http://purl.obolibrary.org/obo/DOID_162\""
                                    + "}]}";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        validationResource = new ValidationResource();
        validationResource.setValidateAPI(validateAPI);
    }

    @Test
    public void testValidateUseRestriction() throws Exception {
        ValidateResponse validateResponse = new ValidateResponse(true, useRestriction);
        when(validateAPI.validateUseRestriction(useRestriction)).thenReturn(validateResponse);
        Response response = validationResource.validateUseRestriction(useRestriction);
        ValidateResponse validateResponseResult = (ValidateResponse) response.getEntity();
        assertTrue(validateResponseResult.getErrors().isEmpty());
        assertTrue(validateResponseResult.isValid());
        assertTrue(response.getStatus() == HttpStatusCodes.STATUS_CODE_OK);
        assertTrue(validateResponseResult.getUseRestriction().endsWith(useRestriction));
    }

    @Test
    public void testValidateUseRestrictionInvalid() throws Exception {
        String error = "Term not found: test";
        ValidateResponse validateResponse = new ValidateResponse(false, invalidUseRestriction);
        validateResponse.addError(error);
        when(validateAPI.validateUseRestriction(useRestriction)).thenReturn(validateResponse);
        Response response = validationResource.validateUseRestriction(useRestriction);
        ValidateResponse validateResponseResult = (ValidateResponse) response.getEntity();
        assertFalse(validateResponseResult.isValid());
        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
        assertTrue(((ArrayList<String>) validateResponseResult.getErrors()).get(0).contains(error));
    }

    @Test
    public void testValidateUseRestrictionAfterException() throws Exception {
        doThrow(new Exception()).when(validateAPI).validateUseRestriction(useRestriction);
        Response response = validationResource.validateUseRestriction(useRestriction);
        assertTrue(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode());
    }

}
