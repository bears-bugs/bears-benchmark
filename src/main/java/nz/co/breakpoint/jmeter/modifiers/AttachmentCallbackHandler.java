package nz.co.breakpoint.jmeter.modifiers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.apache.wss4j.common.ext.Attachment;
import org.apache.wss4j.common.ext.AttachmentRequestCallback;
import org.apache.wss4j.common.ext.AttachmentResultCallback;

/* A Callback Handler implementation for providing attachments to wss4j for signing/encryption
 * and retrieving processed attachments (encrypted/decrypted) from wss4j.
 * This is basically an attachment handover point between the plugin and wss4j.
 */
public class AttachmentCallbackHandler implements CallbackHandler {

    private static final Logger log = LoggingManager.getLoggerForClass();

    protected Map<String, Attachment> attachmentMap = new HashMap<String, Attachment>();

    public void addAttachment(Attachment attachment) {
        String cid = attachment.getId();
        log.debug("Adding attachment cid:"+cid);
        attachmentMap.put(cid, attachment);
    }

    public void addAttachments(Iterable<Attachment> attachments) {
        for (Attachment attachment : attachments) {
            addAttachment(attachment);
        }
    }

    public Attachment getAttachment(String id) {
        return attachmentMap.get(id);
    }

    // return type to match wss4j API
    public List<Attachment> getAttachments() {
        return new ArrayList<Attachment>(attachmentMap.values());
    }

    public void setAttachments(List<Attachment> attachments) {
        attachmentMap.clear();
        addAttachments(attachments);
    }

    /* This is called from wss4j when an attachment reference within the SOAP message is processed,
     * so as to retrieve the attachment content from us, 
     * or, when an attachment was processed (e.g. encrypted) to hand it back to us.
     */
    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            if (callback instanceof AttachmentRequestCallback) {
                AttachmentRequestCallback attachmentRequestCallback = (AttachmentRequestCallback) callback;
                String id = attachmentRequestCallback.getAttachmentId();

                if (getAttachment(id) != null) {
                    log.debug("Sending request attachment cid:"+id);
                    attachmentRequestCallback.setAttachments(Arrays.asList(getAttachment(id)));
                } else {
                    attachmentRequestCallback.setAttachments(getAttachments());
                    log.debug(getAttachments().isEmpty()? "No attachments found, nothing to do"
                        : "Sending a total of "+getAttachments().size()+" request attachment(s) (cid:"+id+" not found)");
                }
            }
            else if (callback instanceof AttachmentResultCallback) {
                AttachmentResultCallback attachmentResultCallback = (AttachmentResultCallback) callback;
                Attachment attachment = attachmentResultCallback.getAttachment();

                log.debug("Received result attachment cid:"+attachment.getId());
                addAttachment(attachment); // replace the map entry for the attachment Id with the processed (e.g. encrypted or decrypted) attachment
            }
            else {
                throw new UnsupportedCallbackException(callback, "Unrecognized Callback");
            }
        }
    }
}
