package org.jsapar.compose.csv;

import org.jsapar.error.JSaParException;
import org.jsapar.model.Line;
import org.jsapar.model.StringCell;
import org.jsapar.schema.CsvSchemaCell;
import org.jsapar.schema.CsvSchemaLine;
import org.jsapar.schema.SchemaException;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;

/**
 * Created by stejon0 on 2016-01-30.
 */
public class CsvLineComposerTest {

    @Test
    public void testOutput() throws IOException, JSaParException {

        CsvSchemaLine schemaLine = new CsvSchemaLine(1);
        schemaLine.setCellSeparator(";-)");
        schemaLine.addSchemaCell(new CsvSchemaCell("First Name"));
        schemaLine.addSchemaCell(new CsvSchemaCell("Last Name"));

        Line line = new Line("");
        line.addCell(new StringCell("First Name", "Jonas"));
        line.addCell(new StringCell("Last Name", "Stenberg"));
        StringWriter writer = new StringWriter();

        CsvLineComposer lineComposer = new CsvLineComposer(writer, schemaLine, "\n");
        lineComposer.compose(line);

        assertEquals("Jonas;-)Stenberg", writer.toString());

    }

    @Test
    public void testOutput_ignoreWrite() throws IOException, JSaParException {

        CsvSchemaLine schemaLine = new CsvSchemaLine(1);
        schemaLine.setCellSeparator(";-)");
        CsvSchemaCell firstNameSchema = new CsvSchemaCell("First Name");
        firstNameSchema.setIgnoreWrite(true);
        schemaLine.addSchemaCell(firstNameSchema);
        schemaLine.addSchemaCell(new CsvSchemaCell("Last Name"));

        Line line = new Line("");
        line.addCell(new StringCell("First Name", "Jonas"));
        line.addCell(new StringCell("Last Name", "Stenberg"));
        StringWriter writer = new StringWriter();

        CsvLineComposer lineComposer = new CsvLineComposer(writer, schemaLine, "\n");
        lineComposer.compose(line);

        assertEquals(";-)Stenberg", writer.toString());

    }

    @Test
    public void testOutput_2byte_unicode() throws IOException, JSaParException {

        CsvSchemaLine schemaLine = new CsvSchemaLine(1);
        schemaLine.setCellSeparator("\uFFD0");
        schemaLine.addSchemaCell(new CsvSchemaCell("First Name"));
        schemaLine.addSchemaCell(new CsvSchemaCell("Last Name"));

        Line line = new Line("");
        line.addCell(new StringCell("First Name", "Jonas"));
        line.addCell(new StringCell("Last Name", "Stenberg"));
        StringWriter writer = new StringWriter();

        CsvLineComposer lineComposer = new CsvLineComposer(writer, schemaLine, "\n");
        lineComposer.compose(line);

        assertEquals("Jonas\uFFD0Stenberg", writer.toString());
    }


    @Test
    public void testOutput_not_found_in_line() throws IOException, JSaParException {

        CsvSchemaLine schemaLine = new CsvSchemaLine(1);
        schemaLine.setCellSeparator(";-)");
        schemaLine.addSchemaCell(new CsvSchemaCell("First Name"));
        schemaLine.addSchemaCell(new CsvSchemaCell("Last Name"));
        schemaLine.addSchemaCell(new CsvSchemaCell("Shoe size"));

        Line line = new Line("");
        line.addCell(new StringCell("First Name", "Jonas"));
        line.addCell(new StringCell("Last Name", "Stenberg"));
        StringWriter writer = new StringWriter();

        CsvLineComposer lineComposer = new CsvLineComposer(writer, schemaLine, "\n");
        lineComposer.compose(line);

        assertEquals("Jonas;-)Stenberg;-)", writer.toString());

    }

    @Test
    public void testOutput_null_value() throws IOException, JSaParException {

        CsvSchemaLine schemaLine = new CsvSchemaLine(1);
        schemaLine.setCellSeparator(";-)");
        schemaLine.addSchemaCell(new CsvSchemaCell("First Name"));
        schemaLine.addSchemaCell(new CsvSchemaCell("Last Name"));
        CsvSchemaCell shoeSchema = new CsvSchemaCell("Shoe size");
        schemaLine.addSchemaCell(shoeSchema);

        Line line = new Line("");
        line.addCell(new StringCell("First Name", "Jonas"));
        line.addCell(new StringCell("Last Name", "Stenberg"));
        line.addCell(shoeSchema.makeEmptyCell());
        StringWriter writer = new StringWriter();

        CsvLineComposer lineComposer = new CsvLineComposer(writer, schemaLine, "\n");
        lineComposer.compose(line);

        assertEquals("Jonas;-)Stenberg;-)", writer.toString());

    }

    @Test
    public void testOutput_reorder() throws IOException, JSaParException {
        CsvSchemaLine schemaLine = new CsvSchemaLine(1);
        schemaLine.addSchemaCell(new CsvSchemaCell("First Name"));
        schemaLine.addSchemaCell(new CsvSchemaCell("Last Name"));
        schemaLine.setCellSeparator(";");

        Line line = new Line("");
        line.addCell(new StringCell("Last Name", "Stenberg"));
        line.addCell(new StringCell("First Name", "Jonas"));
        StringWriter writer = new StringWriter();

        CsvLineComposer lineComposer = new CsvLineComposer(writer, schemaLine, "\n");
        lineComposer.compose(line);

        assertEquals("Jonas;Stenberg", writer.toString());

    }

    @Test
    public void testOutput_default() throws IOException, JSaParException, ParseException, SchemaException {

        CsvSchemaLine schemaLine = new CsvSchemaLine(1);
        schemaLine.setCellSeparator(";-)");
        schemaLine.addSchemaCell(new CsvSchemaCell("First Name"));
        CsvSchemaCell lastNameSchema = new CsvSchemaCell("Last Name");
        lastNameSchema.setDefaultValue("Svensson");
        schemaLine.addSchemaCell(lastNameSchema);

        Line line = new Line("");
        line.addCell(new StringCell("First Name", "Jonas"));
        StringWriter writer = new StringWriter();

        CsvLineComposer lineComposer = new CsvLineComposer(writer, schemaLine, "\n");
        lineComposer.compose(line);

        assertEquals("Jonas;-)Svensson", writer.toString());

    }




}