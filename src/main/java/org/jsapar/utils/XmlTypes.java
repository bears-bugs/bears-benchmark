package org.jsapar.utils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface XmlTypes {
    String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    /**
     * Utility function to retrieve first matching child element.
     *
     * @param nameSpace     The name space of the child.
     * @param parentElement The parent element
     * @param sChildName The name of the child element to get.
     * @return The child element or null if none found.
     */
    default Optional<Element> getChild(String nameSpace, Element parentElement, String sChildName) {
        NodeList nodes = parentElement.getElementsByTagNameNS(nameSpace, sChildName);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node child = nodes.item(i);
            if (child instanceof Element) {
                return Optional.of((Element) child);
            }
        }
        return Optional.empty();
    }

    default Stream<Element> getChildrenStream(String nameSpace, Element parentElement, String sChildName) {
        return getChildrenStream(()->parentElement.getElementsByTagNameNS(nameSpace, sChildName));
    }

    default Stream<Element> getChildrenStream(Element parentElement, String sChildName) {
        return getChildrenStream(()->parentElement.getElementsByTagName(sChildName));
    }

    default Stream<Element> getChildrenStream(Supplier<NodeList> supplier) {
        NodeList nodes = supplier.get();
        return IntStream.range(0, nodes.getLength())
                .mapToObj(nodes::item)
                .filter(n->n instanceof Element)
                .map(e->(Element) e);
    }

    /**
     * Utility function to convert a boolean xml node into a boolean.
     *
     * @param node The node to get boolean value from.
     * @return A boolean value.
     */
    default boolean getBooleanValue(Node node) {
        final String value = node.getNodeValue().trim().toLowerCase();
        switch (value) {
        case "true":
        case "1":
            return true;
        case "false":
        case "0":
            return false;
        default:
            throw new NumberFormatException("Failed to parse boolean node: " + node);
        }
    }

    default String getStringValue(Node node) {
        return node.getNodeValue().trim();
    }

    default Optional<String> attributeValue(Element parent, String name) {
        Node child = parent.getAttributeNode(name);
        if (child == null)
            return Optional.empty();
        else
            return Optional.of(child.getNodeValue());
    }

    default int getIntValue(Node node) {
        return Integer.parseInt(node.getNodeValue().trim());
    }


    default ErrorHandler makeDefaultErrorHandler(){
        return  new ErrorHandler() {
            @Override
            public void error(SAXParseException e) throws SAXException {
                if (e != null)
                    throw e;
                throw new SAXException("Unknown error while parsing xml");
            }

            @Override
            public void fatalError(SAXParseException e) throws SAXException {
                if (e != null)
                    throw e;
                throw new SAXException("Unknown error while parsing xml");
            }

            @Override
            public void warning(SAXParseException e) {
                // System.out.println("Warning while validating schema" +
                // e);
            }
        };

    }
}
