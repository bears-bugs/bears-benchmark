package org.jsapar.parse;

import org.jsapar.model.CellType;
import org.jsapar.schema.SchemaCellFormat;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class CellParseExceptionTest {

    @Test
    public void testGetters() throws Exception {
        SchemaCellFormat cellFormat = new SchemaCellFormat(CellType.STRING, "\\d+");
        CellParseException e = new CellParseException(42, "A", "a", cellFormat, "Some error");
        assertEquals(42, e.getLineNumber());
        assertEquals("A", e.getCellName());
        assertEquals("a", e.getCellValue());
        assertSame(cellFormat, e.getCellFormat());
        System.out.println(e.getMessage());
    }

}