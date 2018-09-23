package nz.co.breakpoint.jmeter.modifiers;

import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestElement;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestPropertyAccessor {
    class DummyTestElement extends AbstractTestElement {
        String prop;
        public void setProp(String value) { prop = value; }
        public String getProp() { return prop; }
    }

    @Test
    public void testGetJMeterProperty() throws Exception {
        DummyTestElement element = new DummyTestElement();
        PropertyAccessor instance = new PropertyAccessor(element.getClass().getName()+".jprop");
        element.setProperty("jprop", "foobar");
        assertEquals("foobar", instance.getProperty(element));
    }

    @Test
    public void testSetJMeterProperty() throws Exception {
        DummyTestElement element = new DummyTestElement();
        PropertyAccessor instance = new PropertyAccessor(element.getClass().getName()+".jprop");
        element.setProperty("jprop", ""); // make sure property exists
        instance.setProperty(element, "foobar");
        assertEquals("foobar", element.getPropertyAsString("jprop"));
    }

    @Test
    public void testGetBeanProperty() throws Exception {
        DummyTestElement element = new DummyTestElement();
        PropertyAccessor instance = new PropertyAccessor(element.getClass().getName()+".prop");
        element.prop = "foobar";
        assertEquals("foobar", instance.getProperty(element));
    }

    @Test
    public void testSetBeanProperty() throws Exception {
        DummyTestElement element = new DummyTestElement();
        PropertyAccessor instance = new PropertyAccessor(element.getClass().getName()+".prop");
        instance.setProperty(element, "foobar");
        assertEquals("foobar", element.prop);
    }

}
