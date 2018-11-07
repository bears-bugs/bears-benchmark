/**
 * 
 */
package org.jsapar.parse.fixed;

import org.jsapar.error.ErrorEventListener;
import org.jsapar.parse.line.ValidationHandler;
import org.jsapar.parse.text.TextParseConfig;
import org.jsapar.parse.text.TextSchemaParser;
import org.jsapar.schema.FixedWidthSchema;

import java.io.IOException;

/**
 * Abstract base class for fixed width text parser based on schema.
 */
public abstract class FixedWidthParser implements TextSchemaParser {
    private FixedWidthSchema schema;
    private TextParseConfig  config;
    private ValidationHandler validationHandler = new ValidationHandler();


    public FixedWidthParser(FixedWidthSchema schema, TextParseConfig config) {
        this.schema = schema;
        this.config = config;
    }

    protected void handleNoParser(long lineNumber, LineParserMatcherResult result, ErrorEventListener errorEventListener) throws IOException {

        // Check if EOF
        if (result == LineParserMatcherResult.NOT_MATCHING)
            this.validationHandler
                    .lineValidation(this, lineNumber, "No schema line could be used to parse line number " + lineNumber,config.getOnUndefinedLineType(), errorEventListener);
    }

    protected FixedWidthSchema getSchema() {
        return schema;
    }


    public TextParseConfig getConfig() {
        return config;
    }

    public void setConfig(TextParseConfig config) {
        this.config = config;
    }

    protected ValidationHandler getValidationHandler() {
        return validationHandler;
    }
}
