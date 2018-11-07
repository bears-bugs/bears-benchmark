package org.jsapar.schema;

import org.jsapar.model.CellType;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;

public class Xml2SchemaBuilderTest {

    /**
     * Test method for {@link org.jsapar.schema.Xml2SchemaBuilder#build(java.io.Reader)} .
     * 
     */
    @Test
    public final void testBuild_FixedWidth()
            throws SchemaException, IOException, ParserConfigurationException, SAXException {

        String sXmlSchema = "<?xml version='1.0' encoding='UTF-8'?>"
                + "<schema  xmlns='http://jsapar.tigris.org/JSaParSchema/2.0' >"
                + "<fixedwidthschema lineseparator='\\r\\n'>" 
                + "<line occurs='*' linetype='Person' minlength='240'>"
                + "<cell name='First name' length='5'/>" + "<cell name='Last name' length='8'/>"
                + "<cell name='Shoe size' length='8' alignment='right'><format type='integer' pattern='00000000'/><emptycondition><match pattern='NULL'/></emptycondition></cell>"
                + "</line></fixedwidthschema></schema>";

        Xml2SchemaBuilder builder = new Xml2SchemaBuilder();
        java.io.Reader reader = new java.io.StringReader(sXmlSchema);
        Schema schema = builder.build(reader);
        FixedWidthSchema fwSchema = (FixedWidthSchema) schema;

        assertEquals("\r\n", fwSchema.getLineSeparator());

        FixedWidthSchemaLine firstLineSchema = fwSchema.getSchemaLines().iterator().next();
        assertEquals(240, firstLineSchema.getMinLength());
        FixedWidthSchemaCell firstNameSchema = firstLineSchema.getSchemaCells().get(0);
        assertEquals("First name", firstNameSchema.getName());
        FixedWidthSchemaCell lastNameSchema = firstLineSchema.getSchemaCells().get(1);
        assertEquals("Last name", lastNameSchema.getName());

        assertEquals(5, firstNameSchema.getLength());
        assertEquals(8, lastNameSchema.getLength());
        FixedWidthSchemaCell shoeSizeSchema = firstLineSchema.getSchemaCells().get(2);
        assertTrue(shoeSizeSchema.getEmptyCondition().satisfies("NULL"));

        assertEquals(CellType.INTEGER, shoeSizeSchema
                .getCellFormat().getCellType());

        SchemaLine schemaLine = fwSchema.getSchemaLine("Person").orElse(null);
        Assert.assertNotNull(schemaLine);
        FixedWidthSchemaCell schemaCell = ((FixedWidthSchemaLine)schemaLine).getSchemaCells().get(2);
        assertEquals( FixedWidthSchemaCell.Alignment.RIGHT, schemaCell.getAlignment() );
        assertEquals( "00000000", schemaCell.getCellFormat().getPattern() );
    }

    @Test
    public final void testBuild_Csv() throws SchemaException, IOException, ParserConfigurationException, SAXException {

        String sXmlSchema = "<?xml version='1.0' encoding='UTF-8'?>"
                + "<schema  xmlns='http://jsapar.tigris.org/JSaParSchema/2.0' >"
                + "<csvschema><line linetype='P' occurs='4'>"
                + "<cell name='First name'/>"
                + "<cell name='Last name'/>"
                + "</line></csvschema></schema>";

        Xml2SchemaBuilder builder = new Xml2SchemaBuilder();
        java.io.Reader reader = new java.io.StringReader(sXmlSchema);
        Schema schema = builder.build(reader);
        CsvSchema csvSchema = (CsvSchema) schema;

        Collection<CsvSchemaCell> schemaCells = csvSchema.getSchemaLine("P").orElseThrow(AssertionError::new)
                .getSchemaCells();
        Iterator<CsvSchemaCell> it = schemaCells.iterator();
        assertEquals("First name", it.next().getName());
        assertEquals("Last name", it.next().getName());
    }

    @Test
    public final void testBuild_CsvControlCell()
            throws SchemaException, IOException, ParserConfigurationException, SAXException {

        String sXmlSchema = "<?xml version='1.0' encoding='UTF-8'?>"
                + "<schema  xmlns='http://jsapar.tigris.org/JSaParSchema/2.0' >"
                + "<csvschema><line occurs='4'>"
                + "<cell name='type'><linecondition><match pattern='P'/></linecondition></cell>"
                + "<cell name='First name'/>"
                + "<cell name='Last name'/>"
                + "</line></csvschema></schema>";

        Xml2SchemaBuilder builder = new Xml2SchemaBuilder();
        java.io.Reader reader = new java.io.StringReader(sXmlSchema);
        Schema schema = builder.build(reader);
        CsvSchema csvSchema = (CsvSchema) schema;

        Collection<CsvSchemaCell> schemaCells = csvSchema.getSchemaLines().iterator().next().getSchemaCells();
        Iterator<CsvSchemaCell> it = schemaCells.iterator();
        CsvSchemaCell controlCell = it.next();
        assertTrue(controlCell.getLineCondition().satisfies("P"));
        assertFalse(controlCell.getLineCondition().satisfies("X"));
        assertEquals("type", controlCell.getName());
        assertEquals("First name", it.next().getName());
        assertEquals("Last name", it.next().getName());

    }

    @Test
    public final void testBuild_Csv_firstlineasschema()
            throws SchemaException, IOException, ParserConfigurationException, SAXException {

        String sXmlSchema = "<?xml version='1.0' encoding='UTF-8'?>"
                + "<schema  xmlns='http://jsapar.tigris.org/JSaParSchema/2.0' >"
                + "<csvschema><line linetype='P' occurs='4' firstlineasschema='true' >"
                + "</line></csvschema></schema>";

        Xml2SchemaBuilder builder = new Xml2SchemaBuilder();
        java.io.Reader reader = new java.io.StringReader(sXmlSchema);
        Schema schema = builder.build(reader);
        CsvSchema csvSchema = (CsvSchema) schema;

        assertEquals(true, csvSchema.getSchemaLines().iterator().next().isFirstLineAsSchema());

    }



    @Test(expected = SchemaException.class)
    public final void testBuild_Csv_firstlineasschema_error()
            throws SchemaException, IOException, ParserConfigurationException, SAXException {

        // "yes" is not a valid boolean value.
        String sXmlSchema = "<?xml version='1.0' encoding='UTF-8'?>\n"
                + "<schema  xmlns='http://jsapar.tigris.org/JSaParSchema/2.0' >\n"
                + "<csvschema><line occurs='4' firstlineasschema='yes' >\n" + "</line></csvschema></schema>";

        Xml2SchemaBuilder builder = new Xml2SchemaBuilder();
        java.io.Reader reader = new java.io.StringReader(sXmlSchema);
        @SuppressWarnings("unused")
        Schema schema = builder.build(reader);
    }

}
