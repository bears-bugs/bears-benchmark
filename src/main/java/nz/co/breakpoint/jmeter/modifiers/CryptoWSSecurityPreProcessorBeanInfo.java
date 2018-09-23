package nz.co.breakpoint.jmeter.modifiers;

import java.beans.PropertyDescriptor;
import org.apache.jmeter.testbeans.gui.FileEditor;
import org.apache.jmeter.testbeans.gui.TableEditor;
import org.apache.jmeter.testbeans.gui.PasswordEditor;

public class CryptoWSSecurityPreProcessorBeanInfo extends AbstractWSSecurityPreProcessorBeanInfo {

    public CryptoWSSecurityPreProcessorBeanInfo(Class<? extends CryptoWSSecurityPreProcessor> clazz) {
        super(clazz);

        createPropertyGroup("Certificate", new String[]{
            "keystoreFile", "keystorePassword", "certAlias", "certPassword"
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

        p = property("certAlias");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");

        p = property("certPassword");
        p.setPropertyEditorClass(PasswordEditor.class);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");
    }

    // This may be added to a property group by subclasses (if desired)
    protected PropertyDescriptor createPartsToSecureProperty() {
        PropertyDescriptor p = property("partsToSecure");
        p.setPropertyEditorClass(TableEditor.class);
        p.setValue(TableEditor.CLASSNAME, SecurityPart.class.getName());
        p.setValue(TableEditor.HEADERS, getTableHeadersWithDefaults("partsToSecure.tableHeaders", new String[]{"ID", "Name", "Namespace", "Encode"}));
        p.setValue(TableEditor.OBJECT_PROPERTIES, new String[]{"id", "name", "namespace", "modifier"});
        return p;
    }
}
