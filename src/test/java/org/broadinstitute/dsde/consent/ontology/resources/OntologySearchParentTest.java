package org.broadinstitute.dsde.consent.ontology.resources;

import org.broadinstitute.dsde.consent.ontology.resources.model.TermParent;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteAPI;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OntologySearchParentTest {

    @Mock
    AutocompleteAPI api;

    OntologySearchResource resource;

    // Construction of children and parents.
    static TermResource child = new TermResource();
    static TermResource parent1 = new TermResource();
    static TermResource parent2 = new TermResource();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        resource = new OntologySearchResource(api);

        parent1.setId("parent1");
        parent1.setLabel("parent1 label");
        parent1.setDefinition("parent1 definition");
        parent1.setSynonyms(new ArrayList<>(Arrays.asList("parent 1", "parent 01")));

        parent2.setId("parent2");
        parent2.setLabel("parent2 label");
        parent2.setDefinition("parent2 definition");
        parent2.setSynonyms(new ArrayList<>(Arrays.asList("parent 2", "parent 02")));

        // These are the parents for the child - each one refers to a full parent node.
        TermParent childParent1 = new TermParent();
        TermParent childParent2 = new TermParent();
        childParent1.setId(parent1.getId());
        childParent1.setOrder(1);
        childParent2.setId(parent2.getId());
        childParent2.setOrder(2);

        child.setId("child");
        child.setLabel("child label");
        child.setDefinition("child definition");
        child.setSynonyms(new ArrayList<>(Arrays.asList("child 1", "child 01")));
        child.setParents(new ArrayList<>());
        child.getParents().add(childParent1);
        child.getParents().add(childParent2);

        Mockito.when(api.lookupById(child.getId())).thenReturn(Collections.singletonList(child));
        Mockito.when(api.lookupById(parent1.getId())).thenReturn(Collections.singletonList(parent1));

    }

    @Test
    public void testGetNodeWithNoParents() throws Exception {
        Response response = resource.getOntologyById(parent1.getId());
        assertOKstatusAndTermSize(response);
    }

    @Test
    public void testGetChildWithParents() throws Exception {
        Response response = resource.getOntologyById(child.getId());
        List<TermResource> terms = assertOKstatusAndTermSize(response);

        TermResource term = terms.get(0);
        assertTrue(child.getId().equals(term.getId()));
        assertTrue(child.getLabel().equals(term.getLabel()));
        assertTrue(child.getDefinition().equals(term.getDefinition()));
        assertTrue(child.getSynonyms().equals(term.getSynonyms()));

        assertEquals("Expected two parents", 2, term.getParents().size());
        term.getParents().sort(Comparator.comparingInt(TermParent::getOrder));

        TermParent actualParent1 = term.getParents().get(0);
        TermParent actualParent2 = term.getParents().get(1);

        assertTrue(actualParent1.getId().equals(parent1.getId()));
        assertTrue(actualParent2.getId().equals(parent2.getId()));
    }

    @SuppressWarnings("unchecked")
    private List<TermResource> assertOKstatusAndTermSize(Response response) {
        assertTrue(response.getStatus() == 200);
        List<TermResource> terms = (List<TermResource>) response.getEntity();
        assertTrue(terms.size() == 1);
        return terms;
    }

}
