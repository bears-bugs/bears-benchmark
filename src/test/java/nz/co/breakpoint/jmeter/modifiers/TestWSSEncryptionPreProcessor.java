package nz.co.breakpoint.jmeter.modifiers;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.threads.JMeterContextService;
import org.junit.Before;
import org.junit.Test;

public class TestWSSEncryptionPreProcessor extends TestWSSSecurityPreProcessorBase {
    private WSSEncryptionPreProcessor mod = null;

    @Before
    public void setUp() throws Exception {
        mod = new WSSEncryptionPreProcessor();
        mod.setThreadContext(JMeterContextService.getContext());
        initCertSettings(mod);
    }

    @Test
    public void testAllEncryptionCombinations() throws Exception {
        for (String ki : WSSEncryptionPreProcessor.keyIdentifiers) {
            for (String ke : WSSEncryptionPreProcessor.keyEncryptionAlgorithms) {
                for (String se : WSSEncryptionPreProcessor.symmetricEncryptionAlgorithms) {
                    for (boolean ek : new boolean[]{true, false}) {
                        initCertSettings(mod);
                        mod.setKeyIdentifier(ki);
                        mod.setKeyEncryptionAlgorithm(ke);
                        mod.setSymmetricEncryptionAlgorithm(se);
                        mod.setCreateEncryptedKey(ek);
                        HTTPSamplerBase sampler = createHTTPSampler();
                        mod.process();
                        String encryptedContent = SamplerPayloadAccessor.getPayload(sampler);
                        assertThat(encryptedContent, containsString("Type=\"http://www.w3.org/2001/04/xmlenc#Content\""));
                        assertThat(encryptedContent, containsString(se));
                    }
                }
            }
        }
    }
}
