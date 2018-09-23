package nz.co.breakpoint.jmeter.modifiers;

import java.beans.PropertyDescriptor;
import org.apache.jmeter.testbeans.gui.PasswordEditor;

public class WSSUsernameTokenPreProcessorBeanInfo extends AbstractWSSecurityPreProcessorBeanInfo {

    public WSSUsernameTokenPreProcessorBeanInfo() {
        super(WSSUsernameTokenPreProcessor.class);

        createPropertyGroup("UsernameToken", new String[]{ 
            "username", "password", "passwordType", "addNonce", "addCreated", "precisionInMilliSeconds"
        });
        PropertyDescriptor p;

        p = property("username");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");

        p = property("password");
        p.setPropertyEditorClass(PasswordEditor.class);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");

        p = property("passwordType");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, WSSUsernameTokenPreProcessor.passwordTypes[0]);
        p.setValue(NOT_OTHER, Boolean.TRUE);
        p.setValue(TAGS, WSSUsernameTokenPreProcessor.passwordTypes);

        p = property("addNonce");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, Boolean.TRUE);

        p = property("addCreated");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, Boolean.TRUE);
        
        p = property("precisionInMilliSeconds");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, Boolean.TRUE);
    }
}
