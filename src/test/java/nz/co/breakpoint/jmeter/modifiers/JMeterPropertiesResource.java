package nz.co.breakpoint.jmeter.modifiers;

import java.io.File;
import java.io.IOException;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.rules.ExternalResource;

/* Shared dummy JMeter properties file which JMeter needs for its initialisation.
 */
public class JMeterPropertiesResource extends ExternalResource {
    protected void before() {
        try {
            File props = File.createTempFile("jmeter-wss-test", ".properties");
            props.deleteOnExit();
            JMeterUtils.loadJMeterProperties(props.getAbsolutePath());
        }
        catch (IOException e) {
            System.out.println("Failed to create JMeter properties file: "+e.getMessage());
        }
    }
}