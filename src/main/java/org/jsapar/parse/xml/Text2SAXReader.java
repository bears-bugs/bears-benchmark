package org.jsapar.parse.xml;

import org.jsapar.TextParser;
import org.jsapar.error.JSaParException;
import org.jsapar.model.Cell;
import org.jsapar.model.DateCell;
import org.jsapar.model.Line;
import org.jsapar.parse.LineParsedEvent;
import org.xml.sax.*;
import org.xml.sax.helpers.AttributesImpl;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 */
public class Text2SAXReader implements XMLReader {

    private static final String         URI = "";
    private              ContentHandler contentHandler;
    private              TextParser     textParser;
    private              ErrorHandler   errorHandler;

    public Text2SAXReader(TextParser textParser) {
        this.textParser = textParser;
    }

    @Override
    public void parse(InputSource inputSource) throws IOException, SAXException {
        contentHandler.startDocument();
        contentHandler.startElement(URI, "document", "document", new AttributesImpl());

        textParser.parse(inputSource.getCharacterStream(), this::handleLineParsedEvent);
        contentHandler.endElement(URI, "document", "document");
        contentHandler.endDocument();
    }

    private void handleLineParsedEvent(LineParsedEvent event) {
        AttributesImpl attributes = new AttributesImpl();
        Line line = event.getLine();
        attributes.addAttribute(URI, "linetype", "linetype", "CDATA", line.getLineType());
        try {
            contentHandler.startElement(URI, "line", "line", attributes);
            line.forEach( c ->{
                attributes.clear();
                attributes.addAttribute(URI, "name", "name", "CDATA", c.getName());
                attributes.addAttribute(URI, "type", "type", "CDATA", c.getCellType().name().toLowerCase());
                try {
                    contentHandler.startElement(URI, "cell", "cell", attributes);
                    char[] value = makeCellXmlValue(c).toCharArray();
                    contentHandler.characters(value, 0, value.length);
                    contentHandler.endElement(URI, "cell", "cell");
                } catch (SAXParseException e) {
                    try {
                        errorHandler.error(e);
                    } catch (SAXException e1) {
                        handleSAXException(e);
                    }
                } catch (SAXException e) {
                    handleSAXException(e);
                }
            });
            contentHandler.endElement(URI, "line", "line");
        } catch (SAXParseException e) {
            try {
                errorHandler.error(e);
            } catch (SAXException e1) {
                handleSAXException(e);
            }
        } catch (SAXException e) {
            handleSAXException(e);
        }
    }

    private String makeCellXmlValue(Cell c) {
        switch (c.getCellType()) {
        case DATE:
            Date value = ((DateCell) c).getValue();
            ZonedDateTime zDate = ZonedDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault());
            return zDate.toString();
        default:
            return c.getStringValue();
        }
    }

    private void handleSAXException(SAXException e) {
        throw new JSaParException("Failed to generate SAX event", e);
    }

    @Override
    public void parse(String s) {

    }

    @Override
    public boolean getFeature(String s) {
        return false;
    }

    @Override
    public void setFeature(String s, boolean b) {

    }

    @Override
    public Object getProperty(String s) {
        return null;
    }

    @Override
    public void setProperty(String s, Object o) {

    }

    @Override
    public void setEntityResolver(EntityResolver entityResolver) {

    }

    @Override
    public EntityResolver getEntityResolver() {
        return null;
    }

    @Override
    public void setDTDHandler(DTDHandler dtdHandler) {

    }

    @Override
    public DTDHandler getDTDHandler() {
        return null;
    }

    @Override
    public void setContentHandler(ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    @Override
    public ContentHandler getContentHandler() {
        return this.contentHandler;
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;

    }

    @Override
    public ErrorHandler getErrorHandler() {
        return null;
    }

}
