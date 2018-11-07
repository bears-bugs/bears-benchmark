package org.jsapar.error;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ThresholdRecordingErrorEventListenerTest {

    @Test
    public void testErrorEvent() throws Exception {
        List<JSaParException> errors = new ArrayList<>();
        ThresholdRecordingErrorEventListener listener = new ThresholdRecordingErrorEventListener(1, errors);
        assertEquals(0, errors.size());
        listener.errorEvent(new ErrorEvent(this, new JSaParException("testing")));
        assertEquals(1, errors.size());
    }

    @Test(expected = MaxErrorsExceededException.class)
    public void testErrorEvent_exceeded() throws Exception {
        ThresholdRecordingErrorEventListener listener = new ThresholdRecordingErrorEventListener(1);
        listener.errorEvent(new ErrorEvent(this, new JSaParException("testing 1")));
        listener.errorEvent(new ErrorEvent(this, new JSaParException("testing 2")));
    }

}