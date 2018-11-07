package org.jsapar.schema;

import org.jsapar.parse.fixed.FixedWidthParserFlat;
import org.jsapar.parse.fixed.FixedWidthParserLinesSeparated;
import org.jsapar.parse.text.TextParseConfig;
import org.jsapar.parse.text.TextSchemaParser;

import java.io.Reader;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Defines a schema for a fixed position buffer. Each cell is defined by a fixed number of
 * characters. Each line is separated by the line separator defined in the base class {@link Schema}
 * .
 * <p>
 * If the end of line is reached before all cells are parsed the remaining cells will not be set.
 * <p>
 * If there are remaining characters when the end of line is reached, those characters will be
 * omitted.
 * <p>
 * If the line separator is an empty string, the lines will be separated by the sum of the length of
 * the cells within the schema.
 */
public class FixedWidthSchema extends Schema {

    /**
     * A list of fixed with schema lines which builds up this schema.
     */
    private LinkedHashMap<String, FixedWidthSchemaLine> schemaLines = new LinkedHashMap<>();


    /**
     * @param schemaLine the schemaLines to set
     */
    public void addSchemaLine(FixedWidthSchemaLine schemaLine) {
        this.schemaLines.put(schemaLine.getLineType(), schemaLine);
    }

    @Override
    public boolean isEmpty() {
        return this.schemaLines.isEmpty();
    }

    /*
         * (non-Javadoc)
         *
         * @see org.jsapar.schema.Schema#clone()
         */
    @Override
    public FixedWidthSchema clone() {
        FixedWidthSchema schema = (FixedWidthSchema) super.clone();

        schema.schemaLines = new LinkedHashMap<>();
        this.stream().map(FixedWidthSchemaLine::clone).forEach(schema::addSchemaLine);
        return schema;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsapar.schema.Schema#toString()
     */
    @Override
    public String toString() {
        return super.toString() + " schemaLines=" + this.schemaLines;
    }

    @Override
    public Collection<FixedWidthSchemaLine> getSchemaLines() {
        return this.schemaLines.values();
    }

    @Override
    public Optional<FixedWidthSchemaLine> getSchemaLine(String lineType) {
        return Optional.ofNullable(schemaLines.get(lineType));
    }

    @Override
    public int size() {
        return this.schemaLines.size();
    }

    @Override
    public Stream<FixedWidthSchemaLine> stream() {
        return this.schemaLines.values().stream();
    }

    @Override
    public Iterator<FixedWidthSchemaLine> iterator() {
        return schemaLines.values().iterator();
    }

    @Override
    public TextSchemaParser makeSchemaParser(Reader reader, TextParseConfig parseConfig) {
        if (getLineSeparator().isEmpty())
            return new FixedWidthParserFlat(reader, this, parseConfig);
        else
            return new FixedWidthParserLinesSeparated(reader, this, parseConfig);
    }

    /**
     * Adds a schema cell at the end of each line to make sure that the total length generated/parsed will always be at
     * least the minLength of the line. The name of the added cell will be _fillToMinLength_. The length of the added
     * cell will be the difference between all minLength of the line and the cells added so far. Adding more schema
     * cells after calling this method will add those cells after the filler cell which will probably lead to unexpected
     * behavior.
     */
    void addFillerCellsToReachLineMinLength() {
        stream().forEach(FixedWidthSchemaLine::addFillerCellToReachMinLength);
    }

}
