package org.jsapar.compose.bean;

import org.jsapar.TstPerson;
import org.jsapar.model.Line;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class RecordingBeanEventListenerTest {

    @Test
    public void getBeans() {
        RecordingBeanEventListener<TstPerson> recordingBeanEventListener = new RecordingBeanEventListener<>();
        TstPerson tstPerson = new TstPerson();
        assertEquals(0, recordingBeanEventListener.getBeans().size());
        recordingBeanEventListener.beanComposedEvent(new BeanEvent<>(this, tstPerson, new Line("Test")));
        assertEquals(1, recordingBeanEventListener.getBeans().size());
        assertSame(tstPerson, recordingBeanEventListener.getBeans().get(0));
    }


    @Test
    public void clearAndSize() {
        RecordingBeanEventListener<TstPerson> recordingBeanEventListener = new RecordingBeanEventListener<>();
        TstPerson tstPerson = new TstPerson();
        assertEquals(0, recordingBeanEventListener.size());
        recordingBeanEventListener.beanComposedEvent(new BeanEvent<>(this, tstPerson, new Line("Test")));
        assertEquals(1, recordingBeanEventListener.size());
        recordingBeanEventListener.clear();
        assertEquals(0, recordingBeanEventListener.size());
    }

    @Test
    public void iterator() {
        RecordingBeanEventListener<TstPerson> recordingBeanEventListener = new RecordingBeanEventListener<>();
        TstPerson tstPerson = new TstPerson();
        recordingBeanEventListener.beanComposedEvent(new BeanEvent<>(this, tstPerson, new Line("Test")));
        Iterator<TstPerson> iterator = recordingBeanEventListener.iterator();
        assertSame(tstPerson, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void iterable() {
        RecordingBeanEventListener<TstPerson> recordingBeanEventListener = new RecordingBeanEventListener<>();
        TstPerson tstPerson = new TstPerson();
        recordingBeanEventListener.beanComposedEvent(new BeanEvent<>(this, tstPerson, new Line("Test")));
        boolean found = false;
        for (TstPerson person : recordingBeanEventListener) {
            assertSame(tstPerson, person);
            found = true;
        }
        assertTrue(found);
    }

}