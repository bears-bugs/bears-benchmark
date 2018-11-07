package org.jsapar.parse.csv;

import org.jsapar.error.ErrorEventListener;
import org.jsapar.parse.LineEventListener;
import org.jsapar.parse.line.ValidationHandler;
import org.jsapar.parse.text.TextParseConfig;
import org.jsapar.parse.text.TextSchemaParser;
import org.jsapar.schema.CsvSchema;

import java.io.IOException;
import java.io.Reader;

/**
 * Internal class for parsing CSV input.
 */
public class CsvParser implements TextSchemaParser {
    
    private CsvLineReader        lineReader;
    private CsvSchema            schema;
    private CsvLineParserFactory lineParserFactory;
    private TextParseConfig      parseConfig;
    private ValidationHandler validationHandler = new ValidationHandler();

    CsvParser(Reader reader, CsvSchema schema) {
        this(reader, schema, new TextParseConfig());
    }


    public CsvParser(Reader reader, CsvSchema schema, TextParseConfig parseConfig) {
        this.parseConfig = parseConfig;
        lineReader = new CsvLineReader(schema.getLineSeparator(), reader);
        this.schema = schema;
        this.lineParserFactory = new CsvLineParserFactory(schema, parseConfig);
    }
    

    @Override
    public long parse(LineEventListener listener, ErrorEventListener errorListener) throws IOException {
        if(schema.isEmpty()) {
            return 0;
        }
        long lineNumber = 0;
        while(true){
            CsvLineParser lineParser = lineParserFactory.makeLineParser(lineReader);
            if(lineParser == null) {
                if(lineParserFactory.isEmpty())
                    return lineNumber; // No more parsers. We should not read any more. Leave rest of input as is.
                if(lineReader.eofReached())
                    return lineNumber;
                handleNoParser(lineReader, errorListener);
                continue;
            }
            if(!lineParser.parse(lineReader, listener, errorListener))
                return lineNumber;
            if(!lineReader.lastLineWasEmpty())
                lineNumber++;
        }

    }

    private void handleNoParser(CsvLineReader lineReader, ErrorEventListener errorEventListener) throws IOException {
        if (lineReader.lastLineWasEmpty())
            return;
        validationHandler.lineValidation(this, lineReader.currentLineNumber(), "No schema line could be used to parse line number " + lineReader.currentLineNumber(), parseConfig.getOnUndefinedLineType(), errorEventListener);
    }


}
