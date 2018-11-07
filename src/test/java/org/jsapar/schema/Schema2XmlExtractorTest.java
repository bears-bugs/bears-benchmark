/**
 * 
 */
package org.jsapar.schema;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertNotNull;

/**
 * @author stejon0
 *
 */
public class Schema2XmlExtractorTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }


    /**
     * Test method for {@link org.jsapar.schema.Schema2XmlExtractor#extractXml(java.io.Writer, org.jsapar.schema.Schema)}.
     * @throws SchemaException 
     */
    @Test
    public void testExtractXml_FixedWidth() throws SchemaException {
        StringWriter writer = new StringWriter();
        FixedWidthSchema schema = new FixedWidthSchema();
        FixedWidthSchemaLine schemaLine = new FixedWidthSchemaLine(2);
        schemaLine.setMinLength(240);
        schema.setLineSeparator("");

        schemaLine.addSchemaCell(new FixedWidthSchemaCell("First name", 5));
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("Last name", 8));
        schema.addSchemaLine(schemaLine);
        
        Schema2XmlExtractor extractor = new Schema2XmlExtractor();
        extractor.extractXml(writer, schema);
        
        String sXml = writer.toString();
//        System.out.println(sXml);
        
        assertNotNull(sXml);
        // TODO Add more accurate tests.
    }


    /**
     * Test method for {@link org.jsapar.schema.Schema2XmlExtractor#extractXml(java.io.Writer, org.jsapar.schema.Schema)}.
     * @throws SchemaException 
     */
    @Test
    public void testExtractXml_Csv() throws SchemaException {
        StringWriter writer = new StringWriter();
        CsvSchema schema = new CsvSchema();
        CsvSchemaLine schemaLine = new CsvSchemaLine(2);

        schemaLine.addSchemaCell(new CsvSchemaCell("First name"));
        schemaLine.addSchemaCell(new CsvSchemaCell("Last name"));
        schema.addSchemaLine(schemaLine);
        
        Schema2XmlExtractor extractor = new Schema2XmlExtractor();
        extractor.extractXml(writer, schema);
        
        String sXml = writer.toString();
//        System.out.println(sXml);
        
        assertNotNull(sXml);
        // TODO Add more accurate tests.
    }


    
}
