package org.jsapar.schema;

import org.junit.Test;

import static org.junit.Assert.*;

public class CSVSchemaLineTest  {

    boolean foundError=false;
    
    @Test
    public final void testCSVSchemaLine()  {
        CsvSchemaLine schemaLine = new CsvSchemaLine();
        assertEquals("", schemaLine.getLineType());
    }

    @Test
    public final void testCSVSchemaLine_String()  {
        CsvSchemaLine schemaLine = new CsvSchemaLine("LineType");
        assertEquals("LineType", schemaLine.getLineType());
    }
    
    

    @Test
    public void testGetSchemaCell(){
        CsvSchemaLine schemaLine = new CsvSchemaLine(1);
        schemaLine.setCellSeparator(";-)");
        CsvSchemaCell cell1 = new CsvSchemaCell("First Name");
        schemaLine.addSchemaCell(cell1);
        schemaLine.addSchemaCell(new CsvSchemaCell("Last Name"));
        
        assertNull(schemaLine.getSchemaCell("Does not exist"));
        assertSame(cell1, schemaLine.getSchemaCell("First Name"));
        
    }
}
