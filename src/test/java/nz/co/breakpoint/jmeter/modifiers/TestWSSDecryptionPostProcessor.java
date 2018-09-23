package nz.co.breakpoint.jmeter.modifiers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.Before;
import org.junit.Test;

public class TestWSSDecryptionPostProcessor extends TestWSSSecurityPreProcessorBase {
    private WSSDecryptionPostProcessor mod = null;
    private SampleResult result = null;

    @Before
    public void setUp() throws Exception {
        mod = new WSSDecryptionPostProcessor();
        JMeterContext context = JMeterContextService.getContext();
        mod.setThreadContext(context);
        mod.setKeystoreFile("src/test/resources/keystore.jks");
        mod.setKeystorePassword("changeit");
        mod.setCertPassword("changeit");
        result = new SampleResult();
        result.setSuccessful(true);
        context.setPreviousResult(result);
    }

    @Test
    public void testDecryption() throws Exception {
        result.setResponseData(Files.readAllBytes(Paths.get("src/test/resources/encrypted-body.xml")));
        mod.process();
        String decrypted = result.getResponseDataAsString();
        assertThat(decrypted, containsString("<add xmlns=\"http://ws.apache.org/counter/counter_port_type\">"));
    }

    @Test
    public void testNoDecryptionIfActorMismatch() throws Exception {
        result.setResponseData(Files.readAllBytes(Paths.get("src/test/resources/encrypted-body.xml")));
        mod.setActor("actor");
        mod.process();
        String decrypted = result.getResponseDataAsString();
        assertThat(decrypted, not(containsString("<add xmlns=\"http://ws.apache.org/counter/counter_port_type\">")));
    }

    @Test
    public void testFailureOnWSSException() throws Exception {
        result.setResponseData("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" >"
            +   "<SOAP-ENV:Header>"
            +       "<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" >"
            +           "<ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" >"
            +               "<ds:KeyInfo />"
            +           "</ds:Signature>"
            +       "</wsse:Security>"
            +   "</SOAP-ENV:Header>"
            +   "<SOAP-ENV:Body />"
            +"</SOAP-ENV:Envelope>",
            "UTF-8");

        JMeterUtils.setProperty(AbstractWSSecurityPostProcessor.FAIL_ON_WSS_EXCEPTION, "false");
        mod.process();
        assertTrue(result.isSuccessful());
        assertEquals(0, result.getAssertionResults().length);

        JMeterUtils.setProperty(AbstractWSSecurityPostProcessor.FAIL_ON_WSS_EXCEPTION, "true");
        mod.process();
        assertFalse(result.isSuccessful());
        AssertionResult[] assertionResults = result.getAssertionResults();
        assertEquals(1, assertionResults.length);
        assertEquals("WSSecurityException", assertionResults[0].getName());
        assertTrue(assertionResults[0].isError());
        assertThat(assertionResults[0].getFailureMessage(), containsString("Any SIG_KEY_INFO MUST contain exactly one child element"));
    }

    @Test
    public void testNoFailureOnOtherException() throws Exception {
        JMeterUtils.setProperty(AbstractWSSecurityPostProcessor.FAIL_ON_WSS_EXCEPTION, "true");
        result.setResponseData("<dummy />", "UTF-8");

        mod.process();

        assertTrue(result.isSuccessful());
        AssertionResult[] assertionResults = result.getAssertionResults();
        assertEquals(0, assertionResults.length);
    }

    @Test
    public void testSignatureValidation() throws Exception {
        result.setResponseData(Files.readAllBytes(Paths.get("src/test/resources/signed-body.xml")));

        mod.process();

        assertTrue(result.isSuccessful());
        assertEquals(0, result.getAssertionResults().length);
    }

    @Test
    public void testFindAttachment() throws Exception {
        SampleResult sub = new SampleResult();
        sub.setSampleLabel("foobar");
        sub.setResponseHeaders("Content-Type: foobar\n"+
            "Content-ID: attachme");
        result.addRawSubResult(sub);

        assertEquals(sub, mod.findAttachment("attachme", result));
        assertEquals(null, mod.findAttachment("foobar", result));

        JMeterUtils.setProperty(AbstractWSSecurityPostProcessor.SAMPLE_LABEL_REGEX, "(.*)");
        assertEquals(null, mod.findAttachment("attachme", result));
        assertEquals(sub, mod.findAttachment("foobar", result));
    }

    @Test
    public void testSwADecryption() throws Exception {
        result.setResponseData(Files.readAllBytes(Paths.get("src/test/resources/encrypted-attachment.xml")));
        mod.setAttachments(Arrays.asList(new Attachment("foobar", "", "f4Sw//ZXCbuPnThtylTb0QIJr05w9k4mBo5F0DknerE=", "", "", "")));

        mod.process();

        assertTrue(result.isSuccessful());
        assertEquals(1, result.getSubResults().length);
        SampleResult a = result.getSubResults()[0];
        assertEquals("Attachment cid:foobar", a.getSampleLabel());
        assertEquals("attachme", a.getResponseDataAsString());
        assertEquals("", a.getResponseHeaders());
    }

    @Test
    public void testSwADecryptionWithHeaders() throws Exception {
        result.setResponseData(Files.readAllBytes(Paths.get("src/test/resources/encrypted-attachment-header.xml")));
        mod.setAttachments(Arrays.asList(new Attachment("foobar", "", "sim44AGzLygo+lC42uG3jSoC+QRuI1bVf/cx06uFE5ZECbk7x9iefOpplHdKk4M5mlcz8CksXft5JHEIId/e/g==", "", "", "")));

        mod.process();

        assertTrue(result.isSuccessful());
        assertEquals(1, result.getSubResults().length);
        SampleResult a = result.getSubResults()[0];
        assertEquals("Attachment cid:foobar", a.getSampleLabel());
        assertEquals("attachme", a.getResponseDataAsString());
        assertEquals("Content-Type:text/plain\n", a.getResponseHeaders());
    }

    @Test
    public void testXopDecryption() throws Exception {
        result.setResponseData(Files.readAllBytes(Paths.get("src/test/resources/encrypted-attachment-xop.xml")));

        mod.process();

        String decrypted = result.getResponseDataAsString();
        assertThat(decrypted, containsString("YXR0YWNobWU="));
        assertTrue(result.isSuccessful());
        assertEquals(0, result.getSubResults().length);
    }
}
