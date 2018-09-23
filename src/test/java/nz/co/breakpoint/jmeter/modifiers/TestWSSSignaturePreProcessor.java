package nz.co.breakpoint.jmeter.modifiers;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.threads.JMeterContextService;
import org.junit.Before;
import org.junit.Test;

public class TestWSSSignaturePreProcessor extends TestWSSSecurityPreProcessorBase {
    private WSSSignaturePreProcessor mod = null;

    @Before
    public void setUp() throws Exception {
        mod = new WSSSignaturePreProcessor();
        mod.setThreadContext(JMeterContextService.getContext());
        initCertSettings(mod);
    }

    @Test
    public void testAllSignatureCombinations() throws Exception {
        for (String ki : WSSSignaturePreProcessor.keyIdentifiers) {
            for (String sc : WSSSignaturePreProcessor.signatureCanonicalizations) {
                for (String sa : WSSSignaturePreProcessor.signatureAlgorithms) {
                    for (String da : WSSSignaturePreProcessor.digestAlgorithms) {
                        for (boolean us : new boolean[]{true, false}) {
                            initCertSettings(mod, sa);
                            mod.setKeyIdentifier(ki);
                            mod.setSignatureCanonicalization(sc);
                            mod.setSignatureAlgorithm(sa);
                            mod.setDigestAlgorithm(da);
                            mod.setUseSingleCertificate(us);
                            HTTPSamplerBase sampler = createHTTPSampler();
                            mod.process();
                            String signedContent = SamplerPayloadAccessor.getPayload(sampler);
                            assertThat(signedContent, containsString("\"http://www.w3.org/2000/09/xmldsig#\""));
                            assertThat(signedContent, containsString(sc));
                            assertThat(signedContent, containsString(sa));
                            assertThat(signedContent, containsString(da));
                        }
                    }
                }
            }
        }
    }
}
