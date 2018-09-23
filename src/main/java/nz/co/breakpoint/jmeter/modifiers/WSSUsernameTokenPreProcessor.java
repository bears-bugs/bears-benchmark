package nz.co.breakpoint.jmeter.modifiers;

import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.dom.message.WSSecBase;
import org.apache.wss4j.dom.message.WSSecHeader;
import org.apache.wss4j.dom.message.WSSecUsernameToken;
import org.apache.wss4j.dom.WSConstants;
import org.w3c.dom.Document;

public class WSSUsernameTokenPreProcessor extends AbstractWSSecurityPreProcessor { 

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggingManager.getLoggerForClass();

    private String username;
    private String password;
    private String passwordType;
    private boolean addNonce;
    private boolean addCreated;
    private boolean precisionInMilliSeconds;

    /* Currently supported attributes are listed below.
     * The first value for each will be displayed in the GUI as default.
     * TODO localization
     */
    static final String[] passwordTypes = new String[]{
        "Password Digest",
        "Password Text",
        "No Password",
    };

    static final Map<String, String> passwordTypeMap = new HashMap<String, String>();
    static {
        passwordTypeMap.put(passwordTypes[0], WSConstants.PASSWORD_DIGEST);
        passwordTypeMap.put(passwordTypes[1], WSConstants.PASSWORD_TEXT);
        passwordTypeMap.put(passwordTypes[2], null); // wss4j does not seem to use WSConstants.PW_NONE
    }

    public WSSUsernameTokenPreProcessor() throws ParserConfigurationException {
        super();
    }

    @Override
    protected Document build(Document document, WSSecHeader secHeader) throws WSSecurityException {
        log.debug("Initializing WSSecUsernameToken");
        WSSecUsernameToken secBuilder = new WSSecUsernameToken(secHeader);

        secBuilder.setUserInfo(getUsername(), getPassword());
        if (isAddNonce()) secBuilder.addNonce();
        if (isAddCreated()) secBuilder.addCreated();
        secBuilder.setPrecisionInMilliSeconds(isPrecisionInMilliSeconds());
        secBuilder.setPasswordType(passwordTypeMap.get(getPasswordType()));

        if ("No Password".equals(getPasswordType())) {
            /* An empty password GUI field will result in an empty password string "", regardless of password type.
             * This would cause a NPE in org.apache.wss4j.dom.message.token.UsernameToken.setPassword("") 
             * (https://github.com/apache/wss4j/blob/wss4j-2.2.2/ws-security-dom/src/main/java/org/apache/wss4j/dom/message/token/UsernameToken.java#L508)
             * when trying to retrieve the password element that is not there (as per password type).
             * Therefore it needs to be set to null explicitly.
             */
            secBuilder.setUserInfo(getUsername(), null);
        }
        log.debug("Building WSSecUsernameToken");
        return secBuilder.build();
    }

    // Accessors
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordType() {
        return passwordType;
    }

    public void setPasswordType(String passwordType) {
        this.passwordType = passwordType;
    }

    public boolean isAddNonce() {
        return addNonce;
    }

    public void setAddNonce(boolean addNonce) {
        this.addNonce = addNonce;
    }

    public boolean isAddCreated() {
        return addCreated;
    }

    public void setAddCreated(boolean addCreated) {
        this.addCreated = addCreated;
    }

    public boolean isPrecisionInMilliSeconds() {
		return precisionInMilliSeconds;
	}

	public void setPrecisionInMilliSeconds(boolean precisionInMilliSeconds) {
		this.precisionInMilliSeconds = precisionInMilliSeconds;
	}
}
