package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestBuiltins extends BaseOParserTest {

	@Test
	public void testDateDayOfMonth() throws Exception {
		compareResourceOMO("builtins/dateDayOfMonth.poc");
	}

	@Test
	public void testDateDayOfYear() throws Exception {
		compareResourceOMO("builtins/dateDayOfYear.poc");
	}

	@Test
	public void testDateMonth() throws Exception {
		compareResourceOMO("builtins/dateMonth.poc");
	}

	@Test
	public void testDateTimeDayOfMonth() throws Exception {
		compareResourceOMO("builtins/dateTimeDayOfMonth.poc");
	}

	@Test
	public void testDateTimeDayOfYear() throws Exception {
		compareResourceOMO("builtins/dateTimeDayOfYear.poc");
	}

	@Test
	public void testDateTimeHour() throws Exception {
		compareResourceOMO("builtins/dateTimeHour.poc");
	}

	@Test
	public void testDateTimeMinute() throws Exception {
		compareResourceOMO("builtins/dateTimeMinute.poc");
	}

	@Test
	public void testDateTimeMonth() throws Exception {
		compareResourceOMO("builtins/dateTimeMonth.poc");
	}

	@Test
	public void testDateTimeSecond() throws Exception {
		compareResourceOMO("builtins/dateTimeSecond.poc");
	}

	@Test
	public void testDateTimeTZName() throws Exception {
		compareResourceOMO("builtins/dateTimeTZName.poc");
	}

	@Test
	public void testDateTimeTZOffset() throws Exception {
		compareResourceOMO("builtins/dateTimeTZOffset.poc");
	}

	@Test
	public void testDateTimeYear() throws Exception {
		compareResourceOMO("builtins/dateTimeYear.poc");
	}

	@Test
	public void testDateYear() throws Exception {
		compareResourceOMO("builtins/dateYear.poc");
	}

	@Test
	public void testDictCount() throws Exception {
		compareResourceOMO("builtins/dictCount.poc");
	}

	@Test
	public void testEnumName() throws Exception {
		compareResourceOMO("builtins/enumName.poc");
	}

	@Test
	public void testEnumSymbols() throws Exception {
		compareResourceOMO("builtins/enumSymbols.poc");
	}

	@Test
	public void testEnumValue() throws Exception {
		compareResourceOMO("builtins/enumValue.poc");
	}

	@Test
	public void testIntegerFormat() throws Exception {
		compareResourceOMO("builtins/integerFormat.poc");
	}

	@Test
	public void testListCount() throws Exception {
		compareResourceOMO("builtins/listCount.poc");
	}

	@Test
	public void testSetCount() throws Exception {
		compareResourceOMO("builtins/setCount.poc");
	}

	@Test
	public void testTextCapitalize() throws Exception {
		compareResourceOMO("builtins/textCapitalize.poc");
	}

	@Test
	public void testTextCount() throws Exception {
		compareResourceOMO("builtins/textCount.poc");
	}

	@Test
	public void testTextIndexOf() throws Exception {
		compareResourceOMO("builtins/textIndexOf.poc");
	}

	@Test
	public void testTextLowercase() throws Exception {
		compareResourceOMO("builtins/textLowercase.poc");
	}

	@Test
	public void testTextReplace() throws Exception {
		compareResourceOMO("builtins/textReplace.poc");
	}

	@Test
	public void testTextReplaceAll() throws Exception {
		compareResourceOMO("builtins/textReplaceAll.poc");
	}

	@Test
	public void testTextSplit() throws Exception {
		compareResourceOMO("builtins/textSplit.poc");
	}

	@Test
	public void testTextTrim() throws Exception {
		compareResourceOMO("builtins/textTrim.poc");
	}

	@Test
	public void testTextUppercase() throws Exception {
		compareResourceOMO("builtins/textUppercase.poc");
	}

	@Test
	public void testTimeHour() throws Exception {
		compareResourceOMO("builtins/timeHour.poc");
	}

	@Test
	public void testTimeMinute() throws Exception {
		compareResourceOMO("builtins/timeMinute.poc");
	}

	@Test
	public void testTimeSecond() throws Exception {
		compareResourceOMO("builtins/timeSecond.poc");
	}

	@Test
	public void testTupleCount() throws Exception {
		compareResourceOMO("builtins/tupleCount.poc");
	}

}

