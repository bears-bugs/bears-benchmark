package prompto.intrinsic;

import org.joda.time.LocalTime;

/* DateTime is final so can't just extend it */
public class PromptoTime implements Comparable<PromptoTime> {

	public static PromptoTime parse(String text) {
		return new PromptoTime(LocalTime.parse(text));
	}

	public static PromptoTime fromMillisOfDay(long utc) {
		return new PromptoTime(LocalTime.fromMillisOfDay(utc));
	}

	private LocalTime wrapped;

	public PromptoTime(LocalTime wrapped) {
		this.wrapped = wrapped;
	}
	
	public PromptoTime(int hour, int minute, int second, int millis) {
		this.wrapped = new LocalTime(hour, minute, second, millis);
	}

	@Override
	public int hashCode() {
		return wrapped.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof PromptoTime && wrapped.equals(((PromptoTime)obj).wrapped);
	}
	
	@Override
	public String toString() {
		return wrapped.toString();
	}
	
	@Override
	public int compareTo(PromptoTime o) {
		return wrapped.compareTo(o.wrapped);
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
	
	public long getNativeMillisOfDay() {
		return wrapped.getMillisOfDay();
	}

	public boolean isAfter(PromptoTime value) {
		return wrapped.isAfter(value.wrapped);
	}

	public PromptoTime plus(PromptoPeriod period) {
		return new PromptoTime(wrapped.plus(period.wrapped));
	}

	public PromptoTime minus(PromptoPeriod period) {
		return new PromptoTime(wrapped.minus(period.wrapped));
	}

	public String format(String formatString) {
		return wrapped.toString(formatString);
	}

	public boolean isEqual(PromptoTime actual) {
		return wrapped.isEqual(actual.wrapped);
	}

	public PromptoTime plusSeconds(long seconds) {
		return new PromptoTime(wrapped.plusSeconds((int)seconds));
	}

	public PromptoPeriod minus(PromptoTime other) {
		return new PromptoPeriod(0, 0, 0, 0, 
				this.getNativeHour() - other.getNativeHour(), 
				this.getNativeMinute() - other.getNativeMinute(), 
				this.getNativeSecond() - other.getNativeSecond(), 
				this.getNativeMillis() - other.getNativeMillis());
	}


}
