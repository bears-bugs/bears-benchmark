package org.jsapar;

import org.jsapar.schema.Schema;
import org.jsapar.schema.Xml2SchemaBuilder;
import org.junit.Test;

import java.io.FileReader;
import java.io.Reader;
import java.io.StringWriter;

/**
 */
public class Text2XmlConverterTest {

    @Test
    public void testConvert() throws Exception {
        try (Reader fileReader = new FileReader("examples/01_Names.csv");
                Reader schemaReader = new FileReader("examples/01_CsvSchema.xml")) {
            Xml2SchemaBuilder xmlBuilder = new Xml2SchemaBuilder();
            Schema schema = xmlBuilder.build(schemaReader);
            Text2XmlConverter converter = new Text2XmlConverter(schema);

            StringWriter w = new StringWriter();
            converter.convert(fileReader, w);
            System.out.print(w.toString());
        }

    }

}