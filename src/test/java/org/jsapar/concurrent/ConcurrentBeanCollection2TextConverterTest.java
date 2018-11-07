package org.jsapar.concurrent;

import org.jsapar.BeanCollection2TextConverter;
import org.jsapar.TstPerson;
import org.jsapar.TstPostAddress;
import org.jsapar.model.CellType;
import org.jsapar.schema.CsvSchema;
import org.jsapar.schema.CsvSchemaCell;
import org.jsapar.schema.CsvSchemaLine;
import org.junit.Test;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class ConcurrentBeanCollection2TextConverterTest {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void testConvert() throws Exception {
        List<TstPerson> people = new LinkedList<>();
        TstPerson testPerson1 = new TstPerson("Nils", "Holgersson", (short)4, 4711, dateFormat.parse("1902-08-07 12:43:22"), 9, 'A');
        testPerson1.setAddress(new TstPostAddress("Track", "Village"));
        people.add(testPerson1);

        TstPerson testPerson2 = new TstPerson("Jonathan", "Lionheart", (short)37, 17, dateFormat.parse("1955-03-17 12:33:12"), 123456, 'C');
        testPerson2.setAddress(new TstPostAddress("Path", "City"));
        people.add(testPerson2);

        CsvSchema schema = new CsvSchema();
        CsvSchemaLine schemaLine = new CsvSchemaLine(TstPerson.class.getName());
        schemaLine.addSchemaCell(new CsvSchemaCell("firstName", CellType.STRING));
        schemaLine.addSchemaCell(new CsvSchemaCell("lastName", CellType.STRING));
        schema.addSchemaLine(schemaLine);
        StringWriter writer = new StringWriter();
        BeanCollection2TextConverter<TstPerson> converter = new ConcurrentBeanCollection2TextConverter<>(schema);
        converter.convert(people, writer);

        String result=writer.toString();
        String[] resultLines = result.split(schema.getLineSeparator());
        //        System.out.println(result);
        assertEquals("Nils;Holgersson", resultLines[0]);
        assertEquals("Jonathan;Lionheart", resultLines[1]);
    }

}