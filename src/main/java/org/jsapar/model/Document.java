package org.jsapar.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A document contains multiple lines where each line corresponds to a line of
 * the input buffer. Lines can be retrieved by index O(1), where first line has
 * index 0. Note that the class is not synchronized internally. If multiple
 * threads access the same instance, external synchronization is required.
 * 
 */
@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class Document implements Serializable, Iterable<Line> {

    /**
     *
     */
    private static final long serialVersionUID = 6098681751483565286L;

    private List<Line> lines;

    /**
     * Creates an empty document.
     */
    public Document() {
        this.lines = new ArrayList<>();
    }

    /**
     * Creates a document with an initial capacity to contain initialCapacity
     * lines.
     * 
     * @param initialCapacity Initial capacity.
     */
    public Document(int initialCapacity) {
        this.lines = new ArrayList<>(initialCapacity);
    }

    /**
     * @return The lines of this document.
     */
    public List<Line> getLines() {
        return lines;
    }

    /**
     * Returns the line at the specified index. First line has index 0.
     * @param index The index of the line to retrieve.
     * @return The line at the specified index. 
     * @throws IndexOutOfBoundsException
     *             - if the index is out of range <pre>{@code (index < 0 || index >= size())}</pre>
     */
    public Line getLine(int index) {
        return this.lines.get(index);
    }

    /**
     * Removes the first line of this document and returns it.
     * 
     * @return The line that was just removed.
     * @throws IndexOutOfBoundsException
     *             - if the document is empty, i.e. it has not lines.
     */
    public Line removeFirstLine() {
        return removeLineAt(0);
    }

    /**
     * Adds the given line at the end of the collection of lines within the document.
     * 
     * @param line
     *            The line to add.
     */
    public void addLine(Line line) {
        this.lines.add(line);
    }

    /**
     * Returns an iterator that will iterate all the lines of this document.
     * @return An iterator that will iterate all the lines of this document.
     */
    public Iterator<Line> iterator() {
        return this.lines.iterator();
    }

    /**
     * @return A stream of all lines within this document.
     */
    public Stream<Line> stream(){
        return lines.stream();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i=1;
        for(Line line : this.lines){
            sb.append("{Line ");
            sb.append(i++);
            sb.append(": ");
            sb.append(line);
            sb.append("}, ");
        }
        return sb.toString();
    }
    
    /**
     * @return True if there are no lines within this document, false otherwise.
     */
    public boolean isEmpty() {
        return lines.isEmpty();
    }

    /**
     * This implementation is quite slow if there are a lot of lines and the line type asked for is not among the top
     * lines. It will iterate through all lines in order to find supplied line type.
     * 
     * @param lineType
     *            The line type to test.
     * @return True if there is at least one line of the supplied line type, false otherwise.
     */
    public boolean containsLineType(String lineType) {
        return findFirstLineOfType(lineType).isPresent();
    }

    /**
     * This implementation is quite slow if there are a lot of lines and the line type asked for is not among the top
     * lines. It will iterate through all lines in order to find supplied line type.
     * 
     * @param lineType
     *            The line type to find first line of.
     * @return An optional line containing the first line in the document that has the supplied line type or empty if no
     * such line exist.
     */
    public Optional<Line> findFirstLineOfType(String lineType) {
        assert lineType != null;
        return lines.stream().filter(l->lineType.equals(l.getLineType())).findFirst();
    }
    
    /**
     * Finds all lines of a specified type.
     * @param lineType The type of the lines to find.
     * @return A list of all lines with a line type that equals supplied line type or an empty list if no such line
     *         exist within this document.
     */
    public List<Line> findLinesOfType(String lineType) {
        assert lineType != null;

        return lines.stream().filter(line -> lineType.equals(line.getLineType())).collect(Collectors.toList());
    }

    /**
     * @return The number of lines within this document.
     */
    public int size() {
        return lines.size();
    }

    /**
     * Removes supplied line from this document. Only if the supplied line instance is part of the document it will be
     * removed since equality will be tested upon same instance and not by using equals() method.
     * 
     * @param line The line to remove
     * @return true if line was found and removed from document. False otherwise.
     */
    public boolean removeLine(Line line) {
        assert line != null;
        Iterator<Line> i = lines.iterator();
        while (i.hasNext()) {
            if (i.next() == line) {
                i.remove();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Removes line at supplied index.
     * 
     * @param index The index of the line to remove.
     * @return The line that was removed.
     * @throws IndexOutOfBoundsException
     *             - if the index is out of range <pre>{@code (index < 0 || index >= size())}</pre>
     */
    public Line removeLineAt(int index){
        return lines.remove(index);
    }


}
