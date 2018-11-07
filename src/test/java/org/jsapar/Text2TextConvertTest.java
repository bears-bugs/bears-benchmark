/**
 * 
 */
package org.jsapar;

import org.jsapar.convert.LineManipulator;
import org.jsapar.error.JSaParException;
import org.jsapar.error.MaxErrorsExceededException;
import org.jsapar.error.RecordingErrorEventListener;
import org.jsapar.error.ThresholdRecordingErrorEventListener;
import org.jsapar.model.CellType;
import org.jsapar.model.Line;
import org.jsapar.model.StringCell;
import org.jsapar.schema.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author stejon0
 * 
 */
public class Text2TextConvertTest {

    public static final String LN = System.getProperty("line.separator");

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


    @Test
    public void testConvert() throws IOException, JSaParException {
        String toParse = "Jonas Stenberg " + LN + "Frida Bergsten ";
        ;
        org.jsapar.schema.FixedWidthSchema inputSchema = new org.jsapar.schema.FixedWidthSchema();
        FixedWidthSchemaLine inputSchemaLine = new FixedWidthSchemaLine("Person");
        inputSchemaLine.addSchemaCell(new FixedWidthSchemaCell("First name", 6));
        inputSchemaLine.addSchemaCell(new FixedWidthSchemaCell("Last name", 9));
        inputSchema.addSchemaLine(inputSchemaLine);

        org.jsapar.schema.CsvSchema outputSchema = new org.jsapar.schema.CsvSchema();
        CsvSchemaLine outputSchemaLine = new CsvSchemaLine("Person");
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("First name"));
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("Last name"));
        outputSchemaLine.setCellSeparator(";");
        outputSchema.addSchemaLine(outputSchemaLine);
        outputSchema.setLineSeparator("|");

        StringWriter writer = new StringWriter();
        StringReader reader = new StringReader(toParse);
        Text2TextConverter converter = new Text2TextConverter(inputSchema, outputSchema);
        converter.convert(reader, writer);
        reader.close();
        writer.close();
        String sResult = writer.getBuffer().toString();
        String sExpected = "Jonas;Stenberg|Frida;Bergsten";

        Assert.assertEquals(sExpected, sResult);

    }

    @Test
    public void testConvert_error() throws IOException, JSaParException {
        String toParse = "Jonas 41       " + LN + "Frida ERROR    ";
        ;
        org.jsapar.schema.FixedWidthSchema inputSchema = new org.jsapar.schema.FixedWidthSchema();
        FixedWidthSchemaLine inputSchemaLine = new FixedWidthSchemaLine("Person");
        inputSchemaLine.addSchemaCell(new FixedWidthSchemaCell("First name", 6));
        FixedWidthSchemaCell schemaCell2 = new FixedWidthSchemaCell("Shoe size", 9, new SchemaCellFormat(
                CellType.INTEGER));
        inputSchemaLine.addSchemaCell(schemaCell2);
        inputSchema.addSchemaLine(inputSchemaLine);

        org.jsapar.schema.CsvSchema outputSchema = new org.jsapar.schema.CsvSchema();
        CsvSchemaLine outputSchemaLine = new CsvSchemaLine("Person");
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("First name"));
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("Shoe size"));
        outputSchemaLine.setCellSeparator(";");
        outputSchema.addSchemaLine(outputSchemaLine);

        try (StringWriter writer = new StringWriter(); StringReader reader = new StringReader(toParse)) {
            Text2TextConverter converter = new Text2TextConverter(inputSchema, outputSchema);
            RecordingErrorEventListener errorEventListener = new RecordingErrorEventListener();
            converter.setErrorEventListener(errorEventListener);
            converter.convert(reader, writer);
            String sResult = writer.getBuffer().toString();
            String sExpected = "Jonas;41" + LN + "Frida;";

            Assert.assertEquals(1, errorEventListener.getErrors().size());
            Assert.assertEquals(sExpected, sResult);
        }

    }

    @Test(expected = MaxErrorsExceededException.class)
    public void testConvert_max_error() throws IOException, JSaParException {
        String toParse = "Jonas 41       " + LN + "Frida ERROR    ";
        ;
        org.jsapar.schema.FixedWidthSchema inputSchema = new org.jsapar.schema.FixedWidthSchema();
        FixedWidthSchemaLine inputSchemaLine = new FixedWidthSchemaLine("Person");
        inputSchemaLine.addSchemaCell(new FixedWidthSchemaCell("First name", 6));
        FixedWidthSchemaCell schemaCell2 = new FixedWidthSchemaCell("Shoe size", 9, new SchemaCellFormat(
                CellType.INTEGER));
        inputSchemaLine.addSchemaCell(schemaCell2);
        inputSchema.addSchemaLine(inputSchemaLine);

        org.jsapar.schema.CsvSchema outputSchema = new org.jsapar.schema.CsvSchema();
        CsvSchemaLine outputSchemaLine = new CsvSchemaLine("Person");
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("First name"));
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("Shoe size"));
        outputSchemaLine.setCellSeparator(";");
        outputSchema.addSchemaLine(outputSchemaLine);

        try (StringWriter writer = new StringWriter(); StringReader reader = new StringReader(toParse)) {
            Text2TextConverter converter = new Text2TextConverter(inputSchema, outputSchema);
            RecordingErrorEventListener errorEventListener = new ThresholdRecordingErrorEventListener(0);
            converter.setErrorEventListener(errorEventListener);
            converter.convert(reader, writer);
        }

    }

    @Test
    public void testConvert_oneLine() throws IOException, JSaParException {
        String toParse = "Jonas Stenberg ";
        ;
        org.jsapar.schema.FixedWidthSchema inputSchema = new org.jsapar.schema.FixedWidthSchema();
        FixedWidthSchemaLine inputSchemaLine = new FixedWidthSchemaLine();
        inputSchemaLine.addSchemaCell(new FixedWidthSchemaCell("First name", 6));
        inputSchemaLine.addSchemaCell(new FixedWidthSchemaCell("Last name", 9));
        inputSchema.addSchemaLine(inputSchemaLine);

        org.jsapar.schema.CsvSchema outputSchema = new org.jsapar.schema.CsvSchema();
        CsvSchemaLine outputSchemaLine = new CsvSchemaLine();
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("First name"));
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("Last name"));
        outputSchemaLine.setCellSeparator(";");
        outputSchema.addSchemaLine(outputSchemaLine);

        StringWriter writer = new StringWriter();
        StringReader reader = new StringReader(toParse);
        Text2TextConverter converter = new Text2TextConverter(inputSchema, outputSchema);
        converter.convert(reader, writer);
        String sResult = writer.getBuffer().toString();
        String sExpected = "Jonas;Stenberg";

        Assert.assertEquals(sExpected, sResult);

    }

    @Test
    public void testConvert_twoKindOfLines() throws IOException, JSaParException {
        String toParse = "This file contains names" + LN + "Jonas Stenberg "
                + LN + "Frida Bergsten ";
        ;
        org.jsapar.schema.FixedWidthSchema inputSchema = new org.jsapar.schema.FixedWidthSchema();
        FixedWidthSchemaLine inputSchemaLine = new FixedWidthSchemaLine("Header");
        inputSchemaLine.setOccurs(1);
        inputSchemaLine.addSchemaCell(new FixedWidthSchemaCell("Header", 100));
        inputSchema.addSchemaLine(inputSchemaLine);

        inputSchemaLine = new FixedWidthSchemaLine("Person");
        inputSchemaLine.addSchemaCell(new FixedWidthSchemaCell("First name", 6));
        inputSchemaLine.addSchemaCell(new FixedWidthSchemaCell("Last name", 9));
        inputSchema.addSchemaLine(inputSchemaLine);

        org.jsapar.schema.CsvSchema outputSchema = new org.jsapar.schema.CsvSchema();
        CsvSchemaLine outputSchemaLine = new CsvSchemaLine("Header");
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("Header"));
        outputSchema.addSchemaLine(outputSchemaLine);

        outputSchemaLine = new CsvSchemaLine("Person");
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("First name"));
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("Last name"));
        outputSchemaLine.setCellSeparator(";");
        outputSchema.addSchemaLine(outputSchemaLine);

        StringWriter writer = new StringWriter();
        StringReader reader = new StringReader(toParse);
        Text2TextConverter converter = new Text2TextConverter(inputSchema, outputSchema);
        converter.convert(reader, writer);
        String sResult = writer.getBuffer().toString();
        String sExpected = "This file contains names" + LN + "Jonas;Stenberg"
                + LN + "Frida;Bergsten";

        Assert.assertEquals(sExpected, sResult);

    }

    @Test
    public void testConvert_Manipulated() throws IOException, JSaParException {
        String toParse = "Jonas Stenberg " + LN + "Frida Bergsten ";
        ;
        org.jsapar.schema.FixedWidthSchema inputSchema = new org.jsapar.schema.FixedWidthSchema();
        FixedWidthSchemaLine inputSchemaLine = new FixedWidthSchemaLine();
        inputSchemaLine.addSchemaCell(new FixedWidthSchemaCell("First name", 6));
        inputSchemaLine.addSchemaCell(new FixedWidthSchemaCell("Last name", 9));
        inputSchema.addSchemaLine(inputSchemaLine);

        org.jsapar.schema.CsvSchema outputSchema = new org.jsapar.schema.CsvSchema();
        CsvSchemaLine outputSchemaLine = new CsvSchemaLine();
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("First name"));
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("Last name"));
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("Town"));
        outputSchemaLine.setCellSeparator(";");
        outputSchema.addSchemaLine(outputSchemaLine);

        StringWriter writer = new StringWriter();
        StringReader reader = new StringReader(toParse);
        Text2TextConverter converter = new Text2TextConverter(inputSchema, outputSchema);
        converter.addLineManipulator(new LineManipulator() {
            @Override
            public boolean manipulate(Line line) {
                line.addCell(new StringCell("Town", "Stockholm"));
                return true;
            }
        });
        converter.convert(reader, writer);
        String sResult = writer.getBuffer().toString();
        String sExpected = "Jonas;Stenberg;Stockholm" + LN
                + "Frida;Bergsten;Stockholm";

        Assert.assertEquals(sExpected, sResult);

    }


    @Test
    public void testConvert_twoKindOfLinesIn_OneKindOut() throws IOException, JSaParException {
        String toParse = "This file contains names" + LN + "Jonas Stenberg "
                + LN + "Frida Bergsten ";
        ;
        org.jsapar.schema.FixedWidthSchema inputSchema = new org.jsapar.schema.FixedWidthSchema();
        FixedWidthSchemaLine inputSchemaLine = new FixedWidthSchemaLine(1);
        inputSchemaLine.addSchemaCell(new FixedWidthSchemaCell("Header", 100));
        inputSchemaLine.setLineType("Header");
        inputSchema.addSchemaLine(inputSchemaLine);

        inputSchemaLine = new FixedWidthSchemaLine("Names");
        inputSchemaLine.addSchemaCell(new FixedWidthSchemaCell("First name", 6));
        inputSchemaLine.addSchemaCell(new FixedWidthSchemaCell("Last name", 9));
        inputSchema.addSchemaLine(inputSchemaLine);

        org.jsapar.schema.CsvSchema outputSchema = new org.jsapar.schema.CsvSchema();
        CsvSchemaLine outputSchemaLine;

        outputSchemaLine = new CsvSchemaLine("Names");
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("First name"));
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("Last name"));
        outputSchemaLine.setCellSeparator(";");
        outputSchema.addSchemaLine(outputSchemaLine);

        StringWriter writer = new StringWriter();
        StringReader reader = new StringReader(toParse);
        Text2TextConverter converter = new Text2TextConverter(inputSchema, outputSchema);
        converter.convert(reader, writer);
        String sResult = writer.getBuffer().toString();
        String sExpected = "Jonas;Stenberg" + LN + "Frida;Bergsten";

        Assert.assertEquals(sExpected, sResult);

    }


    @Test
    public void testConvert_filter() throws IOException, JSaParException {
        String toParse = "This file contains names" + LN + "Jonas Stenberg "
                + LN + "Frida Bergsten "
                + LN + "Tomas Stornos  ";
        ;
        org.jsapar.schema.FixedWidthSchema inputSchema = new org.jsapar.schema.FixedWidthSchema();
        FixedWidthSchemaLine inputSchemaLine = new FixedWidthSchemaLine(1);
        inputSchemaLine.addSchemaCell(new FixedWidthSchemaCell("Header", 100));
        inputSchemaLine.setLineType("Header");
        inputSchema.addSchemaLine(inputSchemaLine);

        inputSchemaLine = new FixedWidthSchemaLine("Names");
        inputSchemaLine.addSchemaCell(new FixedWidthSchemaCell("First name", 6));
        inputSchemaLine.addSchemaCell(new FixedWidthSchemaCell("Last name", 9));
        inputSchema.addSchemaLine(inputSchemaLine);

        org.jsapar.schema.CsvSchema outputSchema = new org.jsapar.schema.CsvSchema();
        CsvSchemaLine outputSchemaLine;

        outputSchemaLine = new CsvSchemaLine("Names");
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("First name"));
        outputSchemaLine.addSchemaCell(new CsvSchemaCell("Last name"));
        outputSchemaLine.setCellSeparator(";");
        outputSchema.addSchemaLine(outputSchemaLine);

        StringWriter writer = new StringWriter();
        StringReader reader = new StringReader(toParse);
        Text2TextConverter converter = new Text2TextConverter(inputSchema, outputSchema);
        converter.addLineManipulator(new LineManipulator() {
            @Override
            public boolean manipulate(Line line) {
                if(line.getLineType().equals("Names") && line.getCell("First name").orElseThrow(() -> new AssertionError("Should be set")).getStringValue().equals("Tomas"))
                    return false;
                else
                    return true;
            }
        });
        converter.convert(reader, writer);
        String sResult = writer.getBuffer().toString();
        String sExpected = "Jonas;Stenberg" + LN + "Frida;Bergsten";

        Assert.assertEquals(sExpected, sResult);

    }

}
