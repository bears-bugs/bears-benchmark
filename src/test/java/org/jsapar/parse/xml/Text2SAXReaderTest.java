package org.jsapar.parse.xml;

import org.jsapar.TextParser;
import org.jsapar.schema.Xml2SchemaBuilder;
import org.junit.Test;
import org.xml.sax.InputSource;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringWriter;

/**
 * Created by stejon0 on 2017-04-01.
 */
public class Text2SAXReaderTest {

    @Test
    public void testParse() throws Exception {


        try (Reader fileReader = new FileReader("examples/01_Names.csv");
                Reader schemaReader = new FileReader("examples/01_CsvSchema.xml")) {
            Xml2SchemaBuilder xmlBuilder = new Xml2SchemaBuilder();
            TextParser parser = new TextParser(xmlBuilder.build(schemaReader));
            Text2SAXReader saxReader = new Text2SAXReader(parser);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            StringWriter w = new StringWriter();
            transformer.transform(new SAXSource(saxReader, new InputSource(fileReader)), new StreamResult(w));
            System.out.print(w.toString());
        }
    }

}