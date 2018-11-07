package org.jsapar.schema;

import org.jsapar.model.CellType;

import java.io.IOException;
import java.io.Writer;

/**
 * Describes how a cell is represented for a fixed width schema.
 */
public class FixedWidthSchemaCell extends SchemaCell {


    /**
     * Describes how a cell is aligned within its allocated space.
     * 
     */
    public enum Alignment {

        /**
         * Content of the cell is left aligned and filled/truncated to the right to reach correct size.
         */
        LEFT{
            @Override
            public void fit(Writer writer, int length, String sValue) throws IOException {
                writer.write(sValue, 0, length);
            }

        },
        /**
         * Content of the cell is center aligned and filled/truncated to both left and right to reach correct size.
         */
        CENTER {
            @Override
            public void fit(Writer writer, int length, String sValue) throws IOException {
                writer.write(sValue, (sValue.length()-length)/2, length);
            }

        },
        /**
         * Content of the cell is right aligned and filled/truncated to the left to reach correct size.
         */
        RIGHT{
            @Override
            public void fit(Writer writer, int length, String sValue) throws IOException {
                writer.write(sValue, sValue.length() - length, length);
            }

        };


        /**
         * Fits supplied value to supplied length, cutting in the correct end.
         * @param writer The writer to write to
         * @param length The maximum number of characters to write.
         * @param sValue The value to write.
         * @throws IOException If there is an error writing characters
         */
        public abstract void fit(Writer writer, int length, String sValue) throws IOException;
        
    }

    /**
     * The length of the cell.
     */
    private int       length;

    /**
     * The alignment of the cell content within the allocated space. Default is Alignment.LEFT.
     */
    private Alignment alignment = Alignment.LEFT;

    private char padCharacter = ' ';

    /**
     * Creates a fixed with schema cell with specified name, length and alignment.
     * 
     * @param sName
     *            The name of the cell
     * @param nLength
     *            The length of the cell
     * @param alignment
     *            The alignment of the cell content within the allocated space
     * @param padCharacter
     *            The pad character to use to fill the cell.
     */
    public FixedWidthSchemaCell(String sName, int nLength, Alignment alignment, Character padCharacter) {
        this(sName, nLength);
        this.alignment = alignment;
        this.padCharacter = padCharacter;
    }

    /**
     * Creates a fixed with schema cell with specified name and length.
     * 
     * @param sName
     *            The name of the cell
     * @param nLength
     *            The length of the cell
     */
    public FixedWidthSchemaCell(String sName, int nLength) {
        super(sName);
        this.length = nLength;
    }

    /**
     * Creates a fixed with schema cell with specified name, length and format.
     * 
     * @param sName
     *            The name of the cell
     * @param nLength
     *            The length of the cell
     * @param cellFormat
     *            The format of the cell
     */
    public FixedWidthSchemaCell(String sName, int nLength, SchemaCellFormat cellFormat) {
        super(sName, cellFormat);
        this.length = nLength;
    }



    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length
     *            the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }


    /**
     * The alignment of the cell content within the allocated space. Default is Alignment.LEFT.
     * @return the alignment
     */
    public Alignment getAlignment() {
        return alignment;
    }

    /**
     * The alignment of the cell content within the allocated space. Default is Alignment.LEFT.
     * @param alignment
     *            the alignment to set
     */
    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    /**
     * Sets alignment for default value according to current cell type. For type INTEGER and DECIMAL, alignment is
     * RIGHT. For all other types, alignment is LEFT.
     */
    @SuppressWarnings("WeakerAccess")
    public void setDefaultAlignmentForType() {
        if(getCellFormat().getCellType() == CellType.INTEGER || getCellFormat().getCellType() == CellType.DECIMAL)
            this.alignment = Alignment.RIGHT;
        else
            this.alignment = Alignment.LEFT;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jsapar.schema.SchemaCell#clone()
     */
    public FixedWidthSchemaCell clone(){
        return (FixedWidthSchemaCell) super.clone();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsapar.schema.SchemaCell#toString()
     */
    @Override
    public String toString() {
        return super.toString() +
                " length=" +
                this.length +
                " alignment=" +
                this.alignment;
    }

    public char getPadCharacter() {
        return padCharacter;
    }

    public void setPadCharacter(char padCharacter) {
        this.padCharacter = padCharacter;
    }


}
