package hu.oe.nik.szfmv.environment;

import hu.oe.nik.szfmv.environment.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static hu.oe.nik.szfmv.common.Utils.convertMatrixToRadians;

public abstract class XmlToModelConverter {
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Creates List of elements that can be Visualized
     *
     * @param xmlLocation location of the xml containing WorldObjects
     * @return a list of parsed xml objects
     * @throws IOException                  if the xml location is bad
     * @throws ParserConfigurationException when the parse configuration is bad
     * @throws SAXException                 .
     * @throws IOException                  .
     */
    public static List<WorldObject> build(String xmlLocation)
            throws ParserConfigurationException, IOException, SAXException {
        Road.loadReferencePoints();

        List<WorldObject> objectListToReturn = new ArrayList<WorldObject>();
        File inputFile = new File(xmlLocation);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);

        NodeList xmlNodes = doc.getElementsByTagName("Object");

        for (int iterator = 0; iterator < xmlNodes.getLength(); iterator++) {
            Node nodeToAdd = xmlNodes.item(iterator);
            try {
                objectListToReturn.add(readValueFromXml((Element) nodeToAdd));
            } catch (Exception e) {

                LOGGER.error("XML object parse Error in object #" + iterator + ": " + e.getMessage());
            }
        }
        return objectListToReturn;
    }

    /**
     * @param objectElement A Xml Object that can be converted to WorldObject
     * @return WorldObject created from Xml Object
     * @throws XMLSignatureException thrown if tag missing from XmlObject
     * @throws IOException           thrown if WorldObject representative picture not found
     */
    private static WorldObject readValueFromXml(Element objectElement)
            throws XMLSignatureException, IOException {


        //Find Position, Transform, type parameter in current object.
        String type = objectElement.getAttribute("type");
        Element position = null;
        Element transform = null;
        NodeList objectChildNodes = objectElement.getChildNodes();
        for (int i = 0; i < objectChildNodes.getLength(); i++) {
            switch (objectChildNodes.item(i).getNodeName()) {
                case "Position":
                    position = (Element) objectChildNodes.item(i);
                    break;
                case "Transform":
                    transform = (Element) objectChildNodes.item(i);
                    break;
                default:
            }
        }
        if (position == null || transform == null) {
            throw new XMLSignatureException("Invalid format: Not found Position or Transform in Object");
        }

        WorldObject wo = createObjectFromType(type);
        //Set setImageFileName
        wo.setImageFileName(type + ".png");

        //set dimens
        wo.generateDimens();

        //Set position
        Integer[] points = getPointsFromPositionElement(position);
        wo.setX(points[0]);
        wo.setY(points[1]);

        //Set rotation
        wo.setRotation(getRotacionFromTransformElement(transform));

        //Shape drowing
        wo.generateShape();

        LOGGER.debug(wo.toString());
        return wo;
    }

    /**
     * @param type type read from XmlObject, determines what kind the class to be created
     * @return new class, based on type
     * @throws XMLSignatureException in case type not found
     */
    private static WorldObject createObjectFromType
    (String type) throws XMLSignatureException {
        WorldObject wo;
        // road_something_something -> road
        if (type.indexOf('_') != -1) {
            type = type.substring(0, type.indexOf('_'));
        }
        switch (type) {
            case "road":
                wo = new Road();
                break;
            case "parking":
                wo = new ParkingSpot();
                break;
            case "crosswalk":
                wo = new Crosswalk();
                break;
            case "roadsign":
                wo = new RoadSign();
                break;
            case "tree":
                wo = new Tree();
                break;
            case "woman":
                wo = new Pedestrian();
            default:
                throw new XMLSignatureException("Invalid Object type: " + type);
        }
        return wo;
    }

    /**
     * @param position XML element, contain positions param
     * @return Array of integer, contains point parameter
     * @throws XMLSignatureException position parse error
     */
    private static Integer[] getPointsFromPositionElement(Element position)
            throws XMLSignatureException {
        //points[0]=>x
        //points[1]=>y
        Integer[] points = new Integer[2];
        try {
            points[0] = Integer.parseInt(position.getAttribute("x"));
            points[1] = Integer.parseInt(position.getAttribute("y"));
            return points;
        } catch (NumberFormatException e) {
            throw new XMLSignatureException("Invalid format: Position attributes is not Integer: " + e.getMessage());
        }
    }

    /**
     * @param transform XML element, contain transform matrix param
     * @return rotacion param
     * @throws XMLSignatureException transform matrix error
     */
    private static float getRotacionFromTransformElement(Element transform)
            throws XMLSignatureException {
        //Inicialize
        double m11;
        double m12;
        double m21;
        double m22;
        try {
            //Get and parse attribute from element
            m11 = Double.parseDouble(transform.getAttribute("m11"));
            m12 = Double.parseDouble(transform.getAttribute("m12"));
            m21 = Double.parseDouble(transform.getAttribute("m21"));
            m22 = Double.parseDouble(transform.getAttribute("m22"));
            //convert transform matirx to rotacion
            return (float) convertMatrixToRadians(m11, m12, m21, m22);
        } catch (NumberFormatException e) {
            throw new XMLSignatureException("Invalid format: Transform attributes is not Double: " + e.getMessage());
        }
    }
}
