package nz.co.breakpoint.jmeter.modifiers;

import javax.xml.parsers.ParserConfigurationException;
import org.apache.jmeter.protocol.jms.sampler.JMSSampler;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.dom.message.WSSecHeader;
import org.apache.wss4j.dom.message.WSSecBase;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

public class TestAbstractWSSecurityPreProcessor extends TestWSSSecurityPreProcessorBase {
    private DummyWSSecurityPreProcessor instance = null;
    private JMSSampler sampler = null;

    class DummyWSSecurityPreProcessor extends AbstractWSSecurityPreProcessor {
        public DummyWSSecurityPreProcessor() throws ParserConfigurationException {
            super();
        }

        @Override
        protected Document build(Document document, WSSecHeader secHeader) throws WSSecurityException {
            // no-op implementation for testing
            return document;
        }
    }

    @Before
    public void setUp() throws Exception {
        JMeterContext context = JMeterContextService.getContext();
        instance = new DummyWSSecurityPreProcessor();
        instance.setThreadContext(context);
        sampler = new JMSSampler();
        context.setCurrentSampler(sampler);
    }

    @Test
    public void testProcess() throws Exception {
        sampler.setContent("<x>✓</x>");
        instance.process();
        String payload = sampler.getContent();
        assertThat(payload, containsString(">✓<"));
        assertThat(payload, containsString(":Security"));
    }

    @Test
    public void testActor() throws Exception {
        sampler.setContent(SAMPLE_SOAP_MSG);
        instance.setActor("Breakpoint");
        instance.process();
        String payload = sampler.getContent();
        assertThat(payload, containsString("SOAP-ENV:actor=\"Breakpoint\""));
    }

    @Test
    public void testEmptyActor() throws Exception {
        sampler.setContent(SAMPLE_SOAP_MSG);
        instance.setActor("");
        instance.process();
        String payload = sampler.getContent();
        assertThat(payload, not(containsString("SOAP-ENV:actor=")));
    }

    @Test
    public void testMustUnderstand() throws Exception {
        sampler.setContent(SAMPLE_SOAP_MSG);
        instance.setMustUnderstand(true);
        instance.process();
        String payload = sampler.getContent();
        assertThat(payload, containsString("SOAP-ENV:mustUnderstand=\"1\""));
    }

    @Test
    public void testDefaultMustUnderstand() throws Exception {
        sampler.setContent(SAMPLE_SOAP_MSG);
        instance.process();
        String payload = sampler.getContent();
        assertThat(payload, not(containsString("SOAP-ENV:mustUnderstand")));
    }
}
