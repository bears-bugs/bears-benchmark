package nz.co.breakpoint.jmeter.modifiers;

import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.wss4j.dom.engine.WSSConfig;

/**
 * Abstract base class for any test element that deals with WSSecurity.
 * Initialises the wss4j configuration when the class is loaded.
 */
public abstract class AbstractWSSecurityTestElement extends AbstractXMLTestElement implements TestBean {

    static { WSSConfig.init(); }

    private String actor;
    protected List<Attachment> attachments = new ArrayList<Attachment>();
    private AttachmentCallbackHandler attachmentCallbackHandler = null;

    public AbstractWSSecurityTestElement() throws ParserConfigurationException {
        super();
    }

    protected Sampler getSampler() {
        return getThreadContext().getCurrentSampler();
    }

    protected SampleResult getResult() {
        return getThreadContext().getPreviousResult();
    }

    protected List<Attachment> getAttachments() {
        return attachments;
    }

    protected AttachmentCallbackHandler getAttachmentCallbackHandler() {
        return attachmentCallbackHandler;
    }

    // Get attachments from TestElement to attachmentCallbackHandler for wss4j processing
    protected void updateAttachmentCallbackHandler() {
        if (attachmentCallbackHandler == null) {
            attachmentCallbackHandler = new AttachmentCallbackHandler();
        }
        for (Attachment a : attachments) {
            // Multiple calls won't cause duplicates assuming that an attachment's cid won't change (so the same attachment would be overwritten):
            attachmentCallbackHandler.addAttachment(a.toWss4jAttachment());
        }
    }

    // Accessors
    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }
}
