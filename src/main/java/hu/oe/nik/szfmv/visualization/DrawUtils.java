package hu.oe.nik.szfmv.visualization;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

/**
 * Helper classes for drawing world
 */
public abstract class DrawUtils {

    /**
     * Loads the transformation reference points from the resource xml into the referencePoints HashMap
     *
     * @param referencePoints    HashMap that contains reference points for images
     * @param referencePointsURI path to xml file
     * @throws ParserConfigurationException throws if parser has an error
     * @throws IOException                  throws if can't read xml
     * @throws SAXException
     */
    public static void loadReferencePoints(Map<String, Point> referencePoints, String referencePointsURI)
            throws ParserConfigurationException, IOException, SAXException {
        referencePoints.clear();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(referencePointsURI);

        NodeList nodes = document.getElementsByTagName("Image");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element image = (Element) nodes.item(i);
            String imageName = image.getAttribute("name");
            Element refPoint = (Element) image.getChildNodes().item(1);

            int x = Integer.parseInt(refPoint.getAttribute("x"));
            int y = Integer.parseInt(refPoint.getAttribute("y"));
            Point p = new Point(x, y);

            referencePoints.put(imageName, p);
        }
    }
}
