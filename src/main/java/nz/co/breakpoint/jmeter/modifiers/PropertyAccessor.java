package nz.co.breakpoint.jmeter.modifiers;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.FunctionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/* JMeter TestElements (such as samplers) may have certain attributes
 * that a Pre/Post-Processor wants to access.
 * Often, this is represented by a JMeter property, but sometimes a getter/setter pair may be
 * used (e.g. if the attibute is computed at runtime).
 * This class provides generic access to such attributes and can be configured
 * as a comma-separated list of class name (incl. package) and property name:
 * <className>.<propertyName>
 * <className>."<propertyName>"
 * The second format is intended for property names that contain dots.
 * The property will be interpreted as a JMeter Property first.
 * Failing that, access via a getter/setter method will be attempted.
 * Example:
 * org.apache.jmeter.protocol.jms.sampler.JMSSampler.content,org.apache.jmeter.protocol.smtp.sampler.SmtpSampler."SMTPSampler.message"
 * The JMSSampler's getContent/setContent methods will be called (using Reflection),
 * and the SmtpSampler's JMeter property SMTPSampler.message will be used.
 */
public class PropertyAccessor {

    private static final Logger log = LoggingManager.getLoggerForClass();

    protected static final String DELIMITER = ",";

    private static final String ACCESSORS_CONFIG_REGEX = "\\s*([^\"]*)\\.\"?([^\"]*)\"?\\s*";
    
    /* Collection of Class/Accessor pairs (rather than a lookup map) that stores all registered classes 
     * together with an accessor property name (JMeterProperty or bean property).
     * This will be iterated until a the first class is found that the test element is or is a subclass of.
     */
    protected final Map<Class<?>, String> registeredAccessors = new HashMap<Class<?>, String>();

    public PropertyAccessor(String accessors) {
        parseProperties(accessors);
    }

    // package access for unit tests
    void parseProperties(String accessors) {
        for (String classAndAccessor : accessors.split(DELIMITER)) {
            if (classAndAccessor.isEmpty()) continue;

            String className = classAndAccessor.replaceFirst(ACCESSORS_CONFIG_REGEX, "$1");
            String propertyName = classAndAccessor.replaceFirst(ACCESSORS_CONFIG_REGEX, "$2");
            
            log.debug("Registering accessor property for "+className+": "+propertyName);
            try {
                Class<?> clazz = Class.forName(className, false, PropertyAccessor.class.getClassLoader()); // load class without initialisation
                registeredAccessors.put(clazz, propertyName);
            }
            catch (ClassNotFoundException e) {
                log.error("TestElement class not found ("+className+")");
            }
        }
    }
    
    protected String findPropertyAccessor(TestElement element) {
        if (element != null) { // only for unit tests
            for (Map.Entry<Class<?>, String> classAndProperty : registeredAccessors.entrySet()) {
                if (classAndProperty.getKey().isAssignableFrom(element.getClass())) {
                    return classAndProperty.getValue();
                }
            }
            log.warn("Cannot find property accessor for "+element.getClass().getName());
        }
        return null;
    }

    public Object getProperty(TestElement element) {
        String propertyName = findPropertyAccessor(element);
        if (propertyName != null) {
            JMeterProperty prop = element.getProperty(propertyName);
            if (!(prop instanceof NullProperty)) {
                log.debug("Using JMeter property "+prop.getName()+" for getting property value");
                return (prop instanceof FunctionProperty) ? prop.getStringValue() // function evaluation only happens when getting the String value
                    : prop.getObjectValue(); // otherwise return actual stored Object (which may be a String)
            }
            try {
                PropertyDescriptor accessor = new PropertyDescriptor(propertyName, element.getClass());
                return accessor.getReadMethod().invoke(element, new Object[]{});
            }
            catch (IntrospectionException e) {
                log.error("Property getter not found ("+propertyName+")");
            }
            catch (InvocationTargetException e) {
                log.error("Property getter exception: "+e.getCause());
            }
            catch (IllegalAccessException e) {
                log.error("Property getter not accessible");
            }
        }
        return null;
    }

    public void setProperty(TestElement element, Object attribute) {
        String propertyName = findPropertyAccessor(element);
        if (propertyName != null) {
            JMeterProperty prop = element.getProperty(propertyName);
            if (!(prop instanceof NullProperty)) { // warning: will also be a NullProperty if the property is not set yet (even if the sampler normally has that property)
                log.debug("Using JMeter property "+prop.getName()+" for setting property value");
                prop.setObjectValue(attribute);
                return;
            }
            try {
                PropertyDescriptor accessor = new PropertyDescriptor(propertyName, element.getClass());
                accessor.getWriteMethod().invoke(element, new Object[]{attribute});
            }
            catch (IntrospectionException e) {
                log.error("Property setter not found ("+propertyName+")");
            }
            catch (InvocationTargetException e) {
                log.error("Property setter exception: "+e.getCause());
            }
            catch (IllegalAccessException e) {
                log.error("Property setter not accessible");
            }
        }
    }
}
