package nz.co.breakpoint.jmeter.modifiers;

import java.beans.PropertyDescriptor;
import org.apache.jmeter.testbeans.gui.TableEditor;

public class WSSEncryptionPreProcessorBeanInfo extends CryptoWSSecurityPreProcessorBeanInfo {

    public WSSEncryptionPreProcessorBeanInfo() {
        super(WSSEncryptionPreProcessor.class);

        createPropertyGroup("Encryption", new String[]{ 
            "keyIdentifier", "symmetricEncryptionAlgorithm", "keyEncryptionAlgorithm", "createEncryptedKey",
            createPartsToSecureProperty().getName(), "attachments"
        });
        PropertyDescriptor p;

        p = property("keyIdentifier");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, WSSEncryptionPreProcessor.keyIdentifiers[0]);
        p.setValue(NOT_OTHER, Boolean.TRUE);
        p.setValue(TAGS, WSSEncryptionPreProcessor.keyIdentifiers);

        p = property("symmetricEncryptionAlgorithm");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, WSSEncryptionPreProcessor.symmetricEncryptionAlgorithms[0]);
        p.setValue(NOT_OTHER, Boolean.TRUE);
        p.setValue(TAGS, WSSEncryptionPreProcessor.symmetricEncryptionAlgorithms);

        p = property("keyEncryptionAlgorithm");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, WSSEncryptionPreProcessor.keyEncryptionAlgorithms[0]);
        p.setValue(NOT_OTHER, Boolean.TRUE);
        p.setValue(TAGS, WSSEncryptionPreProcessor.keyEncryptionAlgorithms);

        p = property("createEncryptedKey");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, Boolean.TRUE);

        p = property("attachments");
        p.setPropertyEditorClass(TableEditor.class);
        p.setValue(TableEditor.CLASSNAME, Attachment.class.getName());
        p.setValue(TableEditor.HEADERS, getTableHeadersWithDefaults("attachments.tableHeaders",
            new String[]{"Content-ID", "Bytes (base64-encoded)", "Headers", "Output Mode", "Output Destination"}));
        p.setValue(TableEditor.OBJECT_PROPERTIES, new String[]{"name", "content", "headers", "mode", "destination"});
    }
}
