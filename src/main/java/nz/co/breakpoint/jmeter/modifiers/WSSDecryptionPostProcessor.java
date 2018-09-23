package nz.co.breakpoint.jmeter.modifiers;

import java.io.IOException;
import java.util.List;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.dom.engine.WSSecurityEngine;
import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.handler.WSHandlerResult;
import org.w3c.dom.Document;

public class WSSDecryptionPostProcessor extends CryptoWSSecurityPostProcessor {

    private static final long serialVersionUID = 1L;

    public WSSDecryptionPostProcessor() throws ParserConfigurationException {
        super();
    }

    @Override
    protected Document process(Document document) throws WSSecurityException {
        WSSecurityEngine secEngine = new WSSecurityEngine();

        RequestData requestData = new RequestData();
        requestData.setSigVerCrypto(getCrypto());
        requestData.setDecCrypto(getCrypto());
        requestData.setActor(getActor());
        updateAttachmentCallbackHandler();
        requestData.setAttachmentCallbackHandler(getAttachmentCallbackHandler());
        requestData.setExpandXopInclude(true);
        requestData.setCallbackHandler(new CallbackHandler() {
            public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                for (Callback callback : callbacks) {
                    if (callback instanceof WSPasswordCallback) {
                        ((WSPasswordCallback)callback).setPassword(getCertPassword());
                    }
                }
            }
        });
        WSHandlerResult results = secEngine.processSecurityHeader(document, requestData);

        return document;
    }

    // Accessors
    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
