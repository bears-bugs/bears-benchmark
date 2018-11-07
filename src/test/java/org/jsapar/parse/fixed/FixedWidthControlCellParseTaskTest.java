package org.jsapar.parse.fixed;

import org.jsapar.error.ExceptionErrorEventListener;
import org.jsapar.error.ValidationAction;
import org.jsapar.model.Document;
import org.jsapar.parse.DocumentBuilderLineEventListener;
import org.jsapar.parse.LineParseException;
import org.jsapar.parse.text.TextParseConfig;
import org.jsapar.schema.FixedWidthSchema;
import org.jsapar.schema.FixedWidthSchemaCell;
import org.jsapar.schema.FixedWidthSchemaLine;
import org.jsapar.schema.MatchingCellValueCondition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class FixedWidthControlCellParseTaskTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     *
     * .
     * 
     * @throws IOException
     *
     */
    @Test
    public void testParse() throws IOException {
        String toParse = "NJonasStenbergAStorgatan 123 45NFred Bergsten";
        org.jsapar.schema.FixedWidthSchema schema = new FixedWidthSchema();
        schema.setLineSeparator("");

        addSchemaLinesOneCharControl(schema);

        Reader reader = new StringReader(toParse);
        FixedWidthParser parser = new FixedWidthParserFlat(reader, schema);
        DocumentBuilderLineEventListener builder = new DocumentBuilderLineEventListener();
        parser.parse(builder, new ExceptionErrorEventListener());
        Document doc = builder.getDocument();

        checkResult(doc);
    }

    private void addSchemaLinesOneCharControl(FixedWidthSchema schema) {
        FixedWidthSchemaLine schemaLine = new FixedWidthSchemaLine("Name");
        FixedWidthSchemaCell typeN = new FixedWidthSchemaCell("Type", 1);
        typeN.setLineCondition(new MatchingCellValueCondition("N"));
        schemaLine.addSchemaCell(typeN);
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("First name", 5));
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("Last name", 8));
        schema.addSchemaLine(schemaLine);

        schemaLine = new FixedWidthSchemaLine("Address");
        FixedWidthSchemaCell typeA = new FixedWidthSchemaCell("Type", 1);
        typeA.setLineCondition(new MatchingCellValueCondition("A"));
        schemaLine.addSchemaCell(typeA);
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("Street", 10));
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("Zip code", 6));
        schema.addSchemaLine(schemaLine);
    }

    private void addSchemaLinesTwoCharControl(FixedWidthSchema schema) {
        FixedWidthSchemaLine schemaLine = new FixedWidthSchemaLine("Name");
        FixedWidthSchemaCell typeN = new FixedWidthSchemaCell("Type", 2);
        typeN.setLineCondition(new MatchingCellValueCondition("N"));
        schemaLine.addSchemaCell(typeN);
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("First name", 5));
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("Last name", 8));
        schema.addSchemaLine(schemaLine);

        schemaLine = new FixedWidthSchemaLine("Address");
        FixedWidthSchemaCell typeA = new FixedWidthSchemaCell("Type", 2);
        typeA.setLineCondition(new MatchingCellValueCondition("AA"));
        schemaLine.addSchemaCell(typeA);
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("Street", 10));
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("Zip code", 6));
        schema.addSchemaLine(schemaLine);
    }
    /**
     * Test method for
     *
     * .
     * 
     * @throws IOException
     *
     */
    @Test
    public void testParse_separatedLines() throws IOException {
        String toParse = "NJonasStenberg   \r\nAStorgatan 123 45          \r\nNFred Bergsten\r\n";
        org.jsapar.schema.FixedWidthSchema schema = new FixedWidthSchema();
        schema.setLineSeparator("\r\n");

        addSchemaLinesOneCharControl(schema);

        Reader reader = new StringReader(toParse);
        Document doc = build(reader, schema);

        checkResult(doc);
    }

    private void checkResult(Document doc) {
        assertEquals("Jonas", doc.getLine(0).getCell("First name").orElseThrow(() -> new AssertionError("Should be set")).getStringValue());
        assertEquals("Stenberg", doc.getLine(0).getCell("Last name").orElseThrow(() -> new AssertionError("Should be set")).getStringValue());

        assertEquals("Storgatan", doc.getLine(1).getCell("Street").orElseThrow(() -> new AssertionError("Should be set")).getStringValue());
        assertEquals("123 45", doc.getLine(1).getCell("Zip code").orElseThrow(() -> new AssertionError("Should be set")).getStringValue());

        assertEquals("Fred", doc.getLine(2).getCell("First name").orElseThrow(() -> new AssertionError("Should be set")).getStringValue());
        assertEquals("Bergsten", doc.getLine(2).getCell("Last name").orElseThrow(() -> new AssertionError("Should be set")).getStringValue());
    }

    /**
     * Test method for
     *
     * .
     * 
     * @throws IOException
     *
     */
    @Test
    public void testParse_spaceInLineType() throws IOException {
        String toParse = "N JonasStenberg   \r\nAAStorgatan 123 45          \r\nN Fred Bergsten";
        org.jsapar.schema.FixedWidthSchema schema = new FixedWidthSchema();
        schema.setLineSeparator("\r\n");

        addSchemaLinesTwoCharControl(schema);

        Reader reader = new StringReader(toParse);
        Document doc = build(reader, schema);

        checkResult(doc);
    }

    /**
     * Test method for
     *
     * .
     * 
     * @throws IOException
     *
     */
    @Test(expected = LineParseException.class)
    public void testParse_errorOnUndefinedLineType() throws IOException {
        String toParse = "X JonasStenberg   ";
        org.jsapar.schema.FixedWidthSchema schema = new FixedWidthSchema();
        schema.setLineSeparator("\r\n");

        addSchemaLinesTwoCharControl(schema);

        Reader reader = new StringReader(toParse);
        TextParseConfig config = new TextParseConfig();
        config.setOnUndefinedLineType(ValidationAction.EXCEPTION);
        Document doc = build(reader, schema, config);
    }

    /**
     * Test method for
     *
     * .
     * 
     * @throws IOException
     *
     */
    @Test
    public void testParse_noErrorIfUndefinedLineType() throws IOException {
        String toParse = "N JonasStenberg   \r\nXXStorgatan 123 45          \r\n\r\nN Fred Bergsten";
        org.jsapar.schema.FixedWidthSchema schema = new FixedWidthSchema();
        schema.setLineSeparator("\r\n");

        addSchemaLinesTwoCharControl(schema);

        Reader reader = new StringReader(toParse);
        TextParseConfig config = new TextParseConfig();
        config.setOnUndefinedLineType(ValidationAction.OMIT_LINE);
        Document doc = build(reader, schema, config);

        assertEquals(2, doc.size());
        assertEquals("Jonas", doc.getLine(0).getCell("First name").orElseThrow(() -> new AssertionError("Should be set")).getStringValue());
        assertEquals("Stenberg", doc.getLine(0).getCell("Last name").orElseThrow(() -> new AssertionError("Should be set")).getStringValue());

        assertEquals("Fred", doc.getLine(1).getCell("First name").orElseThrow(() -> new AssertionError("Should be set")).getStringValue());
        assertEquals("Bergsten", doc.getLine(1).getCell("Last name").orElseThrow(() -> new AssertionError("Should be set")).getStringValue());
    }

    private Document build(Reader reader, FixedWidthSchema schema) throws IOException {
        return build(reader, schema, new TextParseConfig());
    }

    private Document build(Reader reader, FixedWidthSchema schema, TextParseConfig config) throws IOException {
        FixedWidthParser parser = new FixedWidthParserLinesSeparated(reader, schema, config);
        DocumentBuilderLineEventListener builder = new DocumentBuilderLineEventListener();
        parser.parse(builder, new ExceptionErrorEventListener());
        return builder.getDocument();
    }


}
