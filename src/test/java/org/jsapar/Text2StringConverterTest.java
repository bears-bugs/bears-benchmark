package org.jsapar;

import org.jsapar.parse.text.TextParseConfig;
import org.jsapar.schema.CsvSchema;
import org.jsapar.schema.CsvSchemaCell;
import org.jsapar.schema.CsvSchemaLine;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

public class Text2StringConverterTest {

    @Test
    public void convert() throws IOException {
        CsvSchema parseSchema = new CsvSchema();
        parseSchema.addSchemaLine(new CsvSchemaLine("test-line")
                .addSchemaCell(new CsvSchemaCell("c1"))
                .addSchemaCell(new CsvSchemaCell("c2"))
                .addSchemaCell(new CsvSchemaCell("c3")));
        CsvSchema composeSchema = new CsvSchema();
        composeSchema.addSchemaLine(new CsvSchemaLine("test-line")
                .addSchemaCell(new CsvSchemaCell("c2"))
                .addSchemaCell(new CsvSchemaCell("c1"))
                .addSchemaCell(new CsvSchemaCell("c3")));

        Text2StringConverter converter = new Text2StringConverter(parseSchema, composeSchema);
        String source = "v11;v12;v13\nv21;v22;v23\n";
        converter.convert(new StringReader(source), e -> {
            assertEquals("test-line", e.getLineType());
            switch ((int) e.getLineNumber()) {
                case 1:
                    assertArrayEquals(new String[]{"v12", "v11", "v13"}, e.stream().toArray());
                    break;
                case 2:
                    assertArrayEquals(new String[]{"v22", "v21", "v23"}, e.stream().toArray());
                    break;
            }
        });
    }

    @Test
    public void Text2StringConverter() {
        CsvSchema parseSchema = new CsvSchema();
        CsvSchema composeSchema = new CsvSchema();
        TextParseConfig config = new TextParseConfig();
        Text2StringConverter converter = new Text2StringConverter(parseSchema, composeSchema, config);
        assertSame(config, converter.getParseConfig());
    }

    @Test
    public void getParseConfig() {
        Text2StringConverter converter = new Text2StringConverter(null, null);
        assertNotNull(converter.getParseConfig());
    }

    @Test
    public void setParseConfig() {
        Text2StringConverter converter = new Text2StringConverter(null, null);
        TextParseConfig config = new TextParseConfig();
        converter.setParseConfig(config);
        assertSame(config, converter.getParseConfig());
    }
}