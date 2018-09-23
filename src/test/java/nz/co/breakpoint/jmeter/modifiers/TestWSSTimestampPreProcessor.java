package nz.co.breakpoint.jmeter.modifiers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.threads.JMeterContextService;
import org.junit.Before;
import org.junit.Test;

public class TestWSSTimestampPreProcessor extends TestWSSSecurityPreProcessorBase {
    private WSSTimestampPreProcessor mod = null;
    private final static int TIME_TO_LIVE = 300;
    
    @Before
    public void setUp() throws Exception {
        mod = new WSSTimestampPreProcessor();
        mod.setThreadContext(JMeterContextService.getContext());
        mod.setTimeToLive(TIME_TO_LIVE);
    }

    @Test
    public void testTimestamp() throws Exception {
        HTTPSamplerBase sampler = createHTTPSampler();
        mod.process();
        String content = SamplerPayloadAccessor.getPayload(sampler);
        assertThat(content, containsString(":Timestamp"));
    }
}
