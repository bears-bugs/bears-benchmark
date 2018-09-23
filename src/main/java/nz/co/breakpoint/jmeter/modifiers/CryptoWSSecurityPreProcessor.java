package nz.co.breakpoint.jmeter.modifiers;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.wss4j.common.crypto.Crypto;
import org.apache.wss4j.common.crypto.CryptoFactory;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.message.WSSecBase;

import static org.apache.wss4j.common.crypto.Merlin.PREFIX;
import static org.apache.wss4j.common.crypto.Merlin.KEYSTORE_FILE;
import static org.apache.wss4j.common.crypto.Merlin.KEYSTORE_PASSWORD;

/**
 * Abstract parent class of any preprocessors that perform crypto operations (e.g. signature or encryption).
 */
public abstract class CryptoWSSecurityPreProcessor extends AbstractWSSecurityPreProcessor {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private final Properties cryptoProps = new Properties(); // Holds configured attributes for crypto instance

    protected List<SecurityPart> partsToSecure; // Holds the names of XML elements to secure (e.g. SOAP Body)
    protected String certAlias, certPassword, keyIdentifier;

    static final Map<String, Integer> keyIdentifierMap = new HashMap<String, Integer>();
    static {
        keyIdentifierMap.put("Binary Security Token",         WSConstants.BST_DIRECT_REFERENCE);
        keyIdentifierMap.put("Issuer Name and Serial Number", WSConstants.ISSUER_SERIAL);
        keyIdentifierMap.put("X509 Certificate",              WSConstants.X509_KEY_IDENTIFIER);
        keyIdentifierMap.put("Subject Key Identifier",        WSConstants.SKI_KEY_IDENTIFIER);
        keyIdentifierMap.put("Thumbprint SHA1 Identifier",    WSConstants.THUMBPRINT_IDENTIFIER);
        keyIdentifierMap.put("Encrypted Key SHA1",            WSConstants.ENCRYPTED_KEY_SHA1_IDENTIFIER); // only for encryption (symmetric signature not implemented yet - would require UI fields for setSecretKey or setEncrKeySha1value)
        keyIdentifierMap.put("Custom Key Identifier",         WSConstants.CUSTOM_KEY_IDENTIFIER); // not implemented yet (requires UI fields for setCustomTokenId and setCustomTokenValueType)
        keyIdentifierMap.put("Key Value",                     WSConstants.KEY_VALUE); // only for signature
        keyIdentifierMap.put("Endpoint Key Identifier",       WSConstants.ENDPOINT_KEY_IDENTIFIER); // not supported by Merlin https://ws.apache.org/wss4j/apidocs/org/apache/wss4j/common/crypto/Merlin.html#getX509Certificates-org.apache.wss4j.common.crypto.CryptoType-
    }

    public CryptoWSSecurityPreProcessor() throws ParserConfigurationException {
        super();
    }

    /* Reverse lookup for above keyIdentifierMap. Mainly used for populating the GUI dropdown.
     */
    protected static String getKeyIdentifierLabelForType(int keyIdentifierType) {
        for (Map.Entry<String, Integer> id : keyIdentifierMap.entrySet()) {
            if (id.getValue() == keyIdentifierType)
                return id.getKey();
        }
        return null;
    }

    protected Crypto getCrypto() throws WSSecurityException {
        // A new crypto instance needs to be created for every iteration as the config could contain variables which may change.
        log.debug("Getting crypto instance");
        return CryptoFactory.getInstance(cryptoProps);
    }

    // Accessors
    public String getCertAlias() {
        return certAlias;
    }

    public void setCertAlias(String certAlias) {
        this.certAlias = certAlias;
    }

    public String getCertPassword() {
        return certPassword;
    }

    public void setCertPassword(String certPassword) {
        this.certPassword = certPassword;
    }

    public String getKeystoreFile() {
        return cryptoProps.getProperty(PREFIX+KEYSTORE_FILE);
    }

    public void setKeystoreFile(String keystoreFile) {
        cryptoProps.setProperty(PREFIX+KEYSTORE_FILE, keystoreFile);
    }

    public String getKeystorePassword() {
        return cryptoProps.getProperty(PREFIX+KEYSTORE_PASSWORD);
    }

    public void setKeystorePassword(String keystorePassword) {
        cryptoProps.setProperty(PREFIX+KEYSTORE_PASSWORD, keystorePassword);
    }

    protected void setKeyIdentifier(WSSecBase secBuilder) {
        secBuilder.setKeyIdentifierType(keyIdentifierMap.get(keyIdentifier));
    }

    protected void setPartsToSecure(WSSecBase secBuilder) {
        secBuilder.getParts().clear();
        if (partsToSecure == null) return;
        for (SecurityPart part : partsToSecure) {
            secBuilder.getParts().add(part.getPart());
        }
    }
}
