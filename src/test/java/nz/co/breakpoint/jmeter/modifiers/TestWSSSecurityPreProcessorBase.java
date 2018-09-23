package nz.co.breakpoint.jmeter.modifiers;

import org.apache.jmeter.protocol.http.sampler.HTTPSamplerFactory;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.threads.JMeterContextService;
import org.junit.ClassRule;

public class TestWSSSecurityPreProcessorBase {
    @ClassRule
    public static final JMeterPropertiesResource props = new JMeterPropertiesResource();
    @ClassRule
    public static final JMeterContextResource ctx = new JMeterContextResource();

    protected static final String SAMPLE_SOAP_MSG = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
        + "<SOAP-ENV:Envelope "
        +   "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" "
        +   "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
        +   "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" 
        +   "<SOAP-ENV:Body>" 
        +       "<add xmlns=\"http://ws.apache.org/counter/counter_port_type\">" 
        +           "<value xmlns=\"\">15</value>" 
        +       "</add>" 
        +   "</SOAP-ENV:Body>" 
        + "</SOAP-ENV:Envelope>";    

    static HTTPSamplerBase createHTTPSampler() {
        HTTPSamplerBase sampler = HTTPSamplerFactory.newInstance();
        sampler.addNonEncodedArgument("", SAMPLE_SOAP_MSG, "");
        sampler.setPostBodyRaw(true);
        JMeterContextService.getContext().setCurrentSampler(sampler);
        return sampler;
    }
    
    static void initCertSettings(CryptoWSSecurityPreProcessor mod) {
        initCertSettings(mod, WSSSignaturePreProcessor.signatureAlgorithms[0]);
    }

    static void initCertSettings(CryptoWSSecurityPreProcessor mod, String signatureAlgorithm) {
        mod.setKeystoreFile("src/test/resources/keystore.jks");
        mod.setKeystorePassword("changeit");
        mod.setCertAlias(
            signatureAlgorithm.startsWith("http://www.w3.org/2000/09/xmldsig#dsa") ? "dsa" :
            signatureAlgorithm.startsWith("http://www.w3.org/2001/04/xmldsig-more#ecdsa") ? "ec" :
            "rsa");
        mod.setCertPassword("changeit");
    }
}
