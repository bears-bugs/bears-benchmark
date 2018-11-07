package org.jsapar.error;

import org.junit.Test;

import static org.junit.Assert.*;

public class RecordingErrorEventListenerTest {

    @Test
    public void testErrorEvent() throws Exception {
        RecordingErrorEventListener instance = new RecordingErrorEventListener();
        assertTrue(instance.getErrors().isEmpty());
        JSaParException anError = new JSaParException("testing");
        instance.errorEvent(new ErrorEvent(this, anError));
        assertEquals(1, instance.getErrors().size());
        assertSame(anError, instance.getErrors().get(0));
        instance.clear();
        assertTrue(instance.getErrors().isEmpty());
    }


}