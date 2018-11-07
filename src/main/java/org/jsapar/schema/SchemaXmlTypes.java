package org.jsapar.schema;

/**
 * Packet private constants to be used while reading and writing xml.
 */
interface SchemaXmlTypes {
    String ELEMENT_ROOT                             = "schema";
    String ELEMENT_CSV_SCHEMA                       = "csvschema";
    String ELEMENT_FIXED_WIDTH_SCHEMA               = "fixedwidthschema";
    String ELEMENT_LOCALE                           = "locale";
    String ELEMENT_FORMAT                           = "format";
    String ELEMENT_LINE_CONDITION                   = "linecondition";
    String ELEMENT_EMPTY_CONDITION                  = "emptycondition";
    String ELEMENT_MATCH                            = "match";
    String ATTRIB_FW_SCHEMA_PAD_CHARACTER           = "padcharacter";
    String ATTRIB_FW_SCHEMA_MIN_LENGTH              = "minlength";
    String ATTRIB_SCHEMA_CELL_NAME                  = "name";
    String ATTRIB_FW_SCHEMA_CELL_LENGTH             = "length";
    String ATTRIB_SCHEMA_LINE_LINETYPE              = "linetype";
    String ATTRIB_SCHEMA_LINE_OCCURS                = "occurs";
    String ATTRIB_CSV_SCHEMA_CELL_SEPARATOR         = "cellseparator";
    String ATTRIB_CSV_SCHEMA_LINE_FIRSTLINEASSCHEMA = "firstlineasschema";
    String ATTRIB_CSV_QUOTE_BEHAVIOR = "quotebehavior";
    String ATTRIB_LOCALE_LANGUAGE                   = "language";
    String ATTRIB_LOCALE_COUNTRY                    = "country";
    String ATTRIB_SCHEMA_LINESEPARATOR              = "lineseparator";
    String ATTRIB_SCHEMA_IGNOREREAD = "ignoreread";
    String ATTRIB_SCHEMA_IGNOREWRITE = "ignorewrite";
    String ATTRIB_FW_SCHEMA_CELL_ALIGNMENT          = "alignment";
    String ELEMENT_SCHEMA_LINE                      = "line";
    String ELEMENT_SCHEMA_LINE_CELL                 = "cell";
    String ATTRIB_SCHEMA_CELL_MANDATORY             = "mandatory";
    String ELEMENT_RANGE                            = "range";
    String ATTRIB_SCHEMA_CELL_MIN                   = "min";
    String ATTRIB_SCHEMA_CELL_MAX = "max";
    String ATTRIB_SCHEMA_CELL_MAX_LENGTH = "maxlength";
    String ATTRIB_CSV_QUOTE_CHAR = "quotechar";
    String ATTRIB_SCHEMA_CELL_DEFAULT_VALUE = "default";
    String ATTRIB_PATTERN = "pattern";

    String JSAPAR_XML_SCHEMA = "http://jsapar.tigris.org/JSaParSchema/2.0";

}
