package hu.oe.nik.szfmv.visualization;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

public final class ReferencePointStore {

    private static Map<String, Point> referencePoints;
    private static final String referencePointsURI = "./src/main/resources/reference_points.xml";
    private static final Logger LOGGER = LogManager.getLogger();

    private ReferencePointStore() {
        try {
            DrawUtils.loadReferencePoints(referencePoints, referencePointsURI);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static Map<String, Point> getReferencePoints() {
        return referencePoints;
    }
}
