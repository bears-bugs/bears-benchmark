package org.jsapar.parse.cell;

import org.jsapar.error.RecordingErrorEventListener;
import org.jsapar.model.*;
import org.jsapar.parse.cell.CellParser;
import org.jsapar.schema.MatchingCellValueCondition;
import org.jsapar.schema.SchemaCell;
import org.jsapar.schema.SchemaException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.*;
import java.util.Locale;

import static org.junit.Assert.*;

public class CellParserTest {
    TestSchemaCell schemaCell;

    @Before
    public void before() throws ParseException {
        schemaCell = new TestSchemaCell("test");
    }
    /**
     * To be able to have a specific SchemaCell to test.
     *
     * @author stejon0
     *
     */
    private class TestSchemaCell extends SchemaCell {


        public TestSchemaCell(String name) {
            super(name);
        }

        public TestSchemaCell(String name, CellType type, String pattern, Locale locale) {
            super(name, type, pattern, locale);
        }
    }

    @Test
    public void testMakeCell_String() throws java.text.ParseException {
        CellParser cellParser = new CellParser<>(schemaCell, 0);

        Cell cell = cellParser.makeCell("the value");
        assertEquals("the value", cell.getStringValue());
    }

    @Test
    public void testMakeCell_DefaultString() throws java.text.ParseException {
        schemaCell.setDefaultValue("TheDefault");
        CellParser cellParser = new CellParser<>(schemaCell, 0);

        Cell cell = cellParser.makeCell("");
        assertEquals("TheDefault", cell.getStringValue());
    }

    @Test
    public void testMakeCell_missing_no_default() throws java.text.ParseException {
        CellParser cellParser = new CellParser<>(schemaCell, 0);

        Cell cell = cellParser.makeCell("");
        assertEquals("", cell.getStringValue());
    }

    @Test
    public void testMakeCell_DefaultValue() throws java.text.ParseException, SchemaException {
        schemaCell.setDefaultValue("TheDefault");
        CellParser cellParser = new CellParser<>(schemaCell, 0);

        Cell cell = cellParser.makeCell("");
        assertEquals("TheDefault", cell.getStringValue());
    }

    @Test
    public void testMakeCell_DefaultValue_float() throws SchemaException, java.text.ParseException {
        TestSchemaCell schemaCell = new TestSchemaCell("test",CellType.FLOAT, "#.00", new Locale("sv","SE"));
        schemaCell.setDefaultValue("123456,78901");
        CellParser cellParser = new CellParser<>(schemaCell, 0);

        Cell cell = cellParser.makeCell("");
        assertEquals(123456.78901, ((FloatCell)cell).getValue().doubleValue(), 0.0001);
    }

    @Test
    public void testMakeCell_empty_pattern() throws SchemaException, java.text.ParseException {
        TestSchemaCell schemaCell = new TestSchemaCell("test",CellType.FLOAT, "#.00", new Locale("sv","SE"));
        schemaCell.setEmptyCondition(new MatchingCellValueCondition("NULL"));
        CellParser cellParser = new CellParser<>(schemaCell, 0);

        Cell nonEmptyCell = cellParser.makeCell("1,25");
        assertEquals(1.25, ((FloatCell)nonEmptyCell).getValue().doubleValue(), 0.0001);

        Cell emptyCell = cellParser.makeCell("NULL");
        assertTrue(emptyCell instanceof EmptyCell);

    }

    @Test
    public void testMakeCell_empty_pattern_default() throws SchemaException, java.text.ParseException {
        TestSchemaCell schemaCell = new TestSchemaCell("test", CellType.FLOAT, "#.00", new Locale("sv","SE"));
        schemaCell.setEmptyPattern("NULL");
        schemaCell.setDefaultValue("123456,78901");
        CellParser cellParser = new CellParser<>(schemaCell, 0);

        Cell cell = cellParser.makeCell("NULL");
        assertEquals(123456.78901, ((FloatCell)cell).getValue().doubleValue(), 0.0001);
    }


    @Test
    public void testMakeCell_RegExp() throws SchemaException, java.text.ParseException {
        TestSchemaCell schemaCell = new TestSchemaCell("test");
        schemaCell.setCellFormat(CellType.STRING, "[A-Z]{3}[0-9]{0,3}de");
        CellParser cellParser = new CellParser<>(schemaCell, 0);

        Cell cell = cellParser.makeCell("ABC123de");
        assertEquals("ABC123de", cell.getStringValue());
    }

    @Test(expected=java.text.ParseException.class)
    public void testMakeCell_RegExp_fail() throws SchemaException, java.text.ParseException {
        TestSchemaCell schemaCell = new TestSchemaCell("test");
        schemaCell.setCellFormat(CellType.STRING, "[A-Z]{3}[0-9]{0,3}de");
        CellParser cellParser = new CellParser<>(schemaCell, 0);

        cellParser.makeCell("AB1C123de");
        fail("Should throw ParseException for invalid RegExp validation.");
    }

    @Test
    public void testMakeCell_CellTypeStringStringFormat() throws SchemaException,
            java.text.ParseException {
        Cell cell = CellParser.makeCell(CellType.STRING, "test", "the value", Locale.US);
        assertEquals("the value", cell.getStringValue());
    }


    @Test(expected = ParseException.class)
    public void testMakeCell_UnfinishedInteger() throws ParseException, SchemaException {
        CellParser.makeCell(CellType.INTEGER, "number", "123A45", Locale.getDefault());
        fail("Method should throw exception.");
    }


    @Test
    public void testMakeCell_Integer() throws java.text.ParseException {
        Cell cell;
        cell = CellParser.makeCell(CellType.INTEGER, "number", "12345", Locale.getDefault());
        assertEquals(IntegerCell.class, cell.getClass());
        assertEquals(12345, ((IntegerCell)cell).getValue().intValue());
    }

    @Test
    public void testMakeCell_Integer_DefaultValue() throws java.text.ParseException, SchemaException {
        SchemaCell schemaCell = new TestSchemaCell("A number");
        schemaCell.setCellFormat(CellType.INTEGER);
        schemaCell.setDefaultValue("42");
        CellParser cellParser = new CellParser<>(schemaCell, 0);
        Cell cell;
        cell = cellParser.makeCell("");
        assertEquals(IntegerCell.class, cell.getClass());
        assertEquals(42, ((IntegerCell)cell).getValue().intValue());
        assertEquals("A number", cell.getName());
    }

    @Test(expected = ParseException.class)
    public void testMakeCell_UnfinishedFloat() throws ParseException {
        Locale locale = Locale.UK;
        CellParser cellParser = new CellParser<>(schemaCell, 0);
        CellParser.makeCell(CellType.FLOAT, "number", "12.3A45", locale);
        fail("Method should throw exception.");
    }

    @Test
    public void testMakeCell_Float() throws SchemaException, java.text.ParseException {
        Cell cell;
        Locale locale = Locale.UK;
        CellParser cellParser = new CellParser<>(schemaCell, 0);
        cell = CellParser.makeCell(CellType.FLOAT, "number", "12.345", locale);
        assertEquals(12.345, cell.getValue());
    }

    @Test
    public void testMakeCell_Decimal_spaces() throws SchemaException, java.text.ParseException {
        Cell cell;
        Locale locale = new Locale("sv", "SE");
        cell = CellParser.makeCell(CellType.DECIMAL, "number", "12 345,66", locale);
        assertEquals(new BigDecimal("12345.66"), cell.getValue());
    }

    @Test
    public void testMakeCell_Float_spaces() throws SchemaException, java.text.ParseException {
        Cell cell;
        Locale locale = new Locale("sv", "SE");
        cell = CellParser.makeCell(CellType.FLOAT, "number", "12 345,66", locale);
        assertEquals(12345.66D, cell.getValue());
    }

    @Test
    public void testMakeCell_Int_spaces() throws SchemaException, java.text.ParseException {
        Cell cell;
        Locale locale = new Locale("sv", "SE");
        assertEquals(12345L, CellParser.makeCell(CellType.INTEGER, "number", "12 345", locale).getValue());
        assertEquals(12345L, CellParser.makeCell(CellType.INTEGER, "number", "12\u00A0345", locale).getValue());
    }
    @Test
    public void testMakeCell_LocalTime() throws SchemaException, java.text.ParseException {
        TestSchemaCell schemaCell = new TestSchemaCell("test",CellType.LOCAL_TIME, "HH:mm", new Locale("sv","SE"));
        schemaCell.setDefaultValue("00:00");
        CellParser cellParser = new CellParser<>(schemaCell, 0);

        Cell cell = cellParser.makeCell("15:45");
        assertEquals(LocalTime.of(15, 45), ((LocalTimeCell)cell).getValue());

        cell = cellParser.makeCell("");
        assertEquals(LocalTime.of(0, 0), ((LocalTimeCell)cell).getValue());
    }


    @Test
    public void testMakeCell_LocalDateTime() throws SchemaException, java.text.ParseException {
        TestSchemaCell schemaCell = new TestSchemaCell("test",CellType.LOCAL_DATE_TIME, "yyyy-MM-dd HH:mm", new Locale("sv","SE"));
        schemaCell.setDefaultValue("2000-01-01 00:00");
        CellParser cellParser = new CellParser<>(schemaCell, 0);

        Cell cell = cellParser.makeCell("2017-03-27 15:45");
        assertEquals(LocalDateTime.of(2017, Month.MARCH, 27, 15, 45), ((LocalDateTimeCell)cell).getValue());

        cell = cellParser.makeCell("");
        assertEquals(LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0), ((LocalDateTimeCell)cell).getValue());
    }

    @Test
    public void testMakeCell_LocalDate() throws SchemaException, java.text.ParseException {
        TestSchemaCell schemaCell = new TestSchemaCell("test",CellType.LOCAL_DATE, "yyyy-MM-dd", new Locale("sv","SE"));
        schemaCell.setDefaultValue("2000-01-01");
        CellParser cellParser = new CellParser<>(schemaCell, 0);

        Cell cell = cellParser.makeCell("2017-03-27");
        assertEquals(LocalDate.of(2017, Month.MARCH, 27), ((LocalDateCell)cell).getValue());

        cell = cellParser.makeCell("");
        assertEquals(LocalDate.of(2000, Month.JANUARY, 1), ((LocalDateCell)cell).getValue());
    }

    @Test
    public void testMakeCell_ZonedDateTime() throws SchemaException, java.text.ParseException {
        TestSchemaCell schemaCell = new TestSchemaCell("test",CellType.ZONED_DATE_TIME, "yyyy-MM-dd HH:mmX", new Locale("sv","SE"));
        schemaCell.setDefaultValue("2000-01-01 00:00+00");
        CellParser cellParser = new CellParser<>(schemaCell, 0);

        Cell cell = cellParser.makeCell("2017-03-27 15:45+02");
        assertEquals(ZonedDateTime.of(2017, 3, 27, 15, 45, 0, 0, ZoneId.of("+02:00")), ((ZonedDateTimeCell)cell).getValue());

        cell = cellParser.makeCell("");
        assertEquals(ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.of("+00:00")), ((ZonedDateTimeCell)cell).getValue());
    }

    /**
     *
     */
    @Test
    public void testMakeCell_Integer_RangeValid() throws java.text.ParseException {

        TestSchemaCell schemaCell = new TestSchemaCell("test");
        schemaCell.setCellFormat(CellType.INTEGER);
        schemaCell.setMinValue(new IntegerCell("test",0));
        schemaCell.setMaxValue(new IntegerCell("test",54321));

        CellParser cellParser = new CellParser<>(schemaCell, 0);
        Cell cell = cellParser.makeCell("12345");
        assertEquals(IntegerCell.class, cell.getClass());
        assertEquals(12345, ((IntegerCell)cell).getValue().intValue());

    }

    /**
     *
     *
     */
    @Test
    public void testParse_Integer_MinRangeNotValid() throws java.text.ParseException {

        TestSchemaCell schemaCell = new TestSchemaCell("test");
        schemaCell.setCellFormat(CellType.INTEGER);
        schemaCell.setMinValue(new IntegerCell("test",54321));
        schemaCell.setMaxValue(new IntegerCell("test",54322));
        CellParser cellParser = new CellParser<>(schemaCell, 0);
        RecordingErrorEventListener errorListener = new RecordingErrorEventListener();
        cellParser.parse("12345", errorListener);
        assertEquals(1, errorListener.getErrors().size());
        assertEquals("Cell='test' Value='12345' Expected: CellType=INTEGER - The value is below minimum range limit (54321).", errorListener.getErrors().get(0).getMessage());
    }

    /**
     *
     *
     */
    @Test
    public void testMakeCell_Integer_MaxRangeNotValid() throws java.text.ParseException {

        TestSchemaCell schemaCell = new TestSchemaCell("test");
        schemaCell.setCellFormat(CellType.INTEGER);
        schemaCell.setMinValue(new IntegerCell("test",0));
        schemaCell.setMaxValue(new IntegerCell("test",100));
        CellParser cellParser = new CellParser<>(schemaCell, 0);
        RecordingErrorEventListener errorListener = new RecordingErrorEventListener();
        cellParser.parse("12345", errorListener);
        assertEquals(1, errorListener.getErrors().size());
        assertEquals("Cell='test' Value='12345' Expected: CellType=INTEGER - The value is above maximum range limit (100).", errorListener.getErrors().get(0).getMessage());
    }



}