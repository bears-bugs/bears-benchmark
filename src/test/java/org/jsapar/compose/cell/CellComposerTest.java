package org.jsapar.compose.cell;

import org.jsapar.model.*;
import org.jsapar.parse.cell.DateCellFactory;
import org.jsapar.schema.SchemaCell;
import org.jsapar.schema.SchemaException;
import org.junit.Test;

import java.text.ParseException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CellComposerTest {

    /**
     * To be able to have a specific SchemaCell to test.
     *
     * @author stejon0
     *
     */
    private class TestSchemaCell extends SchemaCell {


        TestSchemaCell(String name) {
            super(name);
        }

        TestSchemaCell(String name, CellType type, String pattern, Locale locale) {
            super(name, type, pattern, locale);
        }
    }

    
    /**
     * Test method for .
     *
     * @throws java.text.ParseException
     * @throws SchemaException
     */
    @Test
    public void testFormat_emptyString_DefaultValue() throws java.text.ParseException, SchemaException {
        TestSchemaCell schemaCell = new TestSchemaCell("test");
        schemaCell.setDefaultValue("TheDefault");

        Cell cell = new StringCell("Test", "");
        CellComposer composer = new CellComposer();
        assertEquals("TheDefault", composer.format(cell, schemaCell));
    }

    /**
     * Test method for .
     *
     *
     * @throws java.text.ParseException
     * @throws SchemaException
     */
    @Test
    public void testFormat_empty_DefaultValue() throws java.text.ParseException, SchemaException {
        TestSchemaCell schemaCell = new TestSchemaCell("test");
        schemaCell.setDefaultValue("TheDefault");

        Cell cell = new EmptyCell("Test", CellType.STRING);
        CellComposer composer = new CellComposer();
        assertEquals("TheDefault", composer.format(cell, schemaCell));
    }

    /**
     * Test method for .
     *
     *
     * @throws java.text.ParseException
     * @throws SchemaException
     */
    @Test
    public void testFormat_null_DefaultValue() throws java.text.ParseException, SchemaException {
        TestSchemaCell schemaCell = new TestSchemaCell("test");
        schemaCell.setDefaultValue("TheDefault");

        CellComposer composer = new CellComposer();
        assertEquals("TheDefault", composer.format(null, schemaCell));
    }

    /**
     * Test method for .
     *
     * 
     */
    @Test
    public void testFormat_empty_no_default()  {
        TestSchemaCell schemaCell = new TestSchemaCell("test");

        Cell cell = new EmptyCell("Test", CellType.STRING);
        CellComposer composer = new CellComposer();
        assertEquals("", composer.format(cell, schemaCell));
    }


    /**
     * Test method for .
     *
     * 
     */
    @Test
    public void testFormat_null_no_default()  {
        TestSchemaCell schemaCell = new TestSchemaCell("test");

        CellComposer composer = new CellComposer();
        assertEquals("", composer.format(null, schemaCell));
    }

    /**
     * Test method for .
     *
     * 
     * @throws SchemaException
     * @throws java.text.ParseException
     */
    @Test
    public void testFormat_DefaultValue_float() throws SchemaException, java.text.ParseException {
        TestSchemaCell schemaCell = new TestSchemaCell("test");
        schemaCell.setLocale( new Locale("sv","SE"));
        schemaCell.setCellFormat(CellType.FLOAT, "#.00");
        schemaCell.setDefaultValue("123456,78901");

        CellComposer composer = new CellComposer();
        String value = composer.format(new EmptyCell("test", CellType.FLOAT), schemaCell);
        assertEquals("123456,78901", value);
    }

    /**
     * Test method for .
     *
     * 
     * @throws SchemaException
     */
    @Test
    public void testFormat_empty_integer() throws SchemaException {
        TestSchemaCell schemaCell = new TestSchemaCell("test");
        schemaCell.setCellFormat(CellType.INTEGER);

        CellComposer composer = new CellComposer();
        String value = composer.format(new EmptyCell("test", CellType.INTEGER), schemaCell);
        assertEquals("", value);
    }

    @Test
    public final void testFormat_date() throws ParseException {
        TestSchemaCell schemaCell = new TestSchemaCell("test");
        schemaCell.setCellFormat(CellType.DATE, "yyyy-MM-dd HH:mm");
        DateCellFactory cellFactory = new DateCellFactory();

        DateCell cell = (DateCell) cellFactory.makeCell("Name", "2007-10-01 14:13", schemaCell.getCellFormat().getFormat());

        CellComposer composer = new CellComposer();
        String value = composer.format(cell, schemaCell);


        assertEquals("2007-10-01 14:13", value);
    }


    /**
     * Test method for .
     *
     * 
     * @throws SchemaException
     */
    @Test
    public void testFormat() throws SchemaException {
        TestSchemaCell schemaCell = new TestSchemaCell("test");

        CellComposer composer = new CellComposer();
        String value = composer.format(new StringCell("test","A"), schemaCell);
        assertEquals("A", value);
    }

    /**
     * Test method for .
     *
     * 
     * @throws SchemaException
     */
    @Test
    public void testFormat_Regexp() throws SchemaException {
        TestSchemaCell schemaCell = new TestSchemaCell("test", CellType.STRING, "A|B", new Locale("sv","SE"));

        CellComposer composer = new CellComposer();
        String value = composer.format(new StringCell("test","A"), schemaCell);
        assertEquals("A", value);
    }

    /**
     * Test method for .
     *
     * 
     * @throws SchemaException
     */
    @Test(expected=IllegalArgumentException.class)
    public void testFormat_Regexp_fail() throws SchemaException {
        TestSchemaCell schemaCell = new TestSchemaCell("test", CellType.STRING, "A|B", new Locale("sv","SE"));

        CellComposer composer = new CellComposer();
        composer.format(new StringCell("test","C"), schemaCell);
        fail("Should throw exception");
    }

}