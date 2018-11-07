package org.jsapar.schema;

import org.junit.Test;

import static org.junit.Assert.*;

public class FixedWidthSchemaTest {

    @Test
    public final void testClone() throws CloneNotSupportedException {
        FixedWidthSchema schema = new FixedWidthSchema();
        schema.setLineSeparator("");
        FixedWidthSchemaLine schemaLine = new FixedWidthSchemaLine(2);
        schemaLine.setLineType("Joho");

        schema.addSchemaLine(schemaLine);

        FixedWidthSchema theClone = schema.clone();

        assertEquals(schema.getLineSeparator(), theClone.getLineSeparator());

        // Does not clone strings values yet. Might do that in the future.
        assertTrue(schema.getLineSeparator() == theClone.getLineSeparator());

        assertEquals(schema.getSchemaLines().iterator().next().getLineType(), theClone.getSchemaLines().iterator().next()
                .getLineType());
        assertFalse(schema.getSchemaLines().iterator().next() == theClone.getSchemaLines().iterator().next());
    }

}
