package org.jsapar.schema;

import org.jsapar.model.CellType;
import org.jsapar.utils.XmlTypes;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Locale;

/**
 * Builds a {@link Schema} instance from xml that conforms to the JSaPar xsd.
 */
public class Xml2SchemaBuilder implements SchemaXmlTypes, XmlTypes {

    /**
     * Utility function to retrieve first matching child element.
     * 
     * @param parentElement The parent element
     * @param sChildName The name of the child element to get.
     * @return The child element or null if none found.
     */
    private Element getChild(Element parentElement, String sChildName) {
        return getChild(JSAPAR_XML_SCHEMA, parentElement, sChildName).orElse(null);
    }

    private String getAttributeValue(Element parent, String name) {
        return attributeValue(parent, name).orElse(null);
    }

    /**
     * Generates a schema from a xml file.
     * 
     * @param reader
     *            - A reader linked to the xml file.
     * @return The file parser schema.
     * @throws SchemaException When there is an error in the schema
     * @throws UncheckedIOException When there is an error reading the input
     */
    public Schema build(Reader reader) throws UncheckedIOException, SchemaException {
        String schemaFileName = "/xml/schema/JSaParSchema.xsd";

        try(InputStream schemaStream = Xml2SchemaBuilder.class.getResourceAsStream(schemaFileName)) {
            if (schemaStream == null)
                throw new FileNotFoundException("Could not find schema file: " + schemaFileName);
            // File schemaFile = new
            // File("resources/xml/schema/JSaParSchema.xsd");

            // javax.xml.validation.Schema xmlSchema;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            factory.setIgnoringComments(true);
            factory.setCoalescing(true);
            factory.setNamespaceAware(true);
            factory.setValidating(true);
            factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            factory.setAttribute(JAXP_SCHEMA_SOURCE, schemaStream);

            // factory.setSchema(xmlSchema);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler() {
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
                public void warning(SAXParseException e)  {
                    // System.out.println("Warning while validating schema" +
                    // e);
                }
            });

            org.xml.sax.InputSource is = new org.xml.sax.InputSource(reader);
            org.w3c.dom.Document xmlDocument = builder.parse(is);

            Element xmlRoot = xmlDocument.getDocumentElement();
            // Element xmlRoot = (Element) xmlDocument.getFirstChild();

            Element xmlSchema = getChild(xmlRoot, ELEMENT_CSV_SCHEMA);
            if (null != xmlSchema)
                return buildCsvSchema(xmlSchema);

            xmlSchema = getChild(xmlRoot, ELEMENT_FIXED_WIDTH_SCHEMA);
            if (null != xmlSchema)
                return buildFixedWidthSchema(xmlSchema);

            throw new SAXException(
                    "Failed to find specific schema XML element. Expected one of " + ELEMENT_CSV_SCHEMA + " or "
                            + ELEMENT_FIXED_WIDTH_SCHEMA);
        }
        catch(ParserConfigurationException|SAXException e){
            throw new SchemaException("Failed to load schema from xml ", e);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load schema from xml", e);
        }
    }

    /**
     * @param xmlSchema The xml element to parse schema from
     * @return A parsed schema.
     * @throws SchemaException If there are errors in the schema configuration.
     */
    private Schema buildFixedWidthSchema(Element xmlSchema) throws SchemaException {
        FixedWidthSchema schema = new FixedWidthSchema();

        assignFixedWidthSchema(schema, xmlSchema);
        schema.addFillerCellsToReachLineMinLength();
        return schema;
    }

    /**
     * Assigns additional fixed width schema
     * @param xmlSchema The schema xml element.
     * @throws SchemaException If there are errors in the schema configuration.
     */
    private void assignFixedWidthSchema(FixedWidthSchema schema, Element xmlSchema) throws SchemaException {

        assignSchemaBase(schema, xmlSchema);

        NodeList nodes = xmlSchema.getElementsByTagNameNS(JSAPAR_XML_SCHEMA, ELEMENT_SCHEMA_LINE);
        for (int i = 0; i < nodes.getLength(); i++) {
            org.w3c.dom.Node child = nodes.item(i);
            if (child instanceof Element)
                schema.addSchemaLine(buildFixedWidthSchemaLine((Element) child, schema.getLocale()));
        }
    }


    /**
     * Builds the lines of a file schema from an xml input.
     * 
     * @param xmlSchemaLine The xml schema line element
     * @param locale The default locale of the schema.
     * @return A newly created fixed with line schema.
     * @throws SchemaException If there are errors in the schema.
     */
    private FixedWidthSchemaLine buildFixedWidthSchemaLine(Element xmlSchemaLine, Locale locale) throws SchemaException {
        FixedWidthSchemaLine schemaLine = new FixedWidthSchemaLine();

        assignSchemaLineBase(schemaLine, xmlSchemaLine);

        attributeValue(xmlSchemaLine, ATTRIB_FW_SCHEMA_PAD_CHARACTER)
                .map(s->s.charAt(0))
                .ifPresent(schemaLine::setPadCharacter);

        attributeValue(xmlSchemaLine, ATTRIB_FW_SCHEMA_MIN_LENGTH)
                .filter(s->!s.isEmpty())
                .map(Integer::valueOf)
                .ifPresent(schemaLine::setMinLength);

        NodeList nodes = xmlSchemaLine.getElementsByTagNameNS(JSAPAR_XML_SCHEMA, ELEMENT_SCHEMA_LINE_CELL);
        for (int i = 0; i < nodes.getLength(); i++) {
            org.w3c.dom.Node child = nodes.item(i);
            if (child instanceof Element)
                schemaLine.addSchemaCell(buildFixedWidthSchemaCell((Element) child, locale, schemaLine));
        }
        return schemaLine;
    }

    /**
     * Builds the cell part of a file schema from an xml input
     * 
     * @param xmlSchemaCell The cell schema xml element
     * @param locale        The default locale
     * @param schemaLine    The schema line to add schema cell to
     * @return A newly created fixed width cell schema.
     * @throws SchemaException If there is an error in the schema.
     */
    private FixedWidthSchemaCell buildFixedWidthSchemaCell(Element xmlSchemaCell, Locale locale, FixedWidthSchemaLine schemaLine) throws SchemaException {

        int nLength = getIntValue(getMandatoryAttribute(xmlSchemaCell, ATTRIB_FW_SCHEMA_CELL_LENGTH));
        String sName = getAttributeValue(xmlSchemaCell, ATTRIB_SCHEMA_CELL_NAME);

        FixedWidthSchemaCell cell = new FixedWidthSchemaCell(sName, nLength);
        assignSchemaCellBase(cell, xmlSchemaCell, locale);

        Node xmlAlignment = xmlSchemaCell.getAttributeNode(ATTRIB_FW_SCHEMA_CELL_ALIGNMENT);
        if(xmlAlignment != null) {
            switch (getStringValue(xmlAlignment)) {
            case "left":
                cell.setAlignment(FixedWidthSchemaCell.Alignment.LEFT);
                break;
            case "center":
                cell.setAlignment(FixedWidthSchemaCell.Alignment.CENTER);
                break;
            case "right":
                cell.setAlignment(FixedWidthSchemaCell.Alignment.RIGHT);
                break;
            default:
                throw new SchemaException(
                        "Invalid value for attribute: " + ATTRIB_FW_SCHEMA_CELL_ALIGNMENT + "=" + getStringValue(
                                xmlAlignment));
            }
        }
        else{
            cell.setDefaultAlignmentForType();
        }

        String sFillChar = getAttributeValue(xmlSchemaCell, ATTRIB_FW_SCHEMA_PAD_CHARACTER);
        if (sFillChar != null)
            cell.setPadCharacter(sFillChar.charAt(0));
        else
            cell.setPadCharacter(schemaLine.getPadCharacter());

        return cell;
    }

    /**
     * Builds a CSV file schema object.
     * 
     * @param xmlSchema A schema xml element.
     * @return A newly created CSV schema.
     * @throws SchemaException If there is an error in the schema.
     */
    private Schema buildCsvSchema(Element xmlSchema) throws SchemaException {
        CsvSchema schema = new CsvSchema();
        assignCsvSchema(schema, xmlSchema);
        return schema;
    }

    /**
     * Assigns values to a CSV schema object.
     * 
     * @param schema    The schema to assign values to
     * @param xmlSchema A schema xml element
     * @throws SchemaException If there is an error in the schema.
     */
    private void assignCsvSchema(CsvSchema schema, Element xmlSchema) throws SchemaException {

        assignSchemaBase(schema, xmlSchema);

        NodeList nodes = xmlSchema.getElementsByTagNameNS(JSAPAR_XML_SCHEMA, ELEMENT_SCHEMA_LINE);
        for (int i = 0; i < nodes.getLength(); i++) {
            org.w3c.dom.Node child = nodes.item(i);
            if (child instanceof Element)
                schema.addSchemaLine(buildCsvSchemaLine((Element) child, schema.getLocale()));
        }
    }


    /**
     * Creates a CSV line schema
     * @param xmlSchemaLine    The line schema xml element
     * @param locale The schema default locale.
     * @return A newly created csv schema line.
     * @throws SchemaException  When there is an error in the schema
     */
    private CsvSchemaLine buildCsvSchemaLine(Element xmlSchemaLine, Locale locale) throws SchemaException {

        CsvSchemaLine schemaLine = new CsvSchemaLine();
        assignSchemaLineBase(schemaLine, xmlSchemaLine);

        attributeValue(xmlSchemaLine, ATTRIB_CSV_SCHEMA_CELL_SEPARATOR)
                .map(Xml2SchemaBuilder::replaceEscapes2Java)
                .ifPresent(schemaLine::setCellSeparator);

        Node xmlFirstLineAsSchema = xmlSchemaLine.getAttributeNode(ATTRIB_CSV_SCHEMA_LINE_FIRSTLINEASSCHEMA);
        if (xmlFirstLineAsSchema != null)
            schemaLine.setFirstLineAsSchema(getBooleanValue(xmlFirstLineAsSchema));

        String sQuoteChar = getAttributeValue(xmlSchemaLine, ATTRIB_CSV_QUOTE_CHAR);
        if (sQuoteChar != null)
            schemaLine.setQuoteChar(sQuoteChar.charAt(0));

        QuoteBehavior quoteBehavior = attributeValue(xmlSchemaLine, ATTRIB_CSV_QUOTE_BEHAVIOR)
                .map(QuoteBehavior::valueOf)
                .orElse(QuoteBehavior.AUTOMATIC);

        NodeList nodes = xmlSchemaLine.getElementsByTagNameNS(JSAPAR_XML_SCHEMA, ELEMENT_SCHEMA_LINE_CELL);
        for (int i = 0; i < nodes.getLength(); i++) {
            org.w3c.dom.Node child = nodes.item(i);
            if (child instanceof Element)
                schemaLine.addSchemaCell(buildCsvSchemaCell((Element) child, locale, quoteBehavior));
        }

        return schemaLine;
    }

    /**
     * Creates a CSV cell schema
     * @param xmlSchemaCell A cell schema xml element.
     * @param quoteBehavior The default quote behavior for the line
     * @return A newly created CSV cell schema.
     * @throws SchemaException  When there is an error in the schema
     */
    private CsvSchemaCell buildCsvSchemaCell(Element xmlSchemaCell, Locale locale, QuoteBehavior quoteBehavior) throws SchemaException {

        String sName = getAttributeValue(xmlSchemaCell, ATTRIB_SCHEMA_CELL_NAME);
        CsvSchemaCell cell = new CsvSchemaCell(sName);
        Node xmlMaxLength = xmlSchemaCell.getAttributeNode(ATTRIB_SCHEMA_CELL_MAX_LENGTH);
        if (xmlMaxLength != null)
            cell.setMaxLength(getIntValue(xmlMaxLength));
        cell.setQuoteBehavior( attributeValue(xmlSchemaCell, ATTRIB_CSV_QUOTE_BEHAVIOR)
                .map(QuoteBehavior::valueOf)
                .orElse(quoteBehavior));

        assignSchemaCellBase(cell, xmlSchemaCell, locale);
        return cell;

    }

    /**
     * Assign common parts for base class.
     * 
     * @param schema The schema to assign to
     * @param xmlSchema The xml element to parse
     * @throws SchemaException  When there is an error in the schema
     */
    private void assignSchemaBase(Schema schema, Element xmlSchema) throws SchemaException {
        attributeValue(xmlSchema, ATTRIB_SCHEMA_LINESEPARATOR)
                .map(Xml2SchemaBuilder::replaceEscapes2Java)
                .ifPresent(schema::setLineSeparator);

        Element xmlLocale = getChild(xmlSchema, ELEMENT_LOCALE);
        if (xmlLocale != null)
            schema.setLocale(buildLocale(xmlLocale));

    }

    /**
     * Replaces escaped string value of \n, \r, \t and \f with their ascii control code values.
     * @param sToReplace The string to replace escaped strings within.
     * @return The string with all escaped values replaced with control code values.
     */
    private static String replaceEscapes2Java(String sToReplace) {
        //   Since it is a regex we need 4 \
        sToReplace = sToReplace.replaceAll("\\\\r", "\r");
        sToReplace = sToReplace.replaceAll("\\\\n", "\n");
        sToReplace = sToReplace.replaceAll("\\\\t", "\t");
        sToReplace = sToReplace.replaceAll("\\\\f", "\f");
        return sToReplace;
    }

    /**
     * Assign common pars for base class.
     * 
     * @param line The line to assign to
     * @param xmlSchemaLine The xml element to parse
     */
    private void assignSchemaLineBase(SchemaLine line, Element xmlSchemaLine)  {
        Node xmlOccurs = xmlSchemaLine.getAttributeNode(ATTRIB_SCHEMA_LINE_OCCURS);
        if (xmlOccurs != null) {
            if (getStringValue(xmlOccurs).equals("*"))
                line.setOccursInfinitely();
            else
                line.setOccurs(getIntValue(xmlOccurs));
        }

        Node xmlLineType = xmlSchemaLine.getAttributeNode(ATTRIB_SCHEMA_LINE_LINETYPE);
        if (xmlLineType != null)
            line.setLineType(getStringValue(xmlLineType));

        Node xmlIgnoreRead = xmlSchemaLine.getAttributeNode(ATTRIB_SCHEMA_IGNOREREAD);
        if (xmlIgnoreRead != null)
            line.setIgnoreRead(getBooleanValue(xmlIgnoreRead));

        Node xmlIgnoreWrite = xmlSchemaLine.getAttributeNode(ATTRIB_SCHEMA_IGNOREWRITE);
        if (xmlIgnoreWrite != null)
            line.setIgnoreWrite(getBooleanValue(xmlIgnoreWrite));


    }

    /**
     * Assign common parts for base class.
     * 
     * @param cell The cell to assign to
     * @param xmlSchemaCell The xml element to parse
     * @param locale The default locale
     * @throws SchemaException  When there is an error in the schema
     */
    private void assignSchemaCellBase(SchemaCell cell, Element xmlSchemaCell, Locale locale) throws SchemaException {
        try {
            Element xmlLocale = getChild(xmlSchemaCell, ELEMENT_LOCALE);
            if (xmlLocale != null)
                locale = buildLocale(xmlLocale);
            cell.setLocale(locale);

            Node xmlIgnoreRead = xmlSchemaCell.getAttributeNode(ATTRIB_SCHEMA_IGNOREREAD);
            if (xmlIgnoreRead != null)
                cell.setIgnoreRead(getBooleanValue(xmlIgnoreRead));

            Node xmlIgnoreWrite = xmlSchemaCell.getAttributeNode(ATTRIB_SCHEMA_IGNOREWRITE);
            if (xmlIgnoreWrite != null)
                cell.setIgnoreWrite(getBooleanValue(xmlIgnoreWrite));

            Node xmlMandatory = xmlSchemaCell.getAttributeNode(ATTRIB_SCHEMA_CELL_MANDATORY);
            if (xmlMandatory != null)
                cell.setMandatory(getBooleanValue(xmlMandatory));

            Element xmlLineCondition = getChild(xmlSchemaCell, ELEMENT_LINE_CONDITION);
            if (xmlLineCondition != null)
                cell.setLineCondition(extractCellValueCondition(xmlLineCondition));

            Element xmlEmptyCondition = getChild(xmlSchemaCell, ELEMENT_EMPTY_CONDITION);
            if (xmlEmptyCondition != null)
                cell.setEmptyCondition(extractCellValueCondition(xmlEmptyCondition));

            Element xmlFormat = getChild(xmlSchemaCell, ELEMENT_FORMAT);
            if (xmlFormat != null)
                assignCellFormat(cell, xmlFormat);

            Element xmlRange = getChild(xmlSchemaCell, ELEMENT_RANGE);
            if (xmlRange != null) {
                Node minValue = xmlRange.getAttributeNode(ATTRIB_SCHEMA_CELL_MIN);
                if (minValue != null)
                    cell.setMinValue(getStringValue(minValue));
                Node maxValue = xmlRange.getAttributeNode(ATTRIB_SCHEMA_CELL_MAX);
                if (maxValue != null)
                    cell.setMaxValue(getStringValue(maxValue));
            }

            Node xmlDefault = xmlSchemaCell.getAttributeNode(ATTRIB_SCHEMA_CELL_DEFAULT_VALUE);
            if (xmlDefault != null)
                cell.setDefaultValue(getStringValue(xmlDefault));

        } catch (ParseException e) {
            throw new SchemaException("Failed to parse value within xml schema. ", e);
        }

    }

    private MatchingCellValueCondition extractCellValueCondition(Element xmlCellValueCondition) throws SchemaException {
        Element xmlMatch = getChild(xmlCellValueCondition, ELEMENT_MATCH);
        if(xmlMatch != null){
            String pattern = getAttributeValue(xmlMatch, ATTRIB_PATTERN);
            return new MatchingCellValueCondition(pattern);
        }
        throw new SchemaException("Expected line condition is missing");
    }

    /**
     * Adds formatting to the cell.
     * 
     * @param cell The cell to assign to
     * @param xmlFormat The xml element to parse
     * @throws SchemaException  When there is an error in the schema
     */
    private void assignCellFormat(SchemaCell cell, Element xmlFormat) throws SchemaException {
        String sType = getAttributeValue(xmlFormat, "type");
        String sPattern = getAttributeValue(xmlFormat, "pattern");

        cell.setCellFormat(makeCellType(sType), sPattern);
    }

    /**
     * Transforms from xml schema type to CellType enum.
     * 
     * @param sType
     *            The xml representation of the type.
     * @return The CellType value
     * @throws SchemaException  When there is an error in the schema
     */
    private CellType makeCellType(String sType) throws SchemaException {
        switch (sType) {
            case "string":
                return CellType.STRING;
            case "integer":
                return CellType.INTEGER;
            case "date":
                return CellType.DATE;
            case "local_date":
                return CellType.LOCAL_DATE;
            case "local_date_time":
                return CellType.LOCAL_DATE_TIME;
            case "local_time":
                return CellType.LOCAL_TIME;
            case "zoned_date_time":
                return CellType.ZONED_DATE_TIME;
            case "float":
                return CellType.FLOAT;
            case "decimal":
                return CellType.DECIMAL;
            case "boolean":
                return CellType.BOOLEAN;
            case "character":
                return CellType.CHARACTER;
            default:
                throw new SchemaException("Unknown cell format type: " + sType);
        }

    }

    /**
     * Builds a java.util.Locale based on a Locale element.
     * 
     * @param xmlLocale The xml element to parse
     * @return The java Locale object.
     * @throws SchemaException  When there is an error in the schema
     */
    private Locale buildLocale(Element xmlLocale) throws SchemaException {
        String sLanguage = getMandatoryAttribute(xmlLocale, ATTRIB_LOCALE_LANGUAGE).getValue();
        String sCountry = getAttributeValue(xmlLocale, ATTRIB_LOCALE_COUNTRY);
        if (sCountry != null)
            return new Locale(sLanguage, sCountry);
        else
            return new Locale(sLanguage);
    }

    /**
     * Gets a mandatory xml attribute from a xml element. If the element does not exist, a
     * SchemaException is thrown.
     * 
     * @param xmlElement The xml element to parse
     * @param sAttributeName The name of the attribute
     * @return The xml attribute.
     * @throws SchemaException  When there is an error in the schema
     */
    private static Attr getMandatoryAttribute(Element xmlElement, String sAttributeName) throws SchemaException {
        Node xmlAttribute = xmlElement.getAttributeNode(sAttributeName);
        if (xmlAttribute == null)
            throw new SchemaException("Missing mandatory attribute: " + sAttributeName + " of element " + xmlElement);
        return (Attr) xmlAttribute;
    }
    
    /**
     * Utility function for loading a schema from an xml file.
     * @param file The file to load schema from
     * @return A newly created schema from the xml file.
     * @throws SchemaException  When there is an error in the schema
     * @throws UncheckedIOException      When there is an error reading from input
     */
    public static Schema loadSchemaFromXmlFile(File file)
            throws UncheckedIOException, SchemaException{
        return loadSchemaFromXmlFile(file, Charset.defaultCharset().name());
    }
    
    /**
     * Utility function for loading a schema from an xml file.
     * @param file      The file to load schema from
     * @param encoding The character encoding to use while reading file.
     * @return A newly created schema from the xml file.
     * @throws SchemaException  When there is an error in the schema
     * @throws UncheckedIOException      When there is an error reading from input
     */
    @SuppressWarnings("WeakerAccess")
    public static Schema loadSchemaFromXmlFile(File file, String encoding)
            throws UncheckedIOException, SchemaException {

        try (InputStream is = new FileInputStream(file);
                Reader schemaReader = new InputStreamReader(is, encoding)) {
            Xml2SchemaBuilder builder = new Xml2SchemaBuilder();
            return builder.build(schemaReader);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load schema from file", e);
        }
    }

    /**
     * Loads a schema from specified resource using default character encoding.
     * 
     * @param resourceBaseClass
     *            A class that specifies the base for the relative location of the resource. If this parameter is null,
     *            the resource name has to specify the resource with an absolute path.
     * @param resourceName
     *            The name of the resource to load.
     * @return A newly created schema from the supplied xml resource.
     * @throws SchemaException  When there is an error in the schema
     * @throws UncheckedIOException      When there is an error reading from input
     */
    @SuppressWarnings("unused")
    public static Schema loadSchemaFromXmlResource(Class<?> resourceBaseClass, String resourceName)
            throws UncheckedIOException, SchemaException{
        return loadSchemaFromXmlResource(resourceBaseClass, resourceName, Charset.defaultCharset().name());
    }    
    
    /**
     * Loads a schema from specified resource using supplied character encoding.
     * 
     * @param resourceBaseClass
     *            A class that specifies the base for the relative location of the resource. If this parameter is null,
     *            the resource name has to specify the resource with an absolute path.
     * @param resourceName
     *            The name of the resource to load.
     * @param encoding
     *            The character encoding to use while reading resource.
     * @return A newly created schema from the supplied xml resource.
     * @throws SchemaException  When there is an error in the schema
     * @throws UncheckedIOException      When there is an error reading from input
     */
    @SuppressWarnings("WeakerAccess")
    public static Schema loadSchemaFromXmlResource(Class<?> resourceBaseClass, String resourceName, String encoding)
            throws UncheckedIOException, SchemaException{
        if (resourceBaseClass == null)
            resourceBaseClass = Xml2SchemaBuilder.class;
        try (InputStream is = resourceBaseClass.getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new IOException(
                        "Failed to load resource [" + resourceName + "] from class " + resourceBaseClass.getName());
            }
            Xml2SchemaBuilder schemaBuilder = new Xml2SchemaBuilder();
            return schemaBuilder.build(new InputStreamReader(is, encoding));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load schema from xml resource", e);
        }
    }

}
