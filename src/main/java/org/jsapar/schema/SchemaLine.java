package org.jsapar.schema;

import org.jsapar.model.Line;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Abstract base class that describes the schema for a line. For instance if you want to ignore a header line you
 * can add a SchemaLine instance to your schema with occurs==1 and ignoreRead==true;
 *
 * @see Schema
 * @see SchemaCell
 */
public abstract class SchemaLine implements Cloneable {
    /**
     * Constant to be used in occurs attribute and that indicates that lines can occur infinite number of times.
     */
    private static final int OCCURS_INFINITE = Integer.MAX_VALUE;
    private static final String NOT_SET = "";

    /**
     * The number of times this type of line occurs in the corresponding input or output.
     *
     * @see #isOccursInfinitely()
     * @see #setOccursInfinitely()
     */
    private int occurs = OCCURS_INFINITE;

    /**
     * The type of the line. This line type will be part of each of the parsed  {@link Line} instances that was created
     * by using this instance.
     * <p>
     * When composing, the line type of the {@link Line} supplied to the composer will be used by the composer to determine
     * which {@link SchemaLine} instance to use for composing.
     */
    private String lineType = NOT_SET;

    /**
     * If set to true, this type of line will be read from the input but then ignored, thus it will not produce any line
     * parsed event.
     */
    private boolean ignoreRead = false;

    /**
     * If set to true, this type of line will not be written to the output.
     */
    private boolean ignoreWrite = false;

    /**
     * Creates a SchemaLine that occurs infinite number of times.
     */
    public SchemaLine() {
        this.occurs = OCCURS_INFINITE;
    }

    /**
     * Creates a SchemaLine that occurs supplied number of times.
     *
     * @param nOccurs The number of times that a line of this type occurs in the input or output text.
     */
    public SchemaLine(int nOccurs) {
        this.occurs = nOccurs;
    }

    /**
     * Creates a SchemaLine with the supplied line type and that occurs infinite number of times.
     *
     * @param lineType The name of the type of the line.
     */
    public SchemaLine(String lineType) {
        this.setLineType(lineType);
    }

    /**
     * Creates a SchemaLine with the supplied line type and occurs supplied number of times.
     *
     * @param lineType The line type of this schema line.
     * @param nOccurs  The number of times it should occur.
     */
    public SchemaLine(String lineType, int nOccurs) {
        this.setLineType(lineType);
        this.setOccurs(nOccurs);
    }

    public int getOccurs() {
        return occurs;
    }

    public void setOccurs(int occurs) {
        this.occurs = occurs;
    }

    /**
     * Sets the occurs attribute so that this type of line occurs until the end of the buffer.
     */
    public void setOccursInfinitely() {
        this.occurs = OCCURS_INFINITE;
    }

    /**
     * @return true if this line occurs to the end of the buffer, false otherwise.
     */
    public boolean isOccursInfinitely() {
        return this.occurs == OCCURS_INFINITE;
    }

    /**
     * Finds a schema cell with the specified name.
     *
     * @param cellName The name of the schema cell to find.
     * @return The schema cell with the supplied name or null if no such cell was found.
     */
    public abstract SchemaCell getSchemaCell(String cellName);


    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public boolean isIgnoreRead() {
        return ignoreRead;
    }

    public void setIgnoreRead(boolean ignoreRead) {
        this.ignoreRead = ignoreRead;
    }

    public boolean isIgnoreWrite() {
        return ignoreWrite;
    }

    public void setIgnoreWrite(boolean ignoreWrite) {
        this.ignoreWrite = ignoreWrite;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SchemaLine lineType=");
        sb.append(this.lineType);
        sb.append(" occurs=");
        if (isOccursInfinitely())
            sb.append("INFINITE");
        else
            sb.append(this.occurs);
        return sb.toString();
    }


    /**
     * @return Number of cells in a line
     */
    public abstract int size();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SchemaLine))
            return false;
        SchemaLine that = (SchemaLine) o;
        return Objects.equals(lineType, that.lineType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLineType());
    }

    @Override
    public SchemaLine clone() {
        try {
            return (SchemaLine) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Can never happen.", e);
        }
    }

    public abstract Stream<? extends SchemaCell> stream();

    public abstract Iterator<? extends SchemaCell> iterator();

    public abstract void forEach(Consumer<? super SchemaCell> consumer);

    public abstract Collection<? extends SchemaCell> getSchemaCells();
}
