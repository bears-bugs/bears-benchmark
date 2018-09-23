package nz.co.breakpoint.jmeter.modifiers;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.dom.message.WSSecBase;
import org.apache.wss4j.dom.message.WSSecHeader;
import org.apache.wss4j.dom.message.WSSecTimestamp;
import org.w3c.dom.Document;

public class WSSTimestampPreProcessor extends AbstractWSSecurityPreProcessor {

    private static final long serialVersionUID = 1;

    private static final Logger log = LoggingManager.getLoggerForClass();

    private int timeToLive;
    private boolean precisionInMilliSeconds = true;

    public WSSTimestampPreProcessor() throws ParserConfigurationException {
        super();
    }

    @Override
    protected Document build(Document document, WSSecHeader secHeader) throws WSSecurityException {
        log.debug("Initializing WSSecTimestamp");
        WSSecTimestamp secBuilder = new WSSecTimestamp(secHeader);

        secBuilder.setTimeToLive(getTimeToLive());
        secBuilder.setPrecisionInMilliSeconds(isPrecisionInMilliSeconds());

        log.debug("Building WSSecTimestamp");
        return secBuilder.build();
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    public boolean isPrecisionInMilliSeconds() {
		return precisionInMilliSeconds;
	}

	public void setPrecisionInMilliSeconds(boolean precisionInMilliSeconds) {
		this.precisionInMilliSeconds = precisionInMilliSeconds;
	}
}
