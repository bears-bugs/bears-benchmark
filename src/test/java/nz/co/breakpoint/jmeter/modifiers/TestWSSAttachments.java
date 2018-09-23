package nz.co.breakpoint.jmeter.modifiers;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.property.ObjectProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

public class TestWSSAttachments extends TestWSSSecurityPreProcessorBase {
    protected static final String XOP_SOAP_MESSAGE = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
        +   "<soap:Body>"
        +       "<xop:Include xmlns:xop=\"http://www.w3.org/2004/08/xop/include\" href=\"cid:foobar\" />"
        +   "</soap:Body>"
        + "</soap:Envelope>";

    static WSSSignaturePreProcessor setupSignaturePreprocessor() throws Exception {
        WSSSignaturePreProcessor mod = new WSSSignaturePreProcessor();
        mod.setThreadContext(JMeterContextService.getContext());
        initCertSettings(mod);
        mod.setKeyIdentifier(WSSSignaturePreProcessor.keyIdentifiers[0]);
        mod.setSignatureCanonicalization(WSSSignaturePreProcessor.signatureCanonicalizations[0]);
        mod.setSignatureAlgorithm(WSSSignaturePreProcessor.signatureAlgorithms[0]);
        mod.setDigestAlgorithm(WSSSignaturePreProcessor.digestAlgorithms[0]);
        return mod;
    }

    static WSSEncryptionPreProcessor setupEncryptionPreProcessor() throws Exception {
        WSSEncryptionPreProcessor mod = new WSSEncryptionPreProcessor();
        initCertSettings(mod);
        mod.setThreadContext(JMeterContextService.getContext());
        mod.setKeyIdentifier(WSSEncryptionPreProcessor.keyIdentifiers[0]);
        mod.setKeyEncryptionAlgorithm(WSSEncryptionPreProcessor.keyEncryptionAlgorithms[0]);
        mod.setSymmetricEncryptionAlgorithm(WSSEncryptionPreProcessor.symmetricEncryptionAlgorithms[0]);
        return mod;
    }

    static List<SecurityPart> initParts(boolean signHeaders) {
        List<SecurityPart> partsToSecure = new ArrayList<SecurityPart>();
        SecurityPart part = new SecurityPart();
        part.setId("cid:Attachments");
        if (signHeaders) part.setModifier("Element");
        partsToSecure.add(part);
        return partsToSecure;
    }
    
    static List<Attachment> initAttachment(String cid, String content) throws UnsupportedEncodingException {
        return Arrays.asList(new Attachment(cid, "text/plain", Base64.getEncoder().encodeToString(content.getBytes("UTF-8")), "Content-Type:text/plain\n", "", ""));
    }

    @Test
    public void testSwASignature() throws Exception {
        Sampler sampler = createHTTPSampler();
        WSSSignaturePreProcessor mod = setupSignaturePreprocessor();
        mod.setPartsToSecure(initParts(false)); // without headers
        mod.setAttachments(initAttachment("foobar", "attachme"));
        
        mod.process();

        String soapenv = mod.getSamplerPayload();
        assertThat(soapenv, containsString("<ds:Reference URI=\"cid:foobar\">"));
        assertThat(soapenv, containsString("<ds:Transform Algorithm=\"http://docs.oasis-open.org/wss/oasis-wss-SwAProfile-1.1#Attachment-Content-Signature-Transform\"/>"));
        org.apache.wss4j.common.ext.Attachment a = mod.getAttachmentCallbackHandler().getAttachment("foobar");
        assertEquals(1, a.getHeaders().size());
        assertEquals("text/plain", a.getHeaders().get("Content-Type"));
    }

    @Test
    public void testSwASignatureWithHeaders() throws Exception {
        Sampler sampler = createHTTPSampler();
        WSSSignaturePreProcessor mod = setupSignaturePreprocessor();
        mod.setPartsToSecure(initParts(true)); // with headers
        mod.setAttachments(initAttachment("foobar", "attachme"));

        mod.process();

        String soapenv = mod.getSamplerPayload();
        assertThat(soapenv, containsString("<ds:Reference URI=\"cid:foobar\">"));
        assertThat(soapenv, containsString("<ds:Transform Algorithm=\"http://docs.oasis-open.org/wss/oasis-wss-SwAProfile-1.1#Attachment-Complete-Signature-Transform\"/>"));
        org.apache.wss4j.common.ext.Attachment a = mod.getAttachmentCallbackHandler().getAttachment("foobar");
        assertEquals(1, a.getHeaders().size());
        assertEquals("text/plain", a.getHeaders().get("Content-Type"));
    }

    @Test
    public void testSwAEncryption() throws Exception {
        Sampler sampler = createHTTPSampler();
        WSSEncryptionPreProcessor mod = setupEncryptionPreProcessor();
        mod.setPartsToSecure(initParts(false)); // without headers
        mod.setAttachments(initAttachment("foobar", "attachme"));

        mod.process();

        String soapenv = mod.getSamplerPayload();
        assertThat(soapenv, containsString("<xenc:CipherReference URI=\"cid:foobar\">"));
        assertThat(soapenv, containsString("Algorithm=\"http://docs.oasis-open.org/wss/oasis-wss-SwAProfile-1.1#Attachment-Ciphertext-Transform\"/>"));
        assertThat(soapenv, containsString("Type=\"http://docs.oasis-open.org/wss/oasis-wss-SwAProfile-1.1#Attachment-Content-Only\""));
        org.apache.wss4j.common.ext.Attachment a = mod.getAttachmentCallbackHandler().getAttachment("foobar");
        assertThat(IOUtils.toString(a.getSourceStream(), "UTF-8"), not(containsString("attachme")));
        assertEquals(1, a.getHeaders().size());
        assertEquals("text/plain", a.getHeaders().get("Content-Type"));
    }

    @Test
    public void testSwAEncryptionWithHeaders() throws Exception {
        Sampler sampler = createHTTPSampler();
        WSSEncryptionPreProcessor mod = setupEncryptionPreProcessor();
        mod.setPartsToSecure(initParts(true)); // with headers
        mod.setAttachments(initAttachment("foobar", "attachme"));

        mod.process();

        String soapenv = mod.getSamplerPayload();
        assertThat(soapenv, containsString("<xenc:CipherReference URI=\"cid:foobar\">"));
        assertThat(soapenv, containsString("Algorithm=\"http://docs.oasis-open.org/wss/oasis-wss-SwAProfile-1.1#Attachment-Ciphertext-Transform\"/>"));
        assertThat(soapenv, containsString("Type=\"http://docs.oasis-open.org/wss/oasis-wss-SwAProfile-1.1#Attachment-Complete\""));
        org.apache.wss4j.common.ext.Attachment a = mod.getAttachmentCallbackHandler().getAttachment("foobar");
        assertThat(IOUtils.toString(a.getSourceStream(), "UTF-8"), not(containsString("attachme")));
        assertEquals(0, a.getHeaders().size()); // Content-Type header is now part of the attachment's encrypted source stream
    }

    @Test
    public void testSwAEncryptionOutputVariable() throws Exception {
        Sampler sampler = createHTTPSampler();
        WSSEncryptionPreProcessor mod = setupEncryptionPreProcessor();
        mod.setPartsToSecure(initParts(false)); // without headers
        mod.setAttachments(initAttachment("foobar", "attachme"));
        mod.getAttachments().get(0).setMode("Variable");
        mod.getAttachments().get(0).setDestination("output");

        mod.process();

        String soapenv = mod.getSamplerPayload();
        assertThat(soapenv, containsString("<xenc:CipherReference URI=\"cid:foobar\">"));
        assertThat(soapenv, containsString("Algorithm=\"http://docs.oasis-open.org/wss/oasis-wss-SwAProfile-1.1#Attachment-Ciphertext-Transform\"/>"));
        assertThat(soapenv, containsString("Type=\"http://docs.oasis-open.org/wss/oasis-wss-SwAProfile-1.1#Attachment-Content-Only\""));
        JMeterContext context = JMeterContextService.getContext();
        assertTrue(context.getVariables().getObject("output") instanceof byte[]);
        assertTrue(((byte[])context.getVariables().getObject("output")).length > 0);
        assertThat(IOUtils.toString((byte[])context.getVariables().getObject("output"), "UTF-8"), not(containsString("attachme")));
    }

    @Test
    public void testXopEncryption() throws Exception {
        Sampler sampler = createHTTPSampler();
        WSSEncryptionPreProcessor mod = setupEncryptionPreProcessor();
        mod.setPartsToSecure(new ArrayList<SecurityPart>());
        mod.setAttachments(initAttachment("foobar", "attachme"));
        mod.setSamplerPayload(XOP_SOAP_MESSAGE);

        mod.process();

        String soapenv = mod.getSamplerPayload();
        assertThat(soapenv, containsString("<xenc:EncryptedData"));
        // no attachment reference expected, as xop element should be inlined:
        assertThat(soapenv, not(containsString("\"cid:foobar\"")));
        assertThat(soapenv, not(containsString("http://docs.oasis-open.org/wss/oasis-wss-SwAProfile-1.1")));
        assertEquals(1, mod.getAttachmentCallbackHandler().getAttachments().size());
        org.apache.wss4j.common.ext.Attachment a = mod.getAttachmentCallbackHandler().getAttachment("foobar");
        assertThat(IOUtils.toString(a.getSourceStream(), "UTF-8"), not(containsString("attachme")));
    }
}
