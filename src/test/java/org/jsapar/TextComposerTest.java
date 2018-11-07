package org.jsapar;

import org.jsapar.error.JSaParException;
import org.jsapar.model.*;
import org.jsapar.schema.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TextComposerTest {
    private Document       document;
    private java.util.Date birthTime;

    @Before
    public void setUp() throws Exception {
        document = new Document();
        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.birthTime = dateFormat.parse("1971-03-25 23:04:24");

        Line line1 = new Line("org.jsapar.TstPerson");
        line1.addCell(new StringCell("FirstName", "Jonas"));
        line1.addCell(new StringCell("LastName", "Stenberg"));
        line1.addCell(new IntegerCell("ShoeSize", 42));
        line1.addCell(new DateCell("BirthTime", this.birthTime));
        line1.addCell(new IntegerCell("LuckyNumber", 123456787901234567L));
        // line1.addCell(new StringCell("NeverUsed", "Should not be assigned"));

        Line line2 = new Line("org.jsapar.TstPerson");
        line2.addCell(new StringCell("FirstName", "Frida"));
        line2.addCell(new StringCell("LastName", "Bergsten"));

        document.addLine(line1);
        document.addLine(line2);

    }

    @Test
    public final void testWrite() throws IOException {
        String sExpected = "JonasStenberg" + System.getProperty("line.separator") + "FridaBergsten";
        org.jsapar.schema.FixedWidthSchema schema = new org.jsapar.schema.FixedWidthSchema();
        FixedWidthSchemaLine schemaLine = new FixedWidthSchemaLine("org.jsapar.TstPerson");
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("FirstName", 5));
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("LastName", 8));
        schema.addSchemaLine(schemaLine);

        Writer writer = new StringWriter();
        TextComposer composer = new TextComposer(schema, writer);
        composer.compose(document);

        assertEquals(sExpected, writer.toString());
    }

    @Test
    public final void testWriteCsv() throws IOException {
        String sExpected = "Jonas;Stenberg" + System.getProperty("line.separator") + "Frida;Bergsten";
        org.jsapar.schema.CsvSchema schema = new org.jsapar.schema.CsvSchema();
        CsvSchemaLine schemaLine = new CsvSchemaLine("org.jsapar.TstPerson");
        schemaLine.addSchemaCell(new CsvSchemaCell("FirstName"));
        schemaLine.addSchemaCell(new CsvSchemaCell("LastName"));
        schema.addSchemaLine(schemaLine);

        Writer writer = new StringWriter();
        TextComposer composer = new TextComposer(schema, writer);
        composer.compose(document);

        assertEquals(sExpected, writer.toString());
    }

    @Test
    public void testOutputLine_FixedWidthControllCell()
            throws IOException, JSaParException, ParseException, SchemaException {
        FixedWidthSchema schema = new FixedWidthSchema();
        schema.setLineSeparator("");

        FixedWidthSchemaLine schemaLine = new FixedWidthSchemaLine("Name");
        FixedWidthSchemaCell typeCellSchema = new FixedWidthSchemaCell("Type", 1);
        typeCellSchema.setDefaultValue("N");
        typeCellSchema.setLineCondition(new MatchingCellValueCondition("N"));
        schemaLine.addSchemaCell(typeCellSchema);
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("First name", 5));
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("Last name", 8));
        schema.addSchemaLine(schemaLine);

        Line line = new Line("Name");
        line.addCell(new StringCell("First name","Jonas"));
        line.addCell(new StringCell("Last name","Stenberg"));

        Writer writer = new StringWriter();
        TextComposer composer = new TextComposer(schema, writer);
        composer.writeLine(line);

        assertEquals("NJonasStenberg", writer.toString());
    }

    
    @Test
    public void testWriteLine_FixedWidthControllCell_minLength()
            throws IOException, JSaParException, ParseException, SchemaException {
        FixedWidthSchema schema = new FixedWidthSchema();
        schema.setLineSeparator("");

        FixedWidthSchemaLine schemaLine = new FixedWidthSchemaLine("Name");
        FixedWidthSchemaCell typeCellSchema = new FixedWidthSchemaCell("Type", 1);
        typeCellSchema.setDefaultValue("N");
        typeCellSchema.setLineCondition(new MatchingCellValueCondition("N"));
        schemaLine.addSchemaCell(typeCellSchema);
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("First name", 5));
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("Last name", 8));
        schemaLine.setMinLength(20);
        schema.addSchemaLine(schemaLine);

        Line line = new Line("Name");
        line.addCell(new StringCell("First name","Jonas"));
        line.addCell(new StringCell("Last name","Stenberg"));

        Writer writer = new StringWriter();
        TextComposer composer = new TextComposer(schema, writer);
        composer.writeLine(line);

        String result = writer.toString();
        assertEquals(20, result.length());
        assertEquals("NJonasStenberg      ", result);
    }

    @Test
    public final void testWriteLine_csv() throws IOException, JSaParException {
        org.jsapar.schema.CsvSchema schema = new org.jsapar.schema.CsvSchema();
        CsvSchemaLine outputSchemaLine = new CsvSchemaLine("Header");
        outputSchemaLine.setOccurs(1);
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("Header"));
        schema.addSchemaLine(outputSchemaLine);

        outputSchemaLine = new CsvSchemaLine("Person");
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("First name"));
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("Last name"));
        outputSchemaLine.setCellSeparator(";");
        schema.addSchemaLine(outputSchemaLine);

        Line line1 = new Line("Person");
        line1.addCell(new StringCell("First name","Jonas"));
        line1.addCell(new StringCell("Last name","Stenberg"));

        StringWriter writer = new StringWriter();
        TextComposer composer = new TextComposer(schema, writer);
        assertTrue(composer.writeLine(line1));

        String sExpected = "Jonas;Stenberg";

        assertEquals(sExpected, writer.toString());
    }

    @Test
    public final void testWriteLine_csv_first() throws IOException, JSaParException {
        org.jsapar.schema.CsvSchema schema = new org.jsapar.schema.CsvSchema();
        CsvSchemaLine outputSchemaLine = new CsvSchemaLine("Header");
        outputSchemaLine.setOccurs(1);
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("Header"));
        schema.addSchemaLine(outputSchemaLine);

        outputSchemaLine = new CsvSchemaLine();
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("First name"));
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("Last name"));
        outputSchemaLine.setCellSeparator(";");
        schema.addSchemaLine(outputSchemaLine);

        Line line1 = new Line("Header");
        line1.addCell(new StringCell("Header", "TheHeader"));
        line1.addCell(new StringCell("Something", "This should not be written"));

        StringWriter writer = new StringWriter();
        TextComposer composer = new TextComposer(schema, writer);
        assertTrue(composer.writeLine(line1));

        String sExpected = "TheHeader";

        assertEquals(sExpected, writer.toString());
    }

    @Test
    public final void testOutputLine_firstLineAsHeader()
            throws IOException, JSaParException, ParseException, SchemaException {
        CsvSchema schema = new CsvSchema();
        CsvSchemaLine schemaLine = new CsvSchemaLine(1);
        schemaLine.addSchemaCell(new CsvSchemaCell("HeaderHeader"));
        schema.addSchemaLine(schemaLine);

        schemaLine = new CsvSchemaLine("Person");
        schemaLine.addSchemaCell(new CsvSchemaCell("First name"));
        schemaLine.addSchemaCell(new CsvSchemaCell("Last name"));

        CsvSchemaCell shoeSizeCell = new CsvSchemaCell("Shoe size",CellType.INTEGER);
        shoeSizeCell.setDefaultValue("41");
        schemaLine.addSchemaCell(shoeSizeCell);

        schemaLine.addSchemaCell(new CsvSchemaCell("Birth date", CellType.DATE, "yyyy-MM-dd", Locale.US));

        schemaLine.setFirstLineAsSchema(true);
        schema.addSchemaLine(schemaLine);

        Line line1 = new Line("Person");
        line1.addCell(new StringCell("First name", "Jonas"));
        line1.addCell(new StringCell("Last name", "Stenberg"));

        StringWriter writer = new StringWriter();
        TextComposer composer = new TextComposer(schema, writer);
        assertTrue(composer.writeLine(line1));

        String sLineSep = System.getProperty("line.separator");
        String sExpected = "First name;Last name;Shoe size;Birth date" + sLineSep + "Jonas;Stenberg;41;";

        assertEquals(sExpected, writer.toString());
    }

}
