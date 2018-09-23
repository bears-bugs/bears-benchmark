package nz.co.breakpoint.jmeter.modifiers;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.dom.message.WSSecHeader;
import org.apache.wss4j.dom.message.WSSecBase;
import org.w3c.dom.Document;

/**
 * Abstract base class for any preprocessor that creates/modifies a SOAP WSS header in the sampler payload.
 * Subclasses need to implement a build method that creates the actual header element.
 */
public abstract class AbstractWSSecurityPreProcessor extends AbstractWSSecurityTestElement implements PreProcessor, TestBean {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private boolean mustUnderstand;

    public AbstractWSSecurityPreProcessor() throws ParserConfigurationException {
        super();
    }

    protected String getSamplerPayload() {
        log.debug("Getting sampler payload");
        return SamplerPayloadAccessor.getPayload(getSampler());
    }

    protected void setSamplerPayload(String payload) {
        log.debug("Setting sampler payload");
        SamplerPayloadAccessor.setPayload(getSampler(), payload);
    }

    /* Subclasses are to implement the actual creation of the signature or encryption.
     */
    protected abstract Document build(Document document, WSSecHeader secHeader)
        throws WSSecurityException;

    /* The main method that is called before the sampler.
     * This will get, parse, secure (sign or encrypt) and then replace
     * the sampler's payload.
     */
    @Override
    public void process() {
        String xml = getSamplerPayload();
        if (xml == null) return;

        try {
            log.debug("Parsing xml payload");
            Document doc = stringToDocument(xml);

            log.debug("Initializing WSS header");
            WSSecHeader secHeader = new WSSecHeader(getActor(), isMustUnderstand(), doc);
            secHeader.insertSecurityHeader(); // Create header unless one exists

            log.debug("Building WSS header");
            doc = this.build(doc, secHeader); // Delegate in abstract method

            setSamplerPayload(documentToString(doc));

            retrieveProcessedAttachments();
        }
        catch (Exception e) {
            log.error("Processing failed! ", e);
        }
    }

    /* The attachmentCallbackHandler may contain request attachments that have been processed
     * (encrypted or signed) by wss4j. This method retrieves any attachments the user cares about
     * (i.e. listed in the script) and makes them available as a file or JMeter variable, so
     * they can be attached to the actual sampler in order to send them.
     */
    protected void retrieveProcessedAttachments() {
        if (getAttachmentCallbackHandler() == null) {
            log.debug("AttachmentCallbackHandler undefined, skip retrieving attachments");
        } else if (getAttachments() == null) {
            log.debug("No attachments defined, skip retrieving attachments");
        } else {
            for (Attachment a : getAttachments()) {
                String cid = a.getName();
                org.apache.wss4j.common.ext.Attachment attachment = getAttachmentCallbackHandler().getAttachment(cid);

                if (attachment == null) {
                    log.warn("No attachment found with cid:"+cid);
                } else {
                    log.debug((a.getMode().length() == 0) ? "No output mode defined, discarding attachment cid:"+cid
                        : "Storing attachment cid:"+cid+" as "+a.getMode()+"="+a.getDestination());
                    try {
                        a.toJmeter(attachment.getSourceStream());
                    }
                    catch (IOException e) {
                        log.error("Failed to store attachment cid:"+cid, e);
                    }
                }
            }
        }
    }

    // Accessors
    public boolean isMustUnderstand() {
        return mustUnderstand;
    }

    public void setMustUnderstand(boolean mustUnderstand) {
        this.mustUnderstand = mustUnderstand;
    }
}
