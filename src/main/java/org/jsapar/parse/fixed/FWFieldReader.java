package org.jsapar.parse.fixed;

import org.jsapar.schema.FixedWidthSchemaCell;

import java.io.IOException;
import java.io.Reader;

public class FWFieldReader {
    private static final String EMPTY_STRING = "";

    /**
     * @param reader The reader to read from.
     * @return The string value of the cell read from the reader at the position pointed to by the offset. Null if end
     * of input stream was reached.
     * @throws IOException If there is a problem while reading the input reader.
     */
    String readToString(FixedWidthSchemaCell schemaCell, Reader reader, int offset) throws IOException {
        int nLength = schemaCell.getLength(); // The actual length

        char[] buffer = new char[nLength];
        int nRead = reader.read(buffer, offset, nLength);
        if (nRead <= 0) {
            if (nLength <= 0)
                return EMPTY_STRING; // It should be empty.
            else{
                return null; // EOF
            }
        }
        nLength = nRead;
        int readOffset = 0;
        char padCharacter = schemaCell.getPadCharacter();
        if(schemaCell.getAlignment() != FixedWidthSchemaCell.Alignment.LEFT) {
            while (readOffset < nLength && buffer[readOffset] == padCharacter) {
                readOffset++;
            }
        }
        if(schemaCell.getAlignment() != FixedWidthSchemaCell.Alignment.RIGHT) {
            while (nLength > readOffset && buffer[nLength - 1] == padCharacter) {
                nLength--;
            }
        }
        nLength -= readOffset;
        if(nLength == 0){
            if(padCharacter == '0' && schemaCell.getCellFormat().getCellType().isNumber())
                return String.valueOf(padCharacter);
            return EMPTY_STRING;
        }
        return new String(buffer, readOffset, nLength);
    }

}
