package nz.co.breakpoint.jmeter.modifiers;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.wss4j.common.util.XMLUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Abstract base class for any test element that parses and processes XML documents.
 */
public abstract class AbstractXMLTestElement extends AbstractTestElement { 

    private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    static { factory.setNamespaceAware(true); }

    transient private final DocumentBuilder docBuilder; // Handles the XML document

    public AbstractXMLTestElement() throws ParserConfigurationException {
        super();
        docBuilder = factory.newDocumentBuilder();
    }

    public Document stringToDocument(String xml) throws IOException, SAXException {
        return (xml == null) ? null : docBuilder.parse(new InputSource(new StringReader(xml)));
    }

    public String documentToString(Document document) throws IOException, TransformerException {
        return (document == null) ? null : XMLUtils.prettyDocumentToString(document);
    }
}
