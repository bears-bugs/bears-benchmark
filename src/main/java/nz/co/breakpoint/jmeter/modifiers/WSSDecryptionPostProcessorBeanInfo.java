package nz.co.breakpoint.jmeter.modifiers;

import java.beans.PropertyDescriptor;
import org.apache.jmeter.testbeans.gui.TableEditor;

public class WSSDecryptionPostProcessorBeanInfo extends CryptoWSSecurityPostProcessorBeanInfo {

    public WSSDecryptionPostProcessorBeanInfo() {
        super(WSSDecryptionPostProcessor.class);

        createPropertyGroup("Decryption", new String[] { "attachments" });

        PropertyDescriptor p = property("attachments");
        p.setPropertyEditorClass(TableEditor.class);
        p.setValue(TableEditor.CLASSNAME, Attachment.class.getName());
        p.setValue(TableEditor.HEADERS, getTableHeadersWithDefaults("attachments.tableHeaders",
            new String[]{"Content-ID", "Bytes (base64)"}));
        p.setValue(TableEditor.OBJECT_PROPERTIES, new String[]{"name", "content"});
    }
}
