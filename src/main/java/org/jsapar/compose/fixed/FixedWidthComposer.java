package org.jsapar.compose.fixed;

import org.jsapar.compose.AbstractSchemaComposer;
import org.jsapar.compose.SchemaComposer;
import org.jsapar.schema.FixedWidthSchema;
import org.jsapar.schema.FixedWidthSchemaLine;

import java.io.Writer;

/**
 * Composes fixed width output based on schema.
 * Created by stejon0 on 2016-01-31.
 */
public class FixedWidthComposer extends AbstractSchemaComposer implements SchemaComposer {

    public FixedWidthComposer(Writer writer, FixedWidthSchema schema) {
        super(writer, schema, schemaLine -> new FixedWidthLineComposer(writer, (FixedWidthSchemaLine) schemaLine));
    }


}
