package org.jsapar.compose.csv;

import org.jsapar.error.JSaParException;
import org.jsapar.model.Document;
import org.jsapar.model.Line;
import org.jsapar.model.StringCell;
import org.jsapar.schema.CsvSchema;
import org.jsapar.schema.CsvSchemaCell;
import org.jsapar.schema.CsvSchemaLine;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * Created by stejon0 on 2016-01-30.
 */
public class CsvComposerTest {

    @Test
    public final void testCompose_firstLineAsHeader() throws IOException, JSaParException {
        CsvSchema schema = new CsvSchema();

        CsvSchemaLine schemaLine = new CsvSchemaLine();
        schemaLine.addSchemaCell(new CsvSchemaCell("FirstName"));
        schemaLine.addSchemaCell(new CsvSchemaCell("LastName"));
        schemaLine.setFirstLineAsSchema(true);
        schema.addSchemaLine(schemaLine);

        Document doc = new Document();

        Line line1 = new Line("");
        line1.addCell(new StringCell("FirstName","Jonas"));
        line1.addCell(new StringCell("LastName","Stenberg"));
        doc.addLine(line1);

        Line line2 = new Line("");
        line2.addCell(new StringCell("FirstName","Nils"));
        line2.addCell(new StringCell("LastName", "Nilsson"));
        doc.addLine(line2);

        StringWriter writer = new StringWriter();
        CsvComposer composer = new CsvComposer(writer, schema);

        composer.compose(doc.iterator());

        String sLineSep = System.getProperty("line.separator");
        String sExpected = "FirstName;LastName" + sLineSep + "Jonas;Stenberg" + sLineSep + "Nils;Nilsson";

        assertEquals(sExpected, writer.toString());
    }


}