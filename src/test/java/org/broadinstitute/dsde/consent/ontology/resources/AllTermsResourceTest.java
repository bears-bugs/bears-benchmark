package org.broadinstitute.dsde.consent.ontology.resources;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteAPI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class AllTermsResourceTest {

    @Mock
    AutocompleteAPI apiMockUp;

    AllTermsResource allTermsResource;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        allTermsResource = new AllTermsResource(apiMockUp);
    }

    @Test
    public void testGetTermWithOntologyNamesNull(){
        int limit = 1;
        List<TermResource> termResources = new ArrayList<>();

        Mockito.when(apiMockUp.lookup(null,limit)).thenReturn(termResources);
        allTermsResource.getTerms(null, null, limit);
        verify(apiMockUp, times(1)).lookup(Mockito.anyString(), Mockito.anyInt());
    }

    @Test
    public void testGetTermWithOntologyNames(){
        int limit = 20;
        List<TermResource> termResources = new ArrayList<>();
        String ontologyName = "Kidney Cancer";
        Mockito.when(apiMockUp.lookup(ontologyName,limit)).thenReturn(termResources);
        allTermsResource.getTerms(null, null, limit);
        verify(apiMockUp,times(1)).lookup(Mockito.anyString(), Mockito.anyInt());
    }

}
