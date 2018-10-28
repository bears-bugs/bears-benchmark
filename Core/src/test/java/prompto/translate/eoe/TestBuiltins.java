package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestBuiltins extends BaseEParserTest {

	@Test
	public void testBooleanText() throws Exception {
		compareResourceEOE("builtins/booleanText.pec");
	}

	@Test
	public void testCategoryText() throws Exception {
		compareResourceEOE("builtins/categoryText.pec");
	}

	@Test
	public void testCharCodePoint() throws Exception {
		compareResourceEOE("builtins/charCodePoint.pec");
	}

	@Test
	public void testCharText() throws Exception {
		compareResourceEOE("builtins/charText.pec");
	}

	@Test
	public void testCursorToList() throws Exception {
		compareResourceEOE("builtins/cursorToList.pec");
	}

	@Test
	public void testDateDayOfMonth() throws Exception {
		compareResourceEOE("builtins/dateDayOfMonth.pec");
	}

	@Test
	public void testDateDayOfYear() throws Exception {
		compareResourceEOE("builtins/dateDayOfYear.pec");
	}

	@Test
	public void testDateMonth() throws Exception {
		compareResourceEOE("builtins/dateMonth.pec");
	}

	@Test
	public void testDateText() throws Exception {
		compareResourceEOE("builtins/dateText.pec");
	}

	@Test
	public void testDateTimeDayOfMonth() throws Exception {
		compareResourceEOE("builtins/dateTimeDayOfMonth.pec");
	}

	@Test
	public void testDateTimeDayOfYear() throws Exception {
		compareResourceEOE("builtins/dateTimeDayOfYear.pec");
	}

	@Test
	public void testDateTimeHour() throws Exception {
		compareResourceEOE("builtins/dateTimeHour.pec");
	}

	@Test
	public void testDateTimeMilli() throws Exception {
		compareResourceEOE("builtins/dateTimeMilli.pec");
	}

	@Test
	public void testDateTimeMinute() throws Exception {
		compareResourceEOE("builtins/dateTimeMinute.pec");
	}

	@Test
	public void testDateTimeMonth() throws Exception {
		compareResourceEOE("builtins/dateTimeMonth.pec");
	}

	@Test
	public void testDateTimeSecond() throws Exception {
		compareResourceEOE("builtins/dateTimeSecond.pec");
	}

	@Test
	public void testDateTimeTZName() throws Exception {
		compareResourceEOE("builtins/dateTimeTZName.pec");
	}

	@Test
	public void testDateTimeTZOffset() throws Exception {
		compareResourceEOE("builtins/dateTimeTZOffset.pec");
	}

	@Test
	public void testDateTimeText() throws Exception {
		compareResourceEOE("builtins/dateTimeText.pec");
	}

	@Test
	public void testDateTimeYear() throws Exception {
		compareResourceEOE("builtins/dateTimeYear.pec");
	}

	@Test
	public void testDateYear() throws Exception {
		compareResourceEOE("builtins/dateYear.pec");
	}

	@Test
	public void testDecimalText() throws Exception {
		compareResourceEOE("builtins/decimalText.pec");
	}

	@Test
	public void testDictCount() throws Exception {
		compareResourceEOE("builtins/dictCount.pec");
	}

	@Test
	public void testDictKeys() throws Exception {
		compareResourceEOE("builtins/dictKeys.pec");
	}

	@Test
	public void testDictText() throws Exception {
		compareResourceEOE("builtins/dictText.pec");
	}

	@Test
	public void testDictValues() throws Exception {
		compareResourceEOE("builtins/dictValues.pec");
	}

	@Test
	public void testDocumentText() throws Exception {
		compareResourceEOE("builtins/documentText.pec");
	}

	@Test
	public void testEnumName() throws Exception {
		compareResourceEOE("builtins/enumName.pec");
	}

	@Test
	public void testEnumSymbols() throws Exception {
		compareResourceEOE("builtins/enumSymbols.pec");
	}

	@Test
	public void testEnumValue() throws Exception {
		compareResourceEOE("builtins/enumValue.pec");
	}

	@Test
	public void testIntegerFormat() throws Exception {
		compareResourceEOE("builtins/integerFormat.pec");
	}

	@Test
	public void testIntegerText() throws Exception {
		compareResourceEOE("builtins/integerText.pec");
	}

	@Test
	public void testListCount() throws Exception {
		compareResourceEOE("builtins/listCount.pec");
	}

	@Test
	public void testListText() throws Exception {
		compareResourceEOE("builtins/listText.pec");
	}

	@Test
	public void testPeriodText() throws Exception {
		compareResourceEOE("builtins/periodText.pec");
	}

	@Test
	public void testSetCount() throws Exception {
		compareResourceEOE("builtins/setCount.pec");
	}

	@Test
	public void testSetText() throws Exception {
		compareResourceEOE("builtins/setText.pec");
	}

	@Test
	public void testTextCapitalize() throws Exception {
		compareResourceEOE("builtins/textCapitalize.pec");
	}

	@Test
	public void testTextCount() throws Exception {
		compareResourceEOE("builtins/textCount.pec");
	}

	@Test
	public void testTextEndsWith() throws Exception {
		compareResourceEOE("builtins/textEndsWith.pec");
	}

	@Test
	public void testTextLowercase() throws Exception {
		compareResourceEOE("builtins/textLowercase.pec");
	}

	@Test
	public void testTextReplace() throws Exception {
		compareResourceEOE("builtins/textReplace.pec");
	}

	@Test
	public void testTextReplaceAll() throws Exception {
		compareResourceEOE("builtins/textReplaceAll.pec");
	}

	@Test
	public void testTextSplit() throws Exception {
		compareResourceEOE("builtins/textSplit.pec");
	}

	@Test
	public void testTextStartsWith() throws Exception {
		compareResourceEOE("builtins/textStartsWith.pec");
	}

	@Test
	public void testTextText() throws Exception {
		compareResourceEOE("builtins/textText.pec");
	}

	@Test
	public void testTextTrim() throws Exception {
		compareResourceEOE("builtins/textTrim.pec");
	}

	@Test
	public void testTextUppercase() throws Exception {
		compareResourceEOE("builtins/textUppercase.pec");
	}

	@Test
	public void testTimeHour() throws Exception {
		compareResourceEOE("builtins/timeHour.pec");
	}

	@Test
	public void testTimeMilli() throws Exception {
		compareResourceEOE("builtins/timeMilli.pec");
	}

	@Test
	public void testTimeMinute() throws Exception {
		compareResourceEOE("builtins/timeMinute.pec");
	}

	@Test
	public void testTimeSecond() throws Exception {
		compareResourceEOE("builtins/timeSecond.pec");
	}

	@Test
	public void testTimeText() throws Exception {
		compareResourceEOE("builtins/timeText.pec");
	}

	@Test
	public void testTupleCount() throws Exception {
		compareResourceEOE("builtins/tupleCount.pec");
	}

	@Test
	public void testTupleText() throws Exception {
		compareResourceEOE("builtins/tupleText.pec");
	}

	@Test
	public void testUuidText() throws Exception {
		compareResourceEOE("builtins/uuidText.pec");
	}

}

