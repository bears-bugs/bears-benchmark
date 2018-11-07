/**
 * 
 */
package org.jsapar.parse.bean;

import org.jsapar.TstPerson;
import org.jsapar.model.*;
import org.jsapar.parse.DocumentBuilderLineEventListener;
import org.jsapar.schema.Schema;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author stejon0
 * 
 */
public class BeanParseTaskTest {
    static final Date birthTime = new Date();

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    private Schema makeOutputSchema(){
        return BeanMarshallerTest.makeOutputSchema();
    }

    private BeanMap makeBeanMap() throws IntrospectionException, ClassNotFoundException {
        return BeanMarshallerTest.makeBeanMap();
    }

    @Test
    public void testBuild() throws IOException, IntrospectionException, ClassNotFoundException {
        List<TstPerson> people = new ArrayList<>(2);
        TstPerson person = new TstPerson();
        person.setFirstName("Jonas");
        people.add(person);

        person = new TstPerson();
        person.setFirstName("Test2");
        people.add(person);

        BeanParseTask<TstPerson> parser = new BeanParseTask<>(people.stream(), makeBeanMap());
        DocumentBuilderLineEventListener listener = new DocumentBuilderLineEventListener();
        parser.setLineEventListener(listener);
        parser.execute();
        Document doc = listener.getDocument();

        assertEquals(2, doc.size());
        Line line = doc.getLine(0);
        assertEquals("Jonas",
                line.getCell("firstName").orElseThrow(() -> new AssertionError("Should be set")).getStringValue());

        line = doc.getLine(1);
        assertEquals("Test2", line.getCell("firstName").orElseThrow(() -> new AssertionError("Should be set")).getStringValue());

    }




    
}
