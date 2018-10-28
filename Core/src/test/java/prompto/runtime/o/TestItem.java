package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestItem extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedItemDict() throws Exception {
		checkInterpretedOutput("item/itemDict.poc");
	}

	@Test
	public void testCompiledItemDict() throws Exception {
		checkCompiledOutput("item/itemDict.poc");
	}

	@Test
	public void testTranspiledItemDict() throws Exception {
		checkTranspiledOutput("item/itemDict.poc");
	}

	@Test
	public void testInterpretedItemList() throws Exception {
		checkInterpretedOutput("item/itemList.poc");
	}

	@Test
	public void testCompiledItemList() throws Exception {
		checkCompiledOutput("item/itemList.poc");
	}

	@Test
	public void testTranspiledItemList() throws Exception {
		checkTranspiledOutput("item/itemList.poc");
	}

	@Test
	public void testInterpretedItemRange() throws Exception {
		checkInterpretedOutput("item/itemRange.poc");
	}

	@Test
	public void testCompiledItemRange() throws Exception {
		checkCompiledOutput("item/itemRange.poc");
	}

	@Test
	public void testTranspiledItemRange() throws Exception {
		checkTranspiledOutput("item/itemRange.poc");
	}

	@Test
	public void testInterpretedItemSet() throws Exception {
		checkInterpretedOutput("item/itemSet.poc");
	}

	@Test
	public void testCompiledItemSet() throws Exception {
		checkCompiledOutput("item/itemSet.poc");
	}

	@Test
	public void testTranspiledItemSet() throws Exception {
		checkTranspiledOutput("item/itemSet.poc");
	}

	@Test
	public void testInterpretedItemText() throws Exception {
		checkInterpretedOutput("item/itemText.poc");
	}

	@Test
	public void testCompiledItemText() throws Exception {
		checkCompiledOutput("item/itemText.poc");
	}

	@Test
	public void testTranspiledItemText() throws Exception {
		checkTranspiledOutput("item/itemText.poc");
	}

}

