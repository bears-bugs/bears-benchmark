package nz.co.breakpoint.jmeter.modifiers;

import java.beans.PropertyDescriptor;

public class WSSTimestampPreProcessorBeanInfo extends AbstractWSSecurityPreProcessorBeanInfo{

    public WSSTimestampPreProcessorBeanInfo() {
        super(WSSTimestampPreProcessor.class);

        createPropertyGroup("Timestamp", new String[]{
            "timeToLive", "precisionInMilliSeconds",
        });
        PropertyDescriptor p;

        p = property("timeToLive");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, 300);

        p = property("precisionInMilliSeconds");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, Boolean.TRUE);
    }
}
