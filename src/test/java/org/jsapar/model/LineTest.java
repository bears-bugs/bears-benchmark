package org.jsapar.model;

import org.jsapar.parse.CellParseException;
import org.jsapar.schema.SchemaCellFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LineTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testLine() {
        Line line = new Line("");
        assertEquals(0, line.size());
        assertEquals("", line.getLineType());
    }

    @Test
    public void testLineInt() {
        Line line = new Line("", 3);
        assertEquals(0, line.size());
        assertEquals("", line.getLineType());
    }

    @Test
    public void testLineString() {
        Line line = new Line("Shoe");
        assertEquals(0, line.size());
        assertEquals("Shoe", line.getLineType());
    }

    @Test
    public void testLineStringInt() {
        Line line = new Line("Shoe", 3);
        assertEquals(0, line.size());
        assertEquals("Shoe", line.getLineType());
    }

    @Test
    public void testGetCells() {
        Line line = makeTestLine();
        java.util.List<Cell> cells = line.getCells();
        assertEquals(2, cells.size());
        assertEquals("Svensson", cells.get(1).getStringValue());
    }

    @Test
    public void testGetCellIterator() {
        Line line = new Line("TestLine");
        line.addCell(new StringCell("FirstName", "Nils"));
        java.util.Iterator<Cell> i = line.iterator();
        assertNotNull(i);
    }

    @Test
    public void testAddCellCell() {
        Line line = makeTestLine();
        assertEquals("Nils", LineUtils.getStringCellValue(line, "FirstName"));
        assertEquals("Svensson", LineUtils.getStringCellValue(line, "LastName"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddCell_twice() {
        Line line = new Line("TestLine");
        line.addCell(new StringCell("FirstName", "Nils"));
        line.addCell(new StringCell("FirstName", "Svensson"));
        fail("Should throw exception for duplicate cell names.");
    }


    @Test
    public void testReplaceCell() {
        Line line = makeTestLine();

        line.putCell(new StringCell("FirstName", "Sven"));
        assertEquals(2, line.size());
        assertEquals("Sven", LineUtils.getStringCellValue(line, "FirstName"));
        assertEquals("Svensson", LineUtils.getStringCellValue(line, "LastName"));
    }


    @Test
    public void testGetCellString() {
        Line line = makeTestLine();
        assertEquals("Nils", LineUtils.getStringCellValue(line, "FirstName"));
    }


    @Test
    public void testGetNumberOfCells() {
        Line line = makeTestLine();
        assertEquals(2, line.size());
    }

    @Test
    public void testGetLineType() {
        Line line = new Line("TestLine");
        assertEquals("TestLine", line.getLineType());
    }

    /**
     *
     */
    @Test
    public void testRemoveCell() {
        Line line = makeTestLine();

        assertEquals("Nils", line.removeCell("FirstName").map(Cell::getStringValue).orElse(""));
        assertEquals(1, line.size());
        assertEquals("Svensson", LineUtils.getStringCellValue(line, "LastName"));
    }

    private Line makeTestLine() {
        Line line = new Line("TestLine");
        line.addCell(new StringCell("FirstName", "Nils"));
        line.addCell(new StringCell("LastName", "Svensson"));
        return line;
    }

    @Test
    public void testAddCellError() throws Exception {
        Line line = makeTestLine();
        assertFalse(line.hasCellErrors());
        assertEquals(0, line.getCellErrors().size());
        CellParseException theError = new CellParseException(17, "FirstName", "some value",
                new SchemaCellFormat(CellType.STRING), "Testing error");
        line.addCellError(theError);
        assertTrue(line.hasCellErrors());
        assertEquals(1, line.getCellErrors().size());
        assertSame(theError, line.getCellError("FirstName").orElseThrow(()-> new AssertionError("fail")));
        assertFalse(line.getCellError("LastName").isPresent());
    }


    @Test
    public void testIsCellOfType() throws Exception {
        Line line = new Line("TestLine");
        line.addCell(new StringCell("FirstName", "Nils"));
        assertTrue(line.containsNonEmptyCell("FirstName", CellType.STRING));
        assertFalse(line.containsNonEmptyCell("FirstName", CellType.INTEGER));
        assertFalse(line.containsNonEmptyCell("LastName", CellType.STRING));
    }

    @Test
    public void testGetNonEmptyCell() {
        Line line = new Line("TestLine");
        line.addCell(new StringCell("FirstName", "Nils"));
        line.addCell(StringCell.emptyOf("LastName"));

        assertEquals("Nils", line.getNonEmptyCell("FirstName").map(Cell::getStringValue).orElse(""));
        assertFalse(line.getNonEmptyCell("LastName").isPresent());
        assertFalse(line.getNonEmptyCell("DoesNotExist").isPresent());
    }

    @Test
    public void testGetExistingCell() {
        Line line = new Line("TestLine");
        line.addCell(new StringCell("FirstName", "Nils"));
        line.addCell(StringCell.emptyOf("LastName"));

        assertEquals("Nils", line.getExistingCell("FirstName").getStringValue());
        assertEquals("", line.getExistingCell("LastName").getStringValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetExistingCell_nonExisting() {
        Line line = new Line("TestLine");

        line.getExistingCell("NonExisting");
        fail("Should throw exception");
    }

}
