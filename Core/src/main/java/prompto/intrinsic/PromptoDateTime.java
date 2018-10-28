package prompto.intrinsic;

import org.joda.time.DateTime;

/* DateTime is final so can't just extend it */
public class PromptoDateTime implements Comparable<PromptoDateTime> {

	public static PromptoDateTime parse(String text) {
		return new PromptoDateTime(DateTime.parse(text));
	}
	
	public static PromptoDateTime now() {
		return new PromptoDateTime(DateTime.now());
	}
	
	private DateTime wrapped;

	public PromptoDateTime(DateTime wrapped) {
		this.wrapped = wrapped;
	}
	
	public PromptoDateTime(int year, int month, int day, int hour, int minute, int second, int millis) {
		this.wrapped = new DateTime(year, month, day, hour, minute, second, millis);
	}
	
	@Override
	public int hashCode() {
		return wrapped.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof PromptoDateTime && wrapped.equals(((PromptoDateTime)obj).wrapped);
	}

	@Override
	public String toString() {
		return wrapped.toString();
	}
	
	@Override
	public int compareTo(PromptoDateTime o) {
		return wrapped.compareTo(o.wrapped);
	}

	public Long getYear() {
		return (long)wrapped.getYear();
	}

	public Long getMonth() {
		return (long)wrapped.getMonthOfYear();
	}

	public Long getDayOfMonth() {
		return (long)wrapped.getDayOfMonth();
	}

	public Long getDayOfYear() {
		return (long)wrapped.getDayOfYear();
	}
	
	public Long getHour() {
		return (long)wrapped.getHourOfDay();
	}

	public Long getMinute() {
		return (long)wrapped.getMinuteOfHour();
	}

	public Long getSecond() {
		return (long)wrapped.getSecondOfMinute();
	}

	public Long getMillisecond() {
		return (long)wrapped.getMillisOfSecond();
	}
	
	public Long getTzOffset() {
		return (long)wrapped.getZone().toTimeZone().getRawOffset() / 1000;
	}

	public long getNativeYear() {
		return wrapped.getYear();
	}

	public long getNativeMonth() {
		return wrapped.getMonthOfYear();
	}

	public long getNativeDayOfMonth() {
		return wrapped.getDayOfMonth();
	}

	public long getNativeDayOfYear() {
		return wrapped.getDayOfYear();
	}
	
	public long getNativeHour() {
		return wrapped.getHourOfDay();
	}

	public long getNativeMinute() {
		return wrapped.getMinuteOfHour();
	}

	public long getNativeSecond() {
		return wrapped.getSecondOfMinute();
	}

	public long getNativeMillis() {
		return wrapped.getMillisOfSecond();
	}
	
	public long getNativeTzOffset() {
		return wrapped.getZone().toTimeZone().getRawOffset() / 1000;
	}

	public String getTzName() {
		return wrapped.getZone().toTimeZone().getID();
	}


	public PromptoDateTime plusDays(long count) {
		return new PromptoDateTime(wrapped.plusDays((int)count));
	}

	public boolean isAfter(PromptoDateTime value) {
		return wrapped.isAfter(value.wrapped);
	}

	public PromptoDateTime plus(PromptoPeriod value) {
		return new PromptoDateTime(wrapped.plus(value.wrapped));
	}

	public PromptoDateTime minus(PromptoPeriod value) {
		return new PromptoDateTime(wrapped.minus(value.wrapped));
	}

	public PromptoPeriod minus(PromptoDateTime value) {
		long millis = toJavaTime();
		millis -= value.toJavaTime();
		PromptoPeriod result = new PromptoPeriod(millis);
		if (result.getNativeHours() > 24)
			result = new PromptoPeriod(0, 0, 0, 
					result.getNativeHours() / 24, 
					result.getNativeHours() % 24, 
					result.getNativeMinutes(), 
					result.getNativeSeconds(), 
					result.getNativeMillis());
		return result;
	}

	public String format(String formatString) {
		return wrapped.toString(formatString);
	}

	public long toJavaTime() {
		return wrapped.getMillis();
	}

	public boolean isEqual(PromptoDateTime actual) {
		return wrapped.isEqual(actual.wrapped);
	}




	
}
