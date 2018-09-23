package nz.co.breakpoint.jmeter.modifiers;

import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/* The JMeter API lacks an interface for samplers that have a content or payload.
 * Instead, such samplers may have a JMeter property or getter/setter pair 
 * that exposes the payload. To access this for 3rd party samplers, the class
 * and property name can be configured via the JMeter property:
 * "jmeter.wssecurity.samplerPayloadAccessors"
 */
public class SamplerPayloadAccessor {

    private static final Logger log = LoggingManager.getLoggerForClass();
    
    public static final String ACCESSORS_CONFIG_PROPERTY = "jmeter.wssecurity.samplerPayloadAccessors";

    private static PropertyAccessor accessor = new PropertyAccessor(
        JMeterUtils.getPropDefault(ACCESSORS_CONFIG_PROPERTY, "") + PropertyAccessor.DELIMITER +
        "org.apache.jmeter.protocol.jms.sampler.JMSSampler.\"HTTPSamper.xml_data\"" + PropertyAccessor.DELIMITER +
        "org.apache.jmeter.protocol.jms.sampler.PublisherSampler.\"jms.text_message\"" + PropertyAccessor.DELIMITER +
        "org.apache.jmeter.protocol.tcp.sampler.TCPSampler.\"TCPSampler.request\"" + PropertyAccessor.DELIMITER +
        "org.apache.jmeter.protocol.smtp.sampler.SmtpSampler.\"SMTPSampler.message\""
    );

    // package access for unit tests with additional properties:
    static void parseProperties(String properties) {
        accessor.parseProperties(properties);
    }

    public static String getPayload(Sampler sampler) {
        if (sampler instanceof HTTPSamplerBase) {
            HTTPSamplerBase httpSampler = ((HTTPSamplerBase)sampler);
            
            if (!httpSampler.getPostBodyRaw()) {
                log.error("Raw post body required.");
                return null; 
            }
            return httpSampler.getArguments().getArgument(0).getValue(); 
        }
        return (String)accessor.getProperty(sampler);
    }

    public static void setPayload(Sampler sampler, String payload) {
        if (sampler instanceof HTTPSamplerBase) {
            ((HTTPSamplerBase)sampler).getArguments().getArgument(0).setValue(payload);
            return;
        }
        accessor.setProperty(sampler, payload);
    }
}
