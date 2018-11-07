package org.jsapar.compose.fixed;

import org.jsapar.error.JSaParException;
import org.jsapar.model.Document;
import org.jsapar.model.Line;
import org.jsapar.model.StringCell;
import org.jsapar.schema.FixedWidthSchemaCell;
import org.jsapar.schema.FixedWidthSchemaLine;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by stejon0 on 2016-01-31.
 */
public class FixedWidthComposerTest {

    @Test
    public final void testOutput_Flat() throws IOException, JSaParException {
        String sExpected = "JonasStenbergFridaBergsten";
        org.jsapar.schema.FixedWidthSchema schema = new org.jsapar.schema.FixedWidthSchema();
        FixedWidthSchemaLine schemaLine = new FixedWidthSchemaLine(2);
        schema.setLineSeparator("");

        schemaLine.addSchemaCell(new FixedWidthSchemaCell("First name", 5));
        schemaLine.addSchemaCell(new FixedWidthSchemaCell("Last name", 8));
        schema.addSchemaLine(schemaLine);

        Line line1 = new Line("");
        line1.addCell(new StringCell("First name","Jonas"));
        line1.addCell(new StringCell("Last name","Stenberg"));

        Line line2 = new Line("");
        line2.addCell(new StringCell("First name","Frida"));
        line2.addCell(new StringCell("Last name","Bergsten"));

        Document doc = new Document();
        doc.addLine(line1);
        doc.addLine(line2);

        java.io.Writer writer = new java.io.StringWriter();
        FixedWidthComposer composer = new FixedWidthComposer(writer, schema);
        composer.compose(doc.iterator());

        assertEquals(sExpected, writer.toString());
    }

}