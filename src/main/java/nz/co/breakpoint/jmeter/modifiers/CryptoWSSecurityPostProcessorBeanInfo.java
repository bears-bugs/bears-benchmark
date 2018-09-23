package nz.co.breakpoint.jmeter.modifiers;

import java.beans.PropertyDescriptor;
import org.apache.jmeter.testbeans.gui.FileEditor;
import org.apache.jmeter.testbeans.gui.PasswordEditor;

public class CryptoWSSecurityPostProcessorBeanInfo extends AbstractWSSecurityPostProcessorBeanInfo {

    public CryptoWSSecurityPostProcessorBeanInfo(Class<? extends CryptoWSSecurityPostProcessor> clazz) {
        super(clazz);

        createPropertyGroup("Certificate", new String[]{
            "keystoreFile", "keystorePassword", "certPassword"
        });
        PropertyDescriptor p;

        p = property("keystoreFile");
        p.setPropertyEditorClass(FileEditor.class);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");

        p = property("keystorePassword");
        p.setPropertyEditorClass(PasswordEditor.class);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");

        p = property("certPassword");
        p.setPropertyEditorClass(PasswordEditor.class);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");
    }
}
