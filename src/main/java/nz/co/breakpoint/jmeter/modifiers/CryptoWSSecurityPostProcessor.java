package nz.co.breakpoint.jmeter.modifiers;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.wss4j.common.crypto.Crypto;
import org.apache.wss4j.common.crypto.CryptoFactory;
import org.apache.wss4j.common.ext.WSSecurityException;

import static org.apache.wss4j.common.crypto.Merlin.PREFIX;
import static org.apache.wss4j.common.crypto.Merlin.KEYSTORE_FILE;
import static org.apache.wss4j.common.crypto.Merlin.KEYSTORE_PASSWORD;

/**
 * Abstract parent class of any preprocessors that perform crypto operations (e.g. signature or encryption).
 */
public abstract class CryptoWSSecurityPostProcessor extends AbstractWSSecurityPostProcessor {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private final Properties cryptoProps = new Properties(); // Holds configured attributes for crypto instance

    private String certPassword;

    public CryptoWSSecurityPostProcessor() throws ParserConfigurationException {
        super();
    }

    protected Crypto getCrypto() throws WSSecurityException {
        // A new crypto instance needs to be created for every iteration as the config could contain variables which may change.
        log.debug("Getting crypto instance");
        return CryptoFactory.getInstance(cryptoProps);
    }

    // Accessors
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
}
