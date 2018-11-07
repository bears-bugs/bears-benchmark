package org.jsapar.parse.fixed;

import org.jsapar.error.ErrorEventListener;
import org.jsapar.model.Cell;
import org.jsapar.model.Line;
import org.jsapar.parse.line.LineDecoratorErrorEventListener;
import org.jsapar.parse.line.ValidationHandler;
import org.jsapar.parse.text.TextParseConfig;
import org.jsapar.schema.FixedWidthSchemaCell;
import org.jsapar.schema.FixedWidthSchemaLine;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Parses fixed width text source on line level.
 */
class FixedWidthLineParser {

    private static final String EMPTY_STRING = "";
    private FixedWidthSchemaLine lineSchema;
    private List<FixedWidthCellParser> cellParsers;
    private ValidationHandler    validationHandler = new ValidationHandler();
    private TextParseConfig config;
    private LineDecoratorErrorEventListener lineDecoratorErrorEventListener = new LineDecoratorErrorEventListener();

    FixedWidthLineParser(FixedWidthSchemaLine lineSchema, TextParseConfig config) {
        this.lineSchema = lineSchema;
        this.config = config;
        this.cellParsers = makeCellParsers(lineSchema);
    }

    private List<FixedWidthCellParser> makeCellParsers(FixedWidthSchemaLine lineSchema) {
        return lineSchema.stream().map(this::makeCellParser).collect(Collectors.toList());
    }

    private FixedWidthCellParser makeCellParser(FixedWidthSchemaCell fixedWidthSchemaCell) {
        return FixedWidthCellParser.ofSchemaCell(fixedWidthSchemaCell, Math.min(config.getMaxCellCacheSize(), lineSchema.getOccurs() - 1));
    }

    boolean isIgnoreRead(){
        return lineSchema.isIgnoreRead();
    }

    @SuppressWarnings("UnnecessaryContinue")
    public Line parse(Reader reader, long lineNumber, ErrorEventListener errorListener) throws IOException {
        Line line = new Line(lineSchema.getLineType(), lineSchema.getSchemaCells().size());
        line.setLineNumber(lineNumber);
        boolean setDefaultsOnly = false;
        boolean oneRead = false;
        boolean oneIgnored = false;
        boolean handleInsufficient = true;

        for (FixedWidthCellParser cellParser : cellParsers) {
            FixedWidthSchemaCell schemaCell = cellParser.getSchemaCell();
            if (setDefaultsOnly) {
                if (cellParser.isDefaultValue())
                    line.addCell(cellParser.makeDefaultCell());
                continue;
            } else if (schemaCell.isIgnoreRead()) {
                if (cellParser.isDefaultValue())
                    line.addCell(cellParser.makeDefaultCell());

                long nSkipped = reader.skip(schemaCell.getLength());
                if (nSkipped > 0 || schemaCell.getLength() == 0)
                    oneIgnored = true;

                if (nSkipped != schemaCell.getLength()) {
                    if (oneRead)
                        setDefaultsOnly = true;
                    continue;
                }
            } else {
                lineDecoratorErrorEventListener.initialize(errorListener, line);
                Cell cell = cellParser.parse(reader, lineDecoratorErrorEventListener);
                if (cell == null) {
                    if (oneRead) {
                        setDefaultsOnly = true;
                        if (cellParser.isDefaultValue()) {
                            cell = cellParser.parse(EMPTY_STRING, lineDecoratorErrorEventListener);
                            if(cell != null)
                                line.addCell(cell);
                        }
                        //noinspection ConstantConditions
                        if (handleInsufficient) {
                            if (!validationHandler
                                    .lineValidation(this, lineNumber, "Insufficient number of characters for line",
                                            config.getOnLineInsufficient(), errorListener)) {
                                return null;
                            }
                            handleInsufficient = false;
                        }
                    }
                    continue;
                }

                oneRead = true;
                line.addCell(cell);
            }
        }
        if (line.size() <= 0 && !oneIgnored)
            return null;

        return line;
    }

}
