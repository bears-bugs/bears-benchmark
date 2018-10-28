package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestBuiltins extends BaseOParserTest {

	@Test
	public void testDateDayOfMonth() throws Exception {
		compareResourceOEO("builtins/dateDayOfMonth.poc");
	}

	@Test
	public void testDateDayOfYear() throws Exception {
		compareResourceOEO("builtins/dateDayOfYear.poc");
	}

	@Test
	public void testDateMonth() throws Exception {
		compareResourceOEO("builtins/dateMonth.poc");
	}

	@Test
	public void testDateTimeDayOfMonth() throws Exception {
		compareResourceOEO("builtins/dateTimeDayOfMonth.poc");
	}

	@Test
	public void testDateTimeDayOfYear() throws Exception {
		compareResourceOEO("builtins/dateTimeDayOfYear.poc");
	}

	@Test
	public void testDateTimeHour() throws Exception {
		compareResourceOEO("builtins/dateTimeHour.poc");
	}

	@Test
	public void testDateTimeMinute() throws Exception {
		compareResourceOEO("builtins/dateTimeMinute.poc");
	}

	@Test
	public void testDateTimeMonth() throws Exception {
		compareResourceOEO("builtins/dateTimeMonth.poc");
	}

	@Test
	public void testDateTimeSecond() throws Exception {
		compareResourceOEO("builtins/dateTimeSecond.poc");
	}

	@Test
	public void testDateTimeTZName() throws Exception {
		compareResourceOEO("builtins/dateTimeTZName.poc");
	}

	@Test
	public void testDateTimeTZOffset() throws Exception {
		compareResourceOEO("builtins/dateTimeTZOffset.poc");
	}

	@Test
	public void testDateTimeYear() throws Exception {
		compareResourceOEO("builtins/dateTimeYear.poc");
	}

	@Test
	public void testDateYear() throws Exception {
		compareResourceOEO("builtins/dateYear.poc");
	}

	@Test
	public void testDictCount() throws Exception {
		compareResourceOEO("builtins/dictCount.poc");
	}

	@Test
	public void testEnumName() throws Exception {
		compareResourceOEO("builtins/enumName.poc");
	}

	@Test
	public void testEnumSymbols() throws Exception {
		compareResourceOEO("builtins/enumSymbols.poc");
	}

	@Test
	public void testEnumValue() throws Exception {
		compareResourceOEO("builtins/enumValue.poc");
	}

	@Test
	public void testIntegerFormat() throws Exception {
		compareResourceOEO("builtins/integerFormat.poc");
	}

	@Test
	public void testListCount() throws Exception {
		compareResourceOEO("builtins/listCount.poc");
	}

	@Test
	public void testSetCount() throws Exception {
		compareResourceOEO("builtins/setCount.poc");
	}

	@Test
	public void testTextCapitalize() throws Exception {
		compareResourceOEO("builtins/textCapitalize.poc");
	}

	@Test
	public void testTextCount() throws Exception {
		compareResourceOEO("builtins/textCount.poc");
	}

	@Test
	public void testTextIndexOf() throws Exception {
		compareResourceOEO("builtins/textIndexOf.poc");
	}

	@Test
	public void testTextLowercase() throws Exception {
		compareResourceOEO("builtins/textLowercase.poc");
	}

	@Test
	public void testTextReplace() throws Exception {
		compareResourceOEO("builtins/textReplace.poc");
	}

	@Test
	public void testTextReplaceAll() throws Exception {
		compareResourceOEO("builtins/textReplaceAll.poc");
	}

	@Test
	public void testTextSplit() throws Exception {
		compareResourceOEO("builtins/textSplit.poc");
	}

	@Test
	public void testTextTrim() throws Exception {
		compareResourceOEO("builtins/textTrim.poc");
	}

	@Test
	public void testTextUppercase() throws Exception {
		compareResourceOEO("builtins/textUppercase.poc");
	}

	@Test
	public void testTimeHour() throws Exception {
		compareResourceOEO("builtins/timeHour.poc");
	}

	@Test
	public void testTimeMinute() throws Exception {
		compareResourceOEO("builtins/timeMinute.poc");
	}

	@Test
	public void testTimeSecond() throws Exception {
		compareResourceOEO("builtins/timeSecond.poc");
	}

	@Test
	public void testTupleCount() throws Exception {
		compareResourceOEO("builtins/tupleCount.poc");
	}

}

