package org.jsapar.parse.bean;

import org.jsapar.TstPerson;
import org.jsapar.TstPostAddress;
import org.jsapar.error.ExceptionErrorEventListener;
import org.jsapar.model.*;
import org.jsapar.schema.CsvSchema;
import org.jsapar.schema.CsvSchemaCell;
import org.jsapar.schema.CsvSchemaLine;
import org.jsapar.schema.Schema;
import org.junit.Assert;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.util.Date;

import static org.junit.Assert.*;

public class BeanMarshallerTest {
    static final Date birthTime = new Date();

    @Test
    public void parseBean() {
    }

    @Test
    public void testBuildLine() throws IntrospectionException, ClassNotFoundException {
        TstPerson person = new TstPerson();
        person.setBirthTime(birthTime);
        person.setFirstName("Jonas");
        person.setLastName("Bergsten");
        person.setLuckyNumber(123456787901234567L);
        person.setShoeSize((short)42);
        person.setStreetNumber(4711);
        person.setDoor('A');


        BeanMarshaller<TstPerson> beanMarshaller = new BeanMarshaller<>(makeBeanMap());
        Line line = beanMarshaller.marshal(person, new ExceptionErrorEventListener(), 1).orElse(null);
        assertEquals("org.jsapar.TstPerson", line.getLineType());
        //        assertEquals(8, line.size());
        assertEquals("Jonas", line.getCell("firstName").orElseThrow(() -> new AssertionError("Should be set")).getStringValue());
        assertEquals(42, ((IntegerCell) line.getCell("shoeSize").orElseThrow(() -> new AssertionError("Should be set"))).getValue().shortValue());
        assertEquals(4711, ((IntegerCell) line.getCell("streetNumber").orElseThrow(() -> new AssertionError("Should be set"))).getValue().intValue());
        assertEquals(birthTime, ((DateCell) line.getCell("birthTime").orElseThrow(() -> new AssertionError("Should be set"))).getValue());
        assertEquals(123456787901234567L, ((IntegerCell) line.getCell("luckyNumber").orElseThrow(() -> new AssertionError("Should be set"))).getValue().longValue());
        assertEquals("A", LineUtils.getStringCellValue(line,"door"));
    }

    @Test
    public void testBuildLine_subClass() throws IntrospectionException, ClassNotFoundException {
        TstPerson person = new TstPerson();
        person.setFirstName("Jonas");
        person.setAddress(new TstPostAddress("Stigen", "Staden"));
        BeanMarshaller<TstPerson> beanMarshaller = new BeanMarshaller<>(makeBeanMap());
        Line line = beanMarshaller.marshal(person, new ExceptionErrorEventListener(), 1).orElse(null);
        assertEquals("org.jsapar.TstPerson", line.getLineType());
        assertEquals("Stigen", LineUtils.getStringCellValue(line,"address.street"));
        assertEquals("Staden", LineUtils.getStringCellValue(line,"address.town"));

        // Make sure that loops are avoided.
        Assert.assertFalse(line.isCellSet("address.owner.firstName"));
    }

    @Test
    public void testBuildLine_subClass_multiplePaths() throws IntrospectionException, ClassNotFoundException {
        TstPerson person = new TstPerson();
        person.setAddress(new TstPostAddress("Stigen", "Staden"));
        person.getAddress().setSubAddress(new TstPostAddress("Road", "Town"));
        person.setWorkAddress(new TstPostAddress("Gatan", "Byn"));
        BeanMarshaller<TstPerson> beanMarshaller = new BeanMarshaller<>(makeBeanMap());
        Line line = beanMarshaller.marshal(person, new ExceptionErrorEventListener(), 1).orElse(null);
        assertEquals("org.jsapar.TstPerson", line.getLineType());
        assertEquals("Stigen", LineUtils.getStringCellValue(line,"address.street"));
        assertEquals("Staden", LineUtils.getStringCellValue(line,"address.town"));
        assertEquals("Road", LineUtils.getStringCellValue(line,"address.subAddress.street"));
        assertEquals("Town", LineUtils.getStringCellValue(line,"address.subAddress.town"));
        assertEquals("Gatan", LineUtils.getStringCellValue(line,"workAddress.street"));
        assertEquals("Byn", LineUtils.getStringCellValue(line,"workAddress.town"));
    }

    public static Schema makeOutputSchema(){
        CsvSchema schema = new CsvSchema();
        CsvSchemaLine schemaLine = new CsvSchemaLine(TstPerson.class.getName());
        schemaLine.addSchemaCell(new CsvSchemaCell("firstName", CellType.STRING));
        schemaLine.addSchemaCell(new CsvSchemaCell("lastName", CellType.STRING));
        schemaLine.addSchemaCell(new CsvSchemaCell("shoeSize", CellType.INTEGER));
        schemaLine.addSchemaCell(new CsvSchemaCell("streetNumber", CellType.INTEGER));
        schemaLine.addSchemaCell(new CsvSchemaCell("luckyNumber", CellType.DECIMAL));
        schemaLine.addSchemaCell(new CsvSchemaCell("birthTime", CellType.DATE));
        schemaLine.addSchemaCell(new CsvSchemaCell("door", CellType.CHARACTER));
        schemaLine.addSchemaCell(new CsvSchemaCell("address.street", CellType.STRING));
        schemaLine.addSchemaCell(new CsvSchemaCell("address.town", CellType.STRING));
        schemaLine.addSchemaCell(new CsvSchemaCell("workAddress.street", CellType.STRING));
        schemaLine.addSchemaCell(new CsvSchemaCell("workAddress.town", CellType.STRING));
        schemaLine.addSchemaCell(new CsvSchemaCell("address.subAddress.street", CellType.STRING));
        schemaLine.addSchemaCell(new CsvSchemaCell("address.subAddress.town", CellType.STRING));
        schema.addSchemaLine(schemaLine);
        return schema;
    }

    public static BeanMap makeBeanMap() {
        return BeanMap.ofSchema(makeOutputSchema());
    }

}