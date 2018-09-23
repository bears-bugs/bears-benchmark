package nz.co.breakpoint.jmeter.modifiers;

import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.message.WSSecBase;
import org.apache.wss4j.dom.message.WSSecHeader;
import org.apache.wss4j.dom.message.WSSecSignature;
import org.apache.xml.security.algorithms.MessageDigestAlgorithm;
import org.apache.xml.security.signature.XMLSignature;
import org.w3c.dom.Document;

public class WSSSignaturePreProcessor extends CryptoWSSecurityPreProcessor {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggingManager.getLoggerForClass();

    private String signatureAlgorithm, signatureCanonicalization, digestAlgorithm;
    private boolean useSingleCertificate;

    /* Currently supported attributes are listed below.
     * The first value for each will be displayed in the GUI as default.
     */
    static final String[] keyIdentifiers = new String[]{
        getKeyIdentifierLabelForType(WSConstants.BST_DIRECT_REFERENCE),
        getKeyIdentifierLabelForType(WSConstants.ISSUER_SERIAL),
        getKeyIdentifierLabelForType(WSConstants.X509_KEY_IDENTIFIER),
        getKeyIdentifierLabelForType(WSConstants.SKI_KEY_IDENTIFIER),
        getKeyIdentifierLabelForType(WSConstants.THUMBPRINT_IDENTIFIER),
        getKeyIdentifierLabelForType(WSConstants.KEY_VALUE),
    };

    static final String[] signatureCanonicalizations = new String[]{
        WSConstants.C14N_EXCL_OMIT_COMMENTS, WSConstants.C14N_EXCL_WITH_COMMENTS,
        WSConstants.C14N_OMIT_COMMENTS,      WSConstants.C14N_WITH_COMMENTS,
    };

    static final String[] signatureAlgorithms = new String[]{
        XMLSignature.ALGO_ID_SIGNATURE_RSA,           XMLSignature.ALGO_ID_SIGNATURE_DSA,
        XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA1,    XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA256,
        XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA384,  XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA512,
        XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1,      XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256,
        XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA384,    XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA512,
    };

    static final String[] digestAlgorithms = new String[]{
        MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA1,      MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA256,
        MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA384,    MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA512,
    };

    public WSSSignaturePreProcessor() throws ParserConfigurationException {
        super();
    }

    @Override
    protected Document build(Document document, WSSecHeader secHeader) throws WSSecurityException {
        log.debug("Initializing WSSecSignature");
        WSSecSignature secBuilder = new WSSecSignature(secHeader);
        secBuilder.setExpandXopInclude(true); // don't sign just the xop reference but inline the attachment content first

        secBuilder.setUserInfo(getCertAlias(), getCertPassword());
        setKeyIdentifier(secBuilder);
        setPartsToSecure(secBuilder);
        secBuilder.setSignatureAlgorithm(getSignatureAlgorithm());
        secBuilder.setSigCanonicalization(getSignatureCanonicalization());
        secBuilder.setDigestAlgo(getDigestAlgorithm());
        secBuilder.setUseSingleCertificate(isUseSingleCertificate());
        updateAttachmentCallbackHandler();
        secBuilder.setAttachmentCallbackHandler(getAttachmentCallbackHandler());

        log.debug("Building WSSecSignature");
        return secBuilder.build(getCrypto());
    }

    // Accessors
    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public String getSignatureCanonicalization() {
        return signatureCanonicalization;
    }

    public void setSignatureCanonicalization(String signatureCanonicalization) {
        this.signatureCanonicalization = signatureCanonicalization;
    }

    public String getDigestAlgorithm() {
        return digestAlgorithm;
    }

    public void setDigestAlgorithm(String digestAlgorithm) {
        this.digestAlgorithm = digestAlgorithm;
    }

    public boolean isUseSingleCertificate() {
        return useSingleCertificate;
    }

    public void setUseSingleCertificate(boolean useSingleCertificate) {
        this.useSingleCertificate = useSingleCertificate;
    }

    public String getKeyIdentifier() {
        return keyIdentifier;
    }

    public void setKeyIdentifier(String keyIdentifier) {
        this.keyIdentifier = keyIdentifier;
    }

    public List<SecurityPart> getPartsToSecure() {
        return partsToSecure;
    }

    public void setPartsToSecure(List<SecurityPart> partsToSecure) {
        this.partsToSecure = partsToSecure;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
