package nz.co.breakpoint.jmeter.modifiers;

import java.beans.PropertyDescriptor;

public class AbstractWSSecurityPostProcessorBeanInfo extends AbstractWSSecurityTestElementBeanInfo {

    public AbstractWSSecurityPostProcessorBeanInfo(Class<? extends AbstractWSSecurityPostProcessor> clazz) {
        super(clazz);

        createPropertyGroup("Header", new String[]{
            "actor"
        });
        PropertyDescriptor p;

        p = property("actor");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");
    }
}
