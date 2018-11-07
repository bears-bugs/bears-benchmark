package org.jsapar.compose;

import org.jsapar.compose.csv.CsvComposer;
import org.jsapar.compose.fixed.FixedWidthComposer;
import org.jsapar.schema.CsvSchema;
import org.jsapar.schema.FixedWidthSchema;
import org.jsapar.schema.Schema;

import java.io.Writer;

/**
 * Factory for creating schema composer based on schema.
 * Created by stejon0 on 2016-01-24.
 */
public class TextComposerFactory implements ComposerFactory {

    /**
     * Creates {@link SchemaComposer} based on the schema.
     * @param schema The schema to use while composing
     * @param writer The writer to write output to.
     * @return A newly created {@link SchemaComposer}
     * @throws IllegalArgumentException In case the schema is not of any type that is handled by this class.
     */
    public SchemaComposer makeComposer(Schema schema, Writer writer)  {
        if(schema instanceof CsvSchema){
            return new CsvComposer(writer, (CsvSchema)schema);
        }
        if(schema instanceof FixedWidthSchema){
            return new FixedWidthComposer(writer, (FixedWidthSchema)schema);
        }

        throw new IllegalArgumentException("Unknown schema type. Unable to create parser class for it.");
    }

}
