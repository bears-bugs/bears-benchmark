package org.jsapar.parse.fixed;

import org.jsapar.parse.text.TextParseConfig;
import org.jsapar.schema.FixedWidthSchema;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Creates fixed width line parsers based on schema.
 */
class FWLineParserFactory {
    private List<FWLineParserMatcher> lineParserMatchers;
    private LineParserMatcherResult lastResult;

    FWLineParserFactory(FixedWidthSchema schema, TextParseConfig config) {
        lineParserMatchers = schema.stream()
                .map(schemaLine -> new FWLineParserMatcher(schemaLine, config)).collect(Collectors.toList());
    }

    /**
     * @param reader A buffered reader to read input from
     * @return A {@link FixedWidthLineParser} that can be used or null if no line parser could be found. When returning
     * null, you can call {@link #getLastResult()} to get the reason for failure to return a line parser.
     * @throws IOException
     */
    FixedWidthLineParser makeLineParser(BufferedReader reader) throws IOException {
        if(lineParserMatchers.isEmpty())
            return null;
        Iterator<FWLineParserMatcher> iter = lineParserMatchers.iterator();
        boolean first = true;
        boolean eof = true;
        while(iter.hasNext()){
            FWLineParserMatcher currentMatcher = iter.next();
            LineParserMatcherResult lineParserResult = currentMatcher.testLineParserIfMatching(reader);
            if(lineParserResult == LineParserMatcherResult.SUCCESS) {
                if(!currentMatcher.isOccursLeft())
                    // No longer needed
                    iter.remove();
                else if (!first){
                    // Move current matching line first in list so that it is tested first next time.
                    iter.remove();
                    lineParserMatchers.add(0, currentMatcher);
                }
                lastResult = LineParserMatcherResult.SUCCESS;
                return currentMatcher.getLineParser();
            }
            else if(lineParserResult == LineParserMatcherResult.NO_OCCURS)
                iter.remove();

            if(lineParserResult != LineParserMatcherResult.EOF){
                eof = false;
            }
            first = false;
        }
        if(eof)
            lastResult =  LineParserMatcherResult.EOF;
        else if(lineParserMatchers.isEmpty())
            lastResult = LineParserMatcherResult.NO_OCCURS;
        else
            lastResult = LineParserMatcherResult.NOT_MATCHING;
        return null;
    }

    boolean isEmpty() {
        return lineParserMatchers.isEmpty();
    }

    LineParserMatcherResult getLastResult() {
        return lastResult;
    }

}
