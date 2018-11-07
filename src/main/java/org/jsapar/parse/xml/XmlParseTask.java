package org.jsapar.parse.xml;

import org.jsapar.error.ErrorEvent;
import org.jsapar.error.ErrorEventListener;
import org.jsapar.error.JSaParException;
import org.jsapar.model.Cell;
import org.jsapar.model.CellType;
import org.jsapar.model.DateCell;
import org.jsapar.model.Line;
import org.jsapar.parse.*;
import org.jsapar.parse.cell.CellParser;
import org.jsapar.schema.SchemaCellFormat;
import org.jsapar.schema.Xml2SchemaBuilder;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.GregorianCalendar;

/**
 * Parses xml text that conform to the schema http://jsapar.tigris.org/XMLDocumentFormat/1.0
 */
public class XmlParseTask extends AbstractParseTask implements ParseTask {
    // private final static String SCHEMA_LANGUAGE =
    // "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    private Reader reader;

    public XmlParseTask(Reader reader) {
        this.reader = reader;
    }

    @Override
    public long execute() throws IOException{

        String schemaFileName = "/xml/schema/XMLDocumentFormat.xsd";
        InputStream schemaStream = Xml2SchemaBuilder.class.getResourceAsStream(schemaFileName);

        if (schemaStream == null)
            throw new FileNotFoundException("Could not find schema resource: " + schemaFileName);

        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            StreamSource ss = new StreamSource(schemaStream);
            Schema schema = schemaFactory.newSchema(ss);

            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);
            parserFactory.setSchema(schema);

            // factory.set
            SAXParser parser = parserFactory.newSAXParser();
            org.xml.sax.InputSource is = new org.xml.sax.InputSource(reader);
            JSaParSAXHandler handler = new JSaParSAXHandler(this, this);
            parser.parse(is, handler);
            return handler.currentLineNumber-1;
        } catch (ParserConfigurationException | SAXException e) {
            throw new JSaParException("XML parsing error.", e);
        }
    }

    private CellType makeCellType(String sXmlCellType) {
        return sXmlCellType==null ? null : Enum.valueOf(CellType.class, sXmlCellType.toUpperCase());
    }

    private class JSaParSAXHandler extends org.xml.sax.helpers.DefaultHandler {
        private Line     currentLine;
        private CellType currentCellType;
        private String   currentCellName;
        private Cell     currentCell;
        private boolean cellStarted = false;
        private LineEventListener  listener;
        private ErrorEventListener errorEventListener;
        private long currentLineNumber = 1;

        public JSaParSAXHandler(LineEventListener listener, ErrorEventListener errorEventListener) {
            this.listener = listener;
            this.errorEventListener = errorEventListener;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
         * java.lang.String, java.lang.String)
         */
        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            try {
                if (localName.equals("cell")) {
                    this.currentLine.addCell(this.currentCell);
                    this.currentCell = null;
                    this.cellStarted = false;
                } else if (localName.equals("line")) {
                    this.listener.lineParsedEvent(new LineParsedEvent(this, this.currentLine));
                    this.currentLine = null;
                }
            } catch (JSaParException e) {
                throw new SAXException("Error while handling parsed line.", e);
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
         * java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

            switch (localName) {
            case "cell":
                cellStarted = true;
                this.currentCellName = attributes.getValue("name");
                this.currentCellType = makeCellType(attributes.getValue("type"));
                if (this.currentCellType == null)
                    this.currentCellType = CellType.STRING;
                break;
            case "line":
                this.currentLine = new Line(attributes.getValue("linetype"));
                this.currentLine.setLineNumber(currentLineNumber);
                this.currentLineNumber++;
                break;
            case "document":
                break;
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
         */
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (this.cellStarted) {
                String sValue = new String(ch, start, length);
                try {
                    if (this.currentCellType == CellType.DATE) {
                        this.makeDateCell(sValue);
                    } else {
                        this.currentCell = CellParser
                                .makeCell(this.currentCellType, this.currentCellName, sValue, SchemaCellFormat.defaultLocale);
                    }
                    this.currentCellType = null;
                    this.currentCellName = null;
                } catch (Exception e) {
                    throw new SAXException("Failed to parse cell value", e);
                }
            }
        }

        /**
         * Creates a date cell from a date string value.
         *
         * @param value The text value to make a date cell from.
         * @throws DatatypeConfigurationException
         */
        private void makeDateCell(String value) throws DatatypeConfigurationException {
            XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(value);
            GregorianCalendar calendar = xmlCalendar.toGregorianCalendar();
            this.currentCell = new DateCell(this.currentCellName, calendar.getTime());
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.xml.sax.helpers.DefaultHandler#error(org.xml.sax.SAXParseException
         * )
         */
        @Override
        public void error(SAXParseException e) throws SAXException {
            CellParseException error = new CellParseException(this.currentLineNumber, this.currentCellName, "", null,
                    e.getMessage());
            this.errorEventListener.errorEvent(new ErrorEvent(this, error));
        }

        /*
         * (non-Javadoc)
         *
         * @seeorg.xml.sax.helpers.DefaultHandler#fatalError(org.xml.sax.
         * SAXParseException)
         */
        @Override
        public void fatalError(SAXParseException e) throws SAXException {
            CellParseException error = new CellParseException(this.currentLineNumber, this.currentCellName, "", null,
                    e.getMessage());
            this.errorEventListener.errorEvent(new ErrorEvent(this, error));
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.xml.sax.helpers.DefaultHandler#warning(org.xml.sax.SAXParseException
         * )
         */
        @Override
        public void warning(SAXParseException e) throws SAXException {
            CellParseException error = new CellParseException(this.currentLineNumber, this.currentCellName, "", null,
                    e.getMessage());
            this.errorEventListener.errorEvent(new ErrorEvent(this, error));
        }

    }

    /**
     * Closes attached reader
     * @throws IOException In case of error while closing reader.
     */
    @Override
    public void close() throws IOException {
        this.reader.close();
    }
}
