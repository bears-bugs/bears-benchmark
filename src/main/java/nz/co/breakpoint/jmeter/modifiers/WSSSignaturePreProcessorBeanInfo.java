package nz.co.breakpoint.jmeter.modifiers;

import java.beans.PropertyDescriptor;
import org.apache.jmeter.testbeans.gui.TableEditor;

public class WSSSignaturePreProcessorBeanInfo extends CryptoWSSecurityPreProcessorBeanInfo {

    public WSSSignaturePreProcessorBeanInfo() {
        super(WSSSignaturePreProcessor.class);

        createPropertyGroup("Signature", new String[]{ 
            "keyIdentifier", "signatureAlgorithm", "signatureCanonicalization", "digestAlgorithm", "useSingleCertificate",
            createPartsToSecureProperty().getName(), "attachments"
        });
        PropertyDescriptor p;

        p = property("keyIdentifier");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, WSSSignaturePreProcessor.keyIdentifiers[0]);
        p.setValue(NOT_OTHER, Boolean.TRUE);
        p.setValue(TAGS, WSSSignaturePreProcessor.keyIdentifiers);

        p = property("signatureAlgorithm");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, WSSSignaturePreProcessor.signatureAlgorithms[0]);
        p.setValue(NOT_OTHER, Boolean.TRUE);
        p.setValue(TAGS, WSSSignaturePreProcessor.signatureAlgorithms);

        p = property("signatureCanonicalization");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, WSSSignaturePreProcessor.signatureCanonicalizations[0]);
        p.setValue(NOT_OTHER, Boolean.TRUE);
        p.setValue(TAGS, WSSSignaturePreProcessor.signatureCanonicalizations);

        p = property("digestAlgorithm");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, WSSSignaturePreProcessor.digestAlgorithms[0]);
        p.setValue(NOT_OTHER, Boolean.TRUE);
        p.setValue(TAGS, WSSSignaturePreProcessor.digestAlgorithms);

        p = property("useSingleCertificate");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, Boolean.FALSE);

        p = property("attachments");
        p.setPropertyEditorClass(TableEditor.class);
        p.setValue(TableEditor.CLASSNAME, Attachment.class.getName());
        p.setValue(TableEditor.HEADERS, getTableHeadersWithDefaults("attachments.tableHeaders",
            new String[]{"Content-ID", "Bytes (base64)", "Headers"}));
        p.setValue(TableEditor.OBJECT_PROPERTIES, new String[]{"name", "content", "headers"});
    }
}
