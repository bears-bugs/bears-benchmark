package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestItem extends BaseEParserTest {

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
		checkInterpretedOutput("item/itemDict.pec");
	}

	@Test
	public void testCompiledItemDict() throws Exception {
		checkCompiledOutput("item/itemDict.pec");
	}

	@Test
	public void testTranspiledItemDict() throws Exception {
		checkTranspiledOutput("item/itemDict.pec");
	}

	@Test
	public void testInterpretedItemList() throws Exception {
		checkInterpretedOutput("item/itemList.pec");
	}

	@Test
	public void testCompiledItemList() throws Exception {
		checkCompiledOutput("item/itemList.pec");
	}

	@Test
	public void testTranspiledItemList() throws Exception {
		checkTranspiledOutput("item/itemList.pec");
	}

	@Test
	public void testInterpretedItemRange() throws Exception {
		checkInterpretedOutput("item/itemRange.pec");
	}

	@Test
	public void testCompiledItemRange() throws Exception {
		checkCompiledOutput("item/itemRange.pec");
	}

	@Test
	public void testTranspiledItemRange() throws Exception {
		checkTranspiledOutput("item/itemRange.pec");
	}

	@Test
	public void testInterpretedItemSet() throws Exception {
		checkInterpretedOutput("item/itemSet.pec");
	}

	@Test
	public void testCompiledItemSet() throws Exception {
		checkCompiledOutput("item/itemSet.pec");
	}

	@Test
	public void testTranspiledItemSet() throws Exception {
		checkTranspiledOutput("item/itemSet.pec");
	}

	@Test
	public void testInterpretedItemText() throws Exception {
		checkInterpretedOutput("item/itemText.pec");
	}

	@Test
	public void testCompiledItemText() throws Exception {
		checkCompiledOutput("item/itemText.pec");
	}

	@Test
	public void testTranspiledItemText() throws Exception {
		checkTranspiledOutput("item/itemText.pec");
	}

}

