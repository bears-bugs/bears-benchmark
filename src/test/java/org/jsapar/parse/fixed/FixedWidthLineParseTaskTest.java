package org.jsapar.parse.fixed;

import org.jsapar.error.ExceptionErrorEventListener;
import org.jsapar.error.JSaParException;
import org.jsapar.error.ValidationAction;
import org.jsapar.model.CellType;
import org.jsapar.model.Line;
import org.jsapar.model.LineUtils;
import org.jsapar.parse.CellParseException;
import org.jsapar.parse.LineParseException;
import org.jsapar.parse.text.TextParseConfig;
import org.jsapar.schema.FixedWidthSchemaCell;
import org.jsapar.schema.FixedWidthSchemaLine;
import org.jsapar.schema.SchemaException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

public class FixedWidthLineParseTaskTest {
    
    boolean foundError = false;

    @Before
    public void setUp() throws Exception {
        foundError = false;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testParse() throws IOException, JSaParException {
        String toParse = "JonasStenbergSpiselvagen 19141 59Huddinge";
        org.jsapar.schema.FixedWidthSchema schema = new org.jsapar.schema.FixedWidthSchema();
        FixedWidthSchemaLine schemaLine = new FixedWidthSchemaLine(1);
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("First name", 5));
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("Last name", 8));
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("Street address", 14));
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("Zip code", 6));
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("City", 8));
        schema.addSchemaLine(schemaLine);

        Reader reader = new StringReader(toParse);
        TextParseConfig config = new TextParseConfig();
        FixedWidthLineParser parser = new FixedWidthLineParser(schemaLine, config);
        Line line = parser.parse(reader, 1, new ExceptionErrorEventListener() );
        assertNotNull(line);
        assertEquals("Jonas", LineUtils.getStringCellValue(line, "First name"));
        assertEquals("Stenberg", LineUtils.getStringCellValue(line, "Last name"));
        assertEquals("Huddinge", LineUtils.getStringCellValue(line,"City"));
    }

    @Test
    public void testParse_defaultLast() throws IOException, JSaParException, java.text.ParseException, SchemaException {
        String toParse = "JonasStenberg";
        org.jsapar.schema.FixedWidthSchema schema = new org.jsapar.schema.FixedWidthSchema();
        FixedWidthSchemaLine schemaLine = new FixedWidthSchemaLine(1);
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("First name", 5));
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("Last name", 8));
        FixedWidthSchemaCell cityCell = new FixedWidthSchemaCell("City", 8);
        cityCell.setDefaultValue("Falun");
        schemaLine.addSchemaCell(cityCell);
        schema.addSchemaLine(schemaLine);

        Reader reader = new StringReader(toParse);
        TextParseConfig config = new TextParseConfig();
        FixedWidthLineParser parser = new FixedWidthLineParser(schemaLine, config);
        Line line = parser.parse(reader, 1, new ExceptionErrorEventListener() );
        assertNotNull(line);
        assertEquals("Jonas", LineUtils.getStringCellValue(line, "First name"));
        assertEquals("Stenberg", LineUtils.getStringCellValue(line, "Last name"));
        assertEquals("Falun", LineUtils.getStringCellValue(line,"City"));
    }

    @Test(expected = LineParseException.class)
    public void testParse_insufficient() throws IOException, JSaParException, java.text.ParseException {
        String toParse = "JonasStenberg";
        org.jsapar.schema.FixedWidthSchema schema = new org.jsapar.schema.FixedWidthSchema();
        FixedWidthSchemaLine schemaLine = new FixedWidthSchemaLine(1);
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("First name", 5));
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("Last name", 8));
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("City", 8));
        schema.addSchemaLine(schemaLine);

        Reader reader = new StringReader(toParse);
        TextParseConfig config = new TextParseConfig();
        config.setOnLineInsufficient(ValidationAction.EXCEPTION);
        FixedWidthLineParser parser = new FixedWidthLineParser(schemaLine, config);
        parser.parse(reader, 1, new ExceptionErrorEventListener() );
        fail("Exception is expected");
    }

    @Test
    public void testParse_default_and_mandatory()
            throws IOException, JSaParException, java.text.ParseException, SchemaException {
        String toParse = "JonasStenberg";
        org.jsapar.schema.FixedWidthSchema schema = new org.jsapar.schema.FixedWidthSchema();
        FixedWidthSchemaLine schemaLine = new FixedWidthSchemaLine(1);
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("First name", 5));
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("Last name", 8));
        FixedWidthSchemaCell cityCell = new FixedWidthSchemaCell("City", 8);
        cityCell.setDefaultValue("Falun");
        cityCell.setMandatory(true);
        schemaLine.addSchemaCell(cityCell);
        schema.addSchemaLine(schemaLine);

        Reader reader = new StringReader(toParse);
        TextParseConfig config = new TextParseConfig();
        FixedWidthLineParser parser = new FixedWidthLineParser(schemaLine, config);
        Line line = parser.parse(reader, 1, event -> {
            CellParseException e = (CellParseException) event.getError();
            assertEquals("City", e.getCellName());
            foundError=true;
        });

        assertNotNull(line);
        assertEquals(true, foundError);
    }

    @Test(expected = CellParseException.class)
    public void testParse_parseError() throws IOException, JSaParException {
        String toParse = "JonasStenbergFortyone";
        org.jsapar.schema.FixedWidthSchema schema = new org.jsapar.schema.FixedWidthSchema();
        FixedWidthSchemaLine schemaLine = new FixedWidthSchemaLine(1);
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("First name", 5));
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("Last name", 8));

        FixedWidthSchemaCell shoeSizeSchema = new FixedWidthSchemaCell("Shoe size", 8);
        shoeSizeSchema.setCellFormat(CellType.INTEGER);
        schemaLine.addSchemaCell(shoeSizeSchema);

        schema.addSchemaLine(schemaLine);

        Reader reader = new StringReader(toParse);
        TextParseConfig config = new TextParseConfig();
        FixedWidthLineParser parser = new FixedWidthLineParser(schemaLine, config);
        Line line = parser.parse(reader, 1, new ExceptionErrorEventListener() );
        assertNotNull(line);
    }
    
    
}
