package org.jsapar.compose.string;

import org.jsapar.model.Document;
import org.jsapar.model.Line;
import org.jsapar.model.StringCell;
import org.jsapar.schema.CsvSchema;
import org.jsapar.schema.CsvSchemaCell;
import org.jsapar.schema.CsvSchemaLine;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringComposerTest {

    @Test
    public void compose() throws Exception {
        CsvSchema schema = new CsvSchema();

        CsvSchemaLine schemaLine = new CsvSchemaLine("person");
        schemaLine.addSchemaCell(new CsvSchemaCell("FirstName"));
        schemaLine.addSchemaCell(new CsvSchemaCell("LastName"));
        schema.addSchemaLine(schemaLine);

        Document doc = new Document();

        Line line1 = new Line("person");
        line1.addCell(new StringCell("FirstName","Jonas"));
        line1.addCell(new StringCell("LastName","Stenberg"));
        doc.addLine(line1);

        Line line2 = new Line("person");
        line2.addCell(new StringCell("FirstName","Nils"));
        line2.addCell(new StringCell("LastName", "Nilsson"));
        doc.addLine(line2);

        RecordingStringEventListener listener = new RecordingStringEventListener();
        StringComposer instance = new StringComposer(schema, listener);
        instance.compose(doc);
        assertEquals(2, listener.size() );
        assertEquals("Jonas", listener.getLines().get(0).get(0));
        assertEquals("Stenberg", listener.getLines().get(0).get(1));
        assertEquals("Nils", listener.getLines().get(1).get(0));
        assertEquals("Nilsson", listener.getLines().get(1).get(1));
    }

    @Test
    public void composeLine() throws Exception {

    }

}