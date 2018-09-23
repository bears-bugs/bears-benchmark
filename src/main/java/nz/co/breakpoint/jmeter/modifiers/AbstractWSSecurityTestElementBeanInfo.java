package nz.co.breakpoint.jmeter.modifiers;

import java.util.ResourceBundle;
import org.apache.jmeter.testbeans.BeanInfoSupport;
import static org.apache.jmeter.util.JMeterUtils.getJMeterVersion;

public class AbstractWSSecurityTestElementBeanInfo extends BeanInfoSupport {

    public AbstractWSSecurityTestElementBeanInfo(Class<? extends AbstractWSSecurityTestElement> clazz) {
        super(clazz);
        // custom icon depends on fix https://github.com/apache/jmeter/pull/399
        if (getJMeterVersion().compareTo("5.1") >= 0) {
            setIcon("padlock.png");
        }
    }

    // Convenience method for localized headers of TableEditor columns
    protected String[] getTableHeadersWithDefaults(String resourceName, String[] defaults) {
        ResourceBundle rb = (ResourceBundle)getBeanDescriptor().getValue(RESOURCE_BUNDLE);
        return rb != null && rb.containsKey(resourceName) ? 
            rb.getString(resourceName).split("\\|") : 
            defaults;
    }
}
